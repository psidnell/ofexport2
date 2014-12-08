<#--
$$$$$$$$$$$$$$$$$$
$ DEFINE CONSTANTS
$$$$$$$$$$$$$$$$$$
-->
<#global INDENT="  ">
<#--
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$ Walk over items in root node
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-->
<html>
  <head>
    <title>OmniFocus</title>
    <style type="text/css">
        
        body
        {
            background-color: #fdf6e3;
            font-family: Verdana, Arial, Helvetica, sans-serif;
            margin-left:2cm;
        }
        
        a:link
        {
           color:inherit;
           text-decoration: none;
        }
        
        a:hover
        {
          text-decoration: underline;
        }
        
        .Project
        {
            color: #b58900;
        }
        
        .Folder
        {
            color: #b58900;
        }

        .Context
        {
            color: #6c71c4;
        }
        
        .Task
        {
            color: #586e75;
            font-size: 1.0em;
        }
        
        .TaskGroup
        {
            color: #586e75;
            font-weight: bold;
            font-size: 1.0em;
        }
        
        .Attrib
        {
            font-size: 0.8em;
        }
        
        .AStart
        {
            color: #2aa198;
        }
        
        .ADue
        {
            color: #dc322f;
        }
        
        .AComplete
        {
            color: #839496;
        }
        
        .AFlagged
        {
            color: #d33682;
        }
        
        .AProject
        {
            color: #b58900;
        }
        
        .AContext
        {
            color: #6c71c4;
        }
    </style>
  </head>
  <body>
<#if root.type == "Folder">
  <#list root.folders as f>
    <@doFolder folder=f depth=0/>
  </#list>
  <#list root.projects as p>
  <@doProject project=p depth=0/>
  </#list>
<#elseif root.type == "Context">
  <#list root.contexts as c>
  <@doContext context=c depth=0/>
  </#list>
  <#list root.tasks as task>
  <@doTask task=t depth=0 projectMode=false/>
  </#list>
</#if>
  </body>
<html>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder depth>
<@doIndent indent=depth/><H${depth+1} class="Folder"><a href="omnifocus:///folder/${folder.id}">${escape(folder.name)}</a></H${depth+1}><ul>
<#list folder.folders as f><@doFolder folder=f depth=(depth+1)/></#list>
<#list folder.projects as p><@doProject project=p depth=(depth+1)/></#list>
<@doIndent indent=depth/></ul>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project depth>
<@doIndent indent=depth/><H${depth+1} class="Project"><a href="omnifocus:///task/${project.id}">${escape(project.name)}</a><@doAttribs project/></H${depth+1}><ul>
<@doNote node=project depth=depth/>
<#list project.tasks as t><@doTask task=t depth=depth+1 projectMode=true/></#list>
<@doIndent indent=depth/></ul>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task depth, projectMode>
<@doIndent indent=depth/><li class="Task"><a href="omnifocus:///task/${task.id}">${escape(task.name)}</a><@doAttribs task/></li><ul>
<@doNote node=task depth=depth/>
<#if projectMode><#list task.tasks as t><@doTask task=t depth=depth+1  projectMode=projectMode/></#list></#if>
<@doIndent indent=depth/></ul>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context depth>
<@doIndent indent=depth/><H${depth+1} class="Context"><a href="omnifocus:///context/${context.id}">${escape(context.name)}</a><@doAttribs context/></H${depth+1}>
<#list context.contexts as c><@doContext context=c depth=depth+1/></#list>
<#list context.tasks as t><@doTask task=t depth=depth+1 projectMode=false/></#list>
<@doIndent indent=depth/></ul>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doIndent
$$$$$$$$$$$$$$$$$$
-->
<#macro doIndent indent><#list 0..(indent+1) as i>${INDENT}</#list></#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doAttribs
$$$$$$$$$$$$$$$
Using Java SimpleDateFormat conversion
-->
<#macro doAttribs node>
<span class="Attrib"><#if (node.flagged)??> <span class="AFlagged">FLAGGED</span></#if><#if (node.defer.getDate())??> <span class="AStart">${node.defer.getDate()?string[config.template_date_format]}</span></#if><#if (node.due.getDate())??> <span class="ADue">${node.due.getDate()?string[config.template_date_format]}</span></#if><#if (node.completion.getDate())??> <span class="AComplete">${node.completion.getDate()?string[config.template_date_format]}</span></#if></span></#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doNote
$$$$$$$$$$$$$$$
-->
<#macro doNote node depth>
<#if (node.note)??>${node.formatNote(depth+3, INDENT, "<br>")}</#if></#macro>
<#--
$$$$$$$$$$$$$$
$ FUNCTION: escape
$$$$$$$$$$$$$$$
-->
<#function escape val>
  <#return val?html?replace("\n", "&#10;")>
</#function>