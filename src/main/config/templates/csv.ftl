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
    <@doFolder folder=f/>
  </#list>
  <#list projects as p>
  <@doProject project=p/>
  </#list>
<#elseif type == "Context">
  <#list contexts as c>
  <@doContext context=c/>
  </#list>
  <#list tasks as task>
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
"${node.name?replace("\"","'")}"<#if (node.flagged)??>,"FLAGGED"<#else>,</#if><#if (node.deferDate)??>,"${node.deferDate?string["yyyy-MM-dd"]}"<#else>,</#if><#if (node.dueDate)??>,"${node.dueDate?string["yyyy-MM-dd"]}"<#else>,</#if><#if (node.completionDate)??>,"${node.completionDate?string["yyyy-MM-dd"]}"<#else>,</#if><#if (node.note)??>,"${node.note?replace("\"","'")?replace("\n"," ")}"</#if>
</#macro>
 
