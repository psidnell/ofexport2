# Example FreeMarker Template File

This is FreeMarker template file, which happens to be in Markdown format.

These template files are text files with embeded "magic strings" that refer
to the data extracted from OmniFocus.

<#--
Note that this text is in a comment structure and won't appear in the final output.
 -->

FreeMarker documentation can be found here: http://freemarker.org

## The Data Model

The data provided to FreeMarker is a tree of Folders, Projects, Contexts and Tasks representing the options
specified on the ofexport commandline.

Specifically, the template is given a root Folder or Context (depending on the mode ofexport is run
in).

We can access the root objects attributes like this:

RootItem:
	name: ${name}
	type: ${type}

To get a list of available attributes for all the node types run the command '**ofexport -i**'.

## Traversing The Object Model

Things get interesting when we want to walk the whole object model.

First we need to see what type the root is so we can access it's contents:

<#if type == "Folder">
  Root is a Folder
<#elseif type == "Context">
  Root is a Context 
<#else>
  That's unexpected!
</#if>

Once we know that we can walk down the tree:
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
  <@doTask task=t depth=0/>
  </#list>
</#if>
<#--
$$$$$$$$$$$$$$$$$
$ MACRO: doFolder 
$$$$$$$$$$$$$$$$$
-->
<#macro doFolder folder depth>
<@doIndent indent=depth/>Folder: ${folder.name}
<#list folder.folders as f><@doFolder folder=f depth=depth+1/></#list>
<#list folder.projects as p><@doProject project=p depth=depth+1/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doProject 
$$$$$$$$$$$$$$$$$$
 -->
<#macro doProject project depth>
<@doIndent indent=depth/>Project: ${project.name}
<#list project.tasks as t><@doTask task=t depth=depth+1/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$
$ MACRO: doTask 
$$$$$$$$$$$$$$$
-->
<#macro doTask task depth>
<@doIndent indent=depth/>Task: ${task.name}
<#list task.tasks as t><@doTask task=t depth=depth+1/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doContext
$$$$$$$$$$$$$$$$$$
-->
<#macro doContext context depth>
<@doIndent indent=depth/>Context: ${context.name}
<#list context.contexts as c><@doContext context=c depth=depth+1/></#list>
<#list context.tasks as t><@doTask task=t depth=depth+1/></#list>
</#macro>
<#--
$$$$$$$$$$$$$$$$$$
$ MACRO: doIndent
$$$$$$$$$$$$$$$$$$
-->
<#macro doIndent indent><#list 0..indent as i>${INDENT}</#list></#macro>
 
