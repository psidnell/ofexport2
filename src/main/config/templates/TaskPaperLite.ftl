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
- ${task.name}<#if task.completed><@doDateTag task.completionDate/></#if>
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
$$$$$$$$$$$$$$$$$$
$ MACRO: doDateTag
$$$$$$$$$$$$$$$$$$
Using Java SimpleDateFormat conversion
-->
<#macro doDateTag date> @${date?string["yyyy-MM-dd-EEE"]}</#macro>
 
 