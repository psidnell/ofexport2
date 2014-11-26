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
<opml version="1.0">
  <head>
    <title>OmniFocus</title>
  </head>
  <body>
<#if type == "Folder">
  <#list folders as f>
    <@doFolder folder=f depth=0/>
  </#list>
  <#list projects as p>
  <@doProject project=p depth=0/>
  </#list>
<#elseif type == "Context">
  <#list contexts as c>
  <@doContext context=c depth=0/>
  </#list>
  <#list tasks as task>
  <@doTask task=t depth=0 projectMode=false/>
  </#list>
</#if>
  </body>
</opml>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder depth>
<@doIndent indent=depth/><outline text="${escape(folder.name)}">
<#list folder.folders as f><@doFolder folder=f depth=(depth+1)/></#list>
<#list folder.projects as p><@doProject project=p depth=(depth+1)/></#list>
<@doIndent indent=depth/></outline>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project depth>
<@doIndent indent=depth/><outline text="${escape(project.name)}"<@doAttribs node=project/>>
<#list project.tasks as t><@doTask task=t depth=depth+1 projectMode=true/></#list>
<@doIndent indent=depth/></outline>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task depth, projectMode>
<@doIndent indent=depth/><outline text="${escape(task.name)}"<@doAttribs node=task/>>
<#if projectMode><#list task.tasks as t><@doTask task=t depth=depth+1  projectMode=projectMode/></#list></#if>
<@doIndent indent=depth/></outline>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context depth>
<@doIndent indent=depth/><outline text="${escape(context.name)}">
<#list context.contexts as c><@doContext context=c depth=depth+1/></#list>
<#list context.tasks as t><@doTask task=t depth=depth+1 projectMode=false/></#list>
<@doIndent indent=depth/></outline>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doIndent
$$$$$$$$$$$$$$$$$$
-->
<#macro doIndent indent><#if (indent > 0)><#list 0..(indent-1) as i>${INDENT}</#list></#if></#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doAttribs
$$$$$$$$$$$$$$$
Using Java SimpleDateFormat conversion
-->
<#macro doAttribs node>
<#if node.completed> completed="${node.completionDate?string["yyyy-MM-dd"]}"</#if><#if node.flagged> flagged="flagged"</#if><#if (node.contextName)??> context="${node.contextName?xml}"</#if><#if (node.note)??> _note="${escape(node.note)}"</#if></#macro>
<#--
$$$$$$$$$$$$$$
$ FUNCTION: escape
$$$$$$$$$$$$$$$
-->
<#function escape val>
  <#return val?xml?replace("\n", "&#10;")>
</#function>