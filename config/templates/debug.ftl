<#--
$$$$$$$$$$$$$$$$$$
$ DEFINE CONSTANTS
$$$$$$$$$$$$$$$$$$
-->
<#global INDENT=". ">
<#--
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$ Walk over items in root node
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-->
<#if root.type == "Folder">
  <@doFolder folder=root depth=0/>
<#elseif root.type == "Context">
  <@doContext context=root depth=0/>
</#if>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder depth>
<@doIndent indent=depth/>Folder id:${folder.id} depth:${depth}
<@doIndent indent=depth/>. -name:${folder.name}
<@doIndent indent=depth/>. -folderCount:${folder.folderCount}
<@doIndent indent=depth/>. -included:${folder.included?c}
<@doIndent indent=depth/>. -projectCount:${folder.projectCount}
<@doIndent indent=depth/>. -type:${folder.type}
<#list folder.folders as f><@doFolder folder=f depth=(depth+1)/></#list>
<#list folder.projects as p><@doProject project=p depth=(depth+1)/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project depth>
<@doIndent indent=depth/>Project id:${project.id} depth:${depth}
<@doIndent indent=depth/>. -name:${project.name}
<@doIndent indent=depth/>. -available:${project.available?c}
<@doIndent indent=depth/>. -completed${project.completed?c}
<@doIndent indent=depth/>. -completionDate:<#if (project.completionDate)??>${project.completionDate?string[config.template_date_format]}<#else>null</#if>
<@doIndent indent=depth/>. -contextName:<#if (project.contextName)??>${project.contextName}<#else>null></#if>
<@doIndent indent=depth/>. -deferDate:<#if (project.deferDate)??>${project.deferDate?string[config.template_date_format]}<#else>null</#if>
<@doIndent indent=depth/>. -dueDate:<#if (project.dueDate)??>${project.dueDate?string[config.template_date_format]}<#else>null</#if>
<@doIndent indent=depth/>. -flagged:${project.flagged?c}
<@doIndent indent=depth/>. -included:${project.included?c}
<@doIndent indent=depth/>. -remaining:${project.remaining?c}
<@doIndent indent=depth/>. -sequential:${project.sequential?c}
<@doIndent indent=depth/>. -status:${project.status}
<@doIndent indent=depth/>. -taskCount:${project.taskCount}
<@doIndent indent=depth/>. -type:${project.type}
<@doIndent indent=depth/>. -note:<#if (project.note)??>
${project.formatNote(depth, "  ")}<#else>null</#if>
<#list project.tasks as t><@doTask task=t depth=depth+1 projectMode=true/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task depth, projectMode>
<@doIndent indent=depth/>Task id:${task.id} depth:${depth}
<@doIndent indent=depth/>. -name:${task.name}
<@doIndent indent=depth/>. -available:${task.available?c}
<@doIndent indent=depth/>. -blocked:${task.blocked?c}
<@doIndent indent=depth/>. -completed:${task.completed?c}
<@doIndent indent=depth/>. -completionDate:<#if (task.completionDate)??>${task.completionDate?string[config.template_date_format]}<#else>null</#if>
<@doIndent indent=depth/>. -contextName:<#if (task.contextName)??>${task.contextName}<#else>null></#if>
<@doIndent indent=depth/>. -deferDate:<#if (task.deferDate)??>${task.deferDate?string[config.template_date_format]}<#else>null</#if>
<@doIndent indent=depth/>. -dueDate:<#if (task.dueDate)??>${task.dueDate?string[config.template_date_format]}<#else>null</#if>
<@doIndent indent=depth/>. -flagged:${task.flagged?c}
<@doIndent indent=depth/>. -included:${task.included?c}
<@doIndent indent=depth/>. -projectTask:${task.projectTask?c}
<@doIndent indent=depth/>. -remaining:${task.remaining?c}
<@doIndent indent=depth/>. -sequential:${task.sequential?c}
<@doIndent indent=depth/>. -taskCount:${task.taskCount}
<@doIndent indent=depth/>. -type:${task.type}
<@doIndent indent=depth/>. -note:<#if (task.note)??>
${task.formatNote(depth, "  ")}<#else>null</#if>
<#if projectMode><#list task.tasks as t><@doTask task=t depth=depth+1  projectMode=projectMode/></#list></#if>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context depth>
<@doIndent indent=depth/>Task id:${context.id} depth:${depth}
<@doIndent indent=depth/>. -name:${context.name}
<@doIndent indent=depth/>. -contextCount:${context.taskCount}
<@doIndent indent=depth/>. -included:${context.included?c}
<@doIndent indent=depth/>. -taskCount:${context.taskCount}
<@doIndent indent=depth/>. -type:${context.type}
<#list context.contexts as c><@doContext context=c depth=depth+1/></#list>
<#list context.tasks as t><@doTask task=t depth=depth+1 projectMode=false/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doIndent
$$$$$$$$$$$$$$$$$$
-->
<#macro doIndent indent><#if (indent > 0)><#list 0..(indent-1) as i>${INDENT}</#list></#if></#macro>
