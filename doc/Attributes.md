# Attributes.md

## Folder:

- boolean **active** : true if active.
- date **added** : added date.
- boolean **all** : true for all nodes.
- boolean **dropped** : true if dropped.
- int **folderCount** : number of sub folders.
- list **folders** : the sub folders.
- date **modified** : modified date.
- string **name** : item name/text.
- int **projectCount** : number of sub projects.
- list **projects** : the sub projects
- int **rank** : used to define sort order of items.
- string **type** : the items type: 'Folder'.

## Project:

- boolean **active** : project status is active.
- date **added** : added date.
- boolean **all** : true for all nodes.
- boolean **completed** : item is complete.
- date **completion** : completion date.
- date **completionDate** : date item was completed or null.
- string **contextName** : the context name or null.
- date **defer** : defer date.
- date **deferDate** : date item is to start or null.
- boolean **dropped** : project is dropped.
- date **due** : due date.
- date **dueDate** : date item is due or null.
- integer **estimatedMinutes** : estimated minutes.
- boolean **flagged** : item is flagged.
- date **modified** : modified date.
- string **name** : item name/text.
- string **note** : note text.
- boolean **onHold** : project status is on hold.
- int **rank** : used to define sort order of items.
- boolean **remaining** : item is remaining.
- boolean **sequential** : item is sequential.
- boolean **singleActionList** : true if it's a sinle action list
- string **status** : the items status: 'active', 'inactive', 'done or 'dropped'.
- int **taskCount** : number of tasks.
- list **tasks** : the sub tasks.
- string **type** : the items type: 'Project'.
- int **uncompletedTaskCount** : number of uncompleted tasks.
- boolean **unflagged** : item is not flagged.

## Task:

- date **added** : added date.
- boolean **all** : true for all nodes.
- boolean **available** : item is available.
- boolean **blocked** : item is blocked.
- boolean **completed** : item is complete.
- date **completion** : completion date.
- date **completionDate** : date item was completed or null.
- string **contextName** : the context name or null.
- date **defer** : defer date.
- date **deferDate** : date item is to start or null.
- date **due** : due date.
- date **dueDate** : date item is due or null.
- integer **estimatedMinutes** : estimated minutes.
- boolean **flagged** : item is flagged.
- date **modified** : modified date.
- string **name** : item name/text.
- string **note** : note text.
- boolean **projectTask** : true if task represents a project.
- int **rank** : used to define sort order of items.
- boolean **remaining** : item is remaining.
- boolean **sequential** : item is sequential.
- int **taskCount** : number of tasks.
- list **tasks** : the sub tasks.
- string **type** : the items type: 'Task'.
- int **uncompletedTaskCount** : number of uncompleted tasks.
- boolean **unflagged** : item is not flagged.

## Context:

- boolean **active** : true if context is active.
- date **added** : added date.
- boolean **all** : true for all nodes.
- int **contextCount** : number of contexts.
- boolean **dropped** : true if context is dropped.
- date **modified** : modified date.
- string **name** : item name/text.
- boolean **onHold** : true if context is on hold.
- int **rank** : used to define sort order of items.
- int **taskCount** : number of tasks.
- list **tasks** : the sub tasks.
- string **type** : the items type: 'Context'.
- int **uncompletedTaskCount** : number of uncompleted tasks.

