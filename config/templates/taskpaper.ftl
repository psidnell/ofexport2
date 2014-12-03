<#--
$$$$$$$$$$$$$$$$$$
$ DEFINE CONSTANTS
$$$$$$$$$$$$$$$$$$
-->
<#global INDENT="\t">
<#--
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$ Walk over items in root node
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-->
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
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder depth>
<@doIndent indent=depth/>${folder.name}:
<#list folder.folders as f><@doFolder folder=f depth=(depth+1)/></#list>
<#list folder.projects as p><@doProject project=p depth=(depth+1)/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project depth>
<@doIndent indent=depth/>${project.name}:<@doTags project/>
<@doNote node=project depth=depth+1/>
<#list project.tasks as t><@doTask task=t depth=depth+1 projectMode=true/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task depth, projectMode>
<@doIndent indent=depth/>
- ${task.name}<@doTags task/>
<@doNote node=task depth=depth+1/>
<#if projectMode><#list task.tasks as t><@doTask task=t depth=depth+1  projectMode=projectMode/></#list></#if>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context depth>
<@doIndent indent=depth/>${context.name}:
<#list context.contexts as c><@doContext context=c depth=depth+1/></#list>
<#list context.tasks as t><@doTask task=t depth=depth+1 projectMode=false/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doIndent
$$$$$$$$$$$$$$$$$$
-->
<#macro doIndent indent><#if (indent > 0)><#list 0..(indent-1) as i>${INDENT}</#list></#if></#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTags
$$$$$$$$$$$$$$$
Using Java SimpleDateFormat conversion
-->
<#macro doTags node>
<#if (node.completionDate)??> @done(${node.completionDate?string[config.template_date_format]})</#if><#if node.flagged> @flagged</#if><#if (node.dueDate)??> @done(${node.dueDate?string["yyyy-MM-dd"]})</#if><#if (node.contextName)??> @${node.contextName}</#if></#macro> 
<#--
$$$$$$$$$$$$$$$
$ MACRO: doNote
$$$$$$$$$$$$$$$
-->
<#macro doNote node depth>
<#if (node.note)??>${node.formatNote(depth, INDENT)}</#if></#macro> 
 