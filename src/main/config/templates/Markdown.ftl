<#--
$$$$$$$$$$$$$$$$$$
$ DEFINE CONSTANTS
$$$$$$$$$$$$$$$$$$
-->
<#global INDENT="  ">
<#global lastWasText=false>
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
<#if lastWasText>
</#if>
<@doHeading indent=headingDepth/>${folder.name}

<#assign lastWasText=false>
<#list folder.folders as f><@doFolder folder=f headingDepth=(headingDepth+1)/></#list>
<#list folder.projects as p><@doProject project=p headingDepth=(headingDepth+1)/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project headingDepth>
<#if lastWasText>

</#if>
<@doHeading indent=headingDepth/>${project.name}

<#assign lastWasText=false>
<@doNote node=project indent=0/>
<#list project.tasks as t><@doTask task=t textDepth=0 projectMode=true/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task textDepth, projectMode>
<@doIndent indent=textDepth/>
- ${task.name}
<#assign lastWasText=true>
<@doNote node=task indent=textDepth+1/>
<#if projectMode><#list task.tasks as t><@doTask task=t textDepth=textDepth+1 projectMode=projectMode/></#list></#if>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context headingDepth>
<#if lastWasText>

</#if>
<@doHeading indent=headingDepth/>${context.name}

<#assign lastWasText=false>
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
<#--
$$$$$$$$$$$$$$$
$ MACRO: doNote
$$$$$$$$$$$$$$$
-->
<#macro doNote node indent>
<#if (node.note)??>${node.formatNote(indent, INDENT)}</#if></#macro> 
 