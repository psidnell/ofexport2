BEGIN:VCALENDAR
VERSION:2.0
X-WR-CALNAME:OmniFocus
PRODID:-//ofexport//ofexport//EN
METHOD:PUBLISH
CALSCALE:GREGORIAN
<#--
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$ Walk over items in root node
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
-->
<#if root.type == "Folder">
  <@doFolder folder=root/>
<#elseif root.type == "Context">
  <@doContext context=root/>
</#if>
END:VCALENDAR
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder>
<#list folder.folders as f><@doFolder folder=f/></#list>
<#list folder.projects as p><@doProject project=p/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project>
<#list project.tasks as t><@doTask task=t projectMode=true/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context>
<#list context.tasks as t><@doTask task=t projectMode=false/></#list>
<#list context.contexts as c><@doContext context=c /></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task projectMode>
<#if task.icsHasCalendarData>
BEGIN:VEVENT
DTSTART:${task.icsStart}
DTEND:${task.icsEnd}
DTSTAMP:${task.icsModified}
SUMMARY:${task.name}
URL:omnifocus:///task/${task.id}
UID:omnifocus${task.id}
DESCRIPTION:
<#if task.icsHasAlarm>
BEGIN:VALARM
ACTION:DISPLAY
DESCRIPTION:OmniFocus Reminder
TRIGGER:-PT${task.icsAlarmMinutes}M
END:VALARM
</#if>
END:VEVENT
</#if>
<#if projectMode><#list task.tasks as t><@doTask task=t projectMode=projectMode/></#list></#if>
</#macro>

