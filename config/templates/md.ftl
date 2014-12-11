<#--
$$$$$$$$$$$$$$$$$$
$ DEFINE CONSTANTS
$$$$$$$$$$$$$$$$$$
-->
<#global INDENT="    ">
<#global blankline=true>
<#--
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$ Walk over items in root node
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-->
<#if root.type == "Folder">
  <#list root.folders as f>
    <@doFolder folder=f headingDepth=0/>
  </#list>
  <#list root.projects as p>
  <@doProject project=p headingDepth=0/>
  </#list>
<#elseif root.type == "Context">
  <#list root.contexts as c>
  <@doContext context=c headingDepth=0/>
  </#list>
  <#list root.tasks as task>
  <@doTask task=t headingDepth=0 textDepth=0 projectMode=false/>
  </#list>
</#if>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder headingDepth>
<@doSmartBlankline/>
<@doHeading indent=headingDepth/>${folder.name}
<#assign blankline=true>

<#list folder.folders as f><@doFolder folder=f headingDepth=(headingDepth+1)/></#list>
<#list folder.projects as p><@doProject project=p headingDepth=(headingDepth+1)/></#list>
<@doSmartBlankline/>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project headingDepth>
<@doSmartBlankline/>
<@doHeading indent=headingDepth/>${project.name}
<#assign blankline=true>

<#if (project.note)?? && project.note != "">
${project.formatNote(1, "> ")}<#assign blankline=true>
</#if>
<@doSmartBlankline/>
<#list project.tasks as t><@doTask task=t textDepth=0 projectMode=true/></#list>
<@doSmartBlankline/>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task textDepth, projectMode>
<@doIndent indent=textDepth/>- ${task.name}<#if (task.note)?? && task.note != "">

${task.formatNote(textDepth+1, "> ")}</#if><#assign blankline=false>
<#if projectMode><#list task.tasks as t><@doTask task=t textDepth=textDepth+1 projectMode=projectMode/></#list></#if></#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context headingDepth>
<@doSmartBlankline/>
<@doHeading indent=headingDepth/>${context.name}
<#assign blankline=true>

<#list context.tasks as t><@doTask task=t textDepth=0 projectMode=false/></#list>
<#list context.contexts as c><@doContext context=c headingDepth=headingDepth+1/></#list>
<@doSmartBlankline/>
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
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doSmartSpace
$$$$$$$$$$$$$$$$$$
-->
<#macro doSmartBlankline><#if !blankline><#assign blankline=true>

</#if></#macro>
