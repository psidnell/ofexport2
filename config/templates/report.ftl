<#--
$$$$$$$$$$$$$$$$$$
$ DEFINE CONSTANTS
$$$$$$$$$$$$$$$$$$
-->
<#global INDENT="\t">
<#global TODAY=root.date("today")>
${config.reportTitle}:
<#--
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$ Walk over items in root node
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-->
<#if root.type == "Folder">
  <#list root.folders as f>
    <@doFolder folder=f depth=1/>
  </#list>
  <#list root.projects as p>
  <@doProject project=p depth=1/>
  </#list>
<#elseif root.type == "Context">
  <#list root.contexts as c>
  <@doContext context=c depth=1/>
  </#list>
  <#list root.tasks as task>
  <@doTask task=t depth=1 projectMode=false/>
  </#list>
</#if>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder depth>
<#list folder.folders as f><@doFolder folder=f depth=(depth)/></#list>
<#list folder.projects as p><@doProject project=p depth=(depth)/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project depth>
<@doIndent indent=depth/>${project.name}:
<#list project.tasks as t><@doTask task=t depth=depth+1 projectMode=true/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task depth, projectMode>
<@doIndent indent=depth/>
- ${task.name}<#if (task.completion.getDate())?? && (task.completion.getDate())??><@doDateTag task.completion.getDate()/></#if>
<#if projectMode><#list task.tasks as t><@doTask task=t depth=depth+1  projectMode=projectMode/></#list></#if>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context depth>
<@doIndent indent=depth/>${context.name}:
<#list context.tasks as t><@doTask task=t depth=depth+1 projectMode=false/></#list>
<#list context.contexts as c><@doContext context=c depth=depth+1/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doIndent
$$$$$$$$$$$$$$$$$$
-->
<#macro doIndent indent><#if (indent > 0)><#list 0..(indent-1) as i>${INDENT}</#list></#if></#macro>
 <#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doDateTag
$$$$$$$$$$$$$$$$$$
Using Java SimpleDateFormat conversion
-->
<#macro doDateTag date> @${date?string[config.reportDateFormat]}</#macro>
 
 