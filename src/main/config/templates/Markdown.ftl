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
<#if type == "Folder">
  <#list folders as f>
    <@doFolder folder=f headingDepth=0/>
  </#list>
  <#list projects as p>
  <@doProject project=p headingDepth=0/>
  </#list>
<#elseif type == "Context">
  <#list contexts as c>
  <@doContext context=c headingDepth=0/>
  </#list>
  <#list tasks as task>
  <@doTask task=t headingDepth=0 textDepth=0 projectMode=false/>
  </#list>
</#if>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder headingDepth>
<@doHeading indent=headingDepth/>${folder.name}

<#list folder.folders as f><@doFolder folder=f headingDepth=(headingDepth+1)/></#list>
<#list folder.projects as p><@doProject project=p headingDepth=(headingDepth+1)/></#list>

</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project headingDepth>
<@doHeading indent=headingDepth/>${project.name}

<#if (project.note)?? && project.note != "">
${project.formatNote(1, "> ")}
</#if>
<#list project.tasks as t><@doTask task=t textDepth=0 projectMode=true/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task textDepth, projectMode>
<@doIndent indent=textDepth/>- ${task.name}<#if (task.note)?? && task.note != "">

${task.formatNote(textDepth+1, "> ")}</#if>
<#if projectMode><#list task.tasks as t><@doTask task=t textDepth=textDepth+1 projectMode=projectMode/></#list></#if>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context headingDepth>
<@doHeading indent=headingDepth/>${context.name}

<#list context.contexts as c><@doContext context=c headingDepth=headingDepth+1/></#list>
<#list context.tasks as t><@doTask task=t textDepth=0 projectMode=false/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doHeading
$$$$$$$$$$$$$$$$$$
-->
<#macro doHeading indent>#<#if (indent > 0)><#list 0..(indent-1) as i>#</#list></#if> </#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doIndent
$$$$$$$$$$$$$$$$$$
-->
<#macro doIndent indent><#if (indent > 0)><#list 0..(indent-1) as i>${INDENT}</#list></#if></#macro>
 