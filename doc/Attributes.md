# Attributes.md

    Folder:
        boolean active       : true if active.
        boolean all          : true for all nodes.
        boolean dropped      : true if dropped.
            int folderCount  : number of sub folders.
           list folders      : the sub folders.
         string name         : item name/text.
            int projectCount : number of sub projects.
           list projects     : the sub projects
         string type         : the items type: 'Folder'.
    
    Project:
        boolean active                                  : project status is active.
        boolean all                                     : true for all nodes.
        boolean completed                               : item is complete.
           date completionDate                          : date item was completed or null.
        boolean completionDateBetween (dateFrom,dateTo) : true if date within range.
        boolean completionDateIs (date)                 : true if date matches.
         string contextName                             : the context name or null.
           date deferDate                               : date item is to start or null.
        boolean deferDateBetween (dateFrom,dateTo)      : true if date within range.
        boolean deferDateIs (date)                      : true if date matches.
        boolean dropped                                 : project is dropped.
           date dueDate                                 : date item is due or null.
        boolean dueDateBetween (dateFrom,dateTo)        : true if date within range.
        boolean dueDateIs (date)                        : true if date matches.
        boolean dueSoon                                 : due soon.
        integer estimatedMinutes                        : estimated minutes.
        boolean flagged                                 : item is flagged.
         string name                                    : item name/text.
         string note                                    : note text.
        boolean onHold                                  : project status is on hold.
        boolean remaining                               : item is remaining.
        boolean sequential                              : item is sequential.
        boolean singleActionList                        : true if it's a sinle action list
         string status                                  : the items status: 'active', 'inactive', 'done or 'dropped'.
            int taskCount                               : number of tasks.
           list tasks                                   : the sub tasks.
         string type                                    : the items type: 'Project'.
            int uncompletedTaskCount                    : number of uncompleted tasks.
        boolean unflagged                               : item is not flagged.
    
    Context:
        boolean active               : true if context is active.
        boolean all                  : true for all nodes.
            int contextCount         : number of contexts.
        boolean dropped              : true if context is dropped.
         string name                 : item name/text.
        boolean onHold               : true if context is on hold.
            int taskCount            : number of tasks.
           list tasks                : the sub tasks.
         string type                 : the items type: 'Context'.
            int uncompletedTaskCount : number of uncompleted tasks.
    
    Task:
        boolean all                                     : true for all nodes.
        boolean available                               : item is available.
        boolean blocked                                 : item is blocked.
        boolean completed                               : item is complete.
           date completionDate                          : date item was completed or null.
        boolean completionDateBetween (dateFrom,dateTo) : true if date within range.
        boolean completionDateIs (date)                 : true if date matches.
         string contextName                             : the context name or null.
           date deferDate                               : date item is to start or null.
        boolean deferDateBetween (dateFrom,dateTo)      : true if date within range.
        boolean deferDateIs (date)                      : true if date matches.
           date dueDate                                 : date item is due or null.
        boolean dueDateBetween (dateFrom,dateTo)        : true if date within range.
        boolean dueDateIs (date)                        : true if date matches.
        boolean dueSoon                                 : due soon.
        integer estimatedMinutes                        : estimated minutes.
        boolean flagged                                 : item is flagged.
         string name                                    : item name/text.
         string note                                    : note text.
        boolean projectTask                             : true if task represents a project.
        boolean remaining                               : item is remaining.
        boolean sequential                              : item is sequential.
            int taskCount                               : number of tasks.
           list tasks                                   : the sub tasks.
         string type                                    : the items type: 'Task'.
            int uncompletedTaskCount                    : number of uncompleted tasks.
        boolean unflagged                               : item is not flagged.
