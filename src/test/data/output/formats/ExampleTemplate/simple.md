# Example FreeMarker Template File

This is FreeMarker template file, which happens to be in Markdown format.

These template files are text files with embeded "magic strings" that refer
to the data extracted from OmniFocus.


FreeMarker documentation can be found here: http://freemarker.org

## The Data Model

The data provided to FreeMarker is a tree of Folders, Projects, Contexts and Tasks representing the options
specified on the ofexport commandline.

Specifically, the template is given a root Folder or Context (depending on the mode ofexport is run
in).

We can access the root objects attributes like this:

RootItem:
	name: RootFolder
	type: Folder

To get a list of available attributes for all the node types run the command '**ofexport -i**'.

## Traversing The Object Model

Things get interesting when we want to walk the whole object model.

First we need to see what type the root is so we can access it's contents:

  Root is a Folder

Once we know that we can walk down the tree:
Folder: %Test Folder
  Project: %Test Project Foo Non Empty
    Task: %TestToDo
      Task: %Test Sub task
    Task: %Test completed
  Project: %Test Project Bar Empty
