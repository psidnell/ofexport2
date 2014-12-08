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
<#if root.type == "Folder">
  <#list root.folders as f>
    <@doFolder folder=f/>
  </#list>
  <#list root.projects as p>
  <@doProject project=p/>
  </#list>
<#elseif root.type == "Context">
  <#list root.contexts as c>
  <@doContext context=c/>
  </#list>
  <#list root.tasks as task>
  <@doTask task=t projectMode=false/>
  </#list>
</#if>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder>
<@doRow folder/>
<#list folder.folders as f><@doFolder folder=f/></#list>
<#list folder.projects as p><@doProject project=p/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project>
<@doRow project/>
<#list project.tasks as t><@doTask task=t projectMode=true/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task projectMode>
<@doRow task/>
<#if projectMode><#list task.tasks as t><@doTask task=t projectMode=projectMode/></#list></#if>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context>
<@doRow context/>
<#list context.contexts as c><@doContext context=c/></#list>
<#list context.tasks as t><@doTask task=t projectMode=false/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doRow
$$$$$$$$$$$$$$$$$$
-->
<#macro doRow node>
"${node.name?replace("\"","'")}"<#if (node.flagged)??>,"FLAGGED"<#else>,</#if><#if (node.defer.getDate())??>,"${node.defer.getDate()?string[config.template_date_format]}"<#else>,</#if><#if (node.due.getDate())??>,"${node.due.getDate()?string[config.template_date_format]}"<#else>,</#if><#if (node.completion.getDate())??>,"${node.completion.getDate()?string[config.template_date_format]}"<#else>,</#if><#if (node.note)??>,"${node.note?replace("\"","'")?replace("\n"," ")}"</#if>
</#macro>
 
