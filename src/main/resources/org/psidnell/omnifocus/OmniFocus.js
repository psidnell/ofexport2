
// Useful Resources:
// https://developer.apple.com/library/mac/releasenotes/InterapplicationCommunication/RN-JavaScriptForAutomation/index.html
// Intro: http://pixelsnatch.com/blog/2014/10/24/using-javascript-with-omnifocus.html
// Projects: https://github.com/brandonpittman/omnifocus
// Key file: https://github.com/brandonpittman/OmniFocus/blob/master/OmniFocusLibrary.js

var app = Application('OmniFocus');
app.includeStandardAdditions = true;

var doc = app.defaultDocument;

function nameOf (o) {
    if (o == null) {
        return null;
    }
    else  {
        return o.name();
    }
}

function adaptTask (o) {
    return {
            name: o.name(),
            containingProject: nameOf(o.containingProject()),
            context: nameOf(o.context()),
            id: o.id(),
            note: o.note(),
            deferDate: o.deferDate(),
            dueDate: o.dueDate(),
            completionDate: o.completionDate(),
            completed: o.completed(),
            sequential: o.sequential(),
            next: o.next(),
            blocked: o.blocked(),
            flagged: o.flagged()
    };
}

function adaptProject (o) {
    return {
        name: o.name(),
        context: nameOf(o.context()),
        id: o.id(),
        note: o.note(),
        deferDate: o.deferDate(),
        dueDate: o.dueDate(),
        completionDate: o.completionDate(),
        completed: o.completed(),
        sequential: o.sequential(),
        flagged: o.flagged()
    }
}

function adaptContext (o) {
    return {
        name: o.name(),
        id: o.id(),
    }
}

function adaptContexts (contexts) {
    result = [];
    for (i in contexts) {
        result.push (adaptContext(contexts[i]));
    }
    return result;
}

function adaptProjects (projects) {
    result = [];
    for (i in projects) {
        result.push (adaptProject(projects[i]));
    }
    return result;
}

function adaptTasks (tasks) {
    result = [];
    for (i in tasks) {
        result.push (adaptTask(tasks[i]));
    }
    return result;
}

function _getContext(filter) {
    if(typeof(filter)==='undefined') {
        return doc.flattenedContexts();
    }
    else {
        return doc.flattenedContexts.whose(filter);
    }
}

function _getProject(filter) {
    if(typeof(filter)==='undefined') {
        return doc.flattenedProjects();
    }
    else {
        return doc.flattenedProjects.whose(filter);
    }
}

function getContexts(filter) {
    if(typeof(filter)==='undefined') {
        return adaptContexts(doc.flattenedContexts);
    }
    else {
        return adaptContexts(doc.flattenedContexts.whose(filter));
    }
}

function getProjects(filter) {
    if(typeof(filter)==='undefined') {
        return adaptProjects(doc.flattenedProjects());
    }
    else {
        return adaptProjects(doc.flattenedProjects.whose(filter));
    }
}

function getAllTasksFromContext(id, filter) {
    var context = _getContext({id : id})[0];
    if(typeof(filter)==='undefined') {
        return adaptTasks(context.tasks());
    }
    else {
        return adaptTasks(context.tasks.whose(filter));
    }
}

function getRemainingTasksFromContext(id, filter) {
    var context = _getContext({id : id})[0];
    if(typeof(filter)==='undefined') {
        return adaptTasks(context.remainingTasks());
    }
    else {
        return adaptTasks(context.remainingTasks.whose(filter));
    }
}

function getAvailableTasksFromContext(id, filter) {
    var context = _getContext({id : id})[0];
    if(typeof(filter)==='undefined') {
        return adaptTasks(context.availableTasks());
    }
    else {
        return adaptTasks(context.availableTasks.whose(filter));
    }
}

function getAllTasksFromProject(id, filter) {
    var project = _getProject({id : id})[0];
    var rootTask = project.rootTask();
    if(typeof(filter)==='undefined') {
        return adaptTasks(rootTask.flattenedTasks());
    }
    else {
        return adaptTasks(rootTask.flattenedTasks.whose(filter));
    }
}

function getNextTaskFromProject(id) {
    var project = _getProject({id : id})[0];
    var nextTask = project.nextTask();
    if (nextTask == null) {
        return null;
    }
    else {
        return adaptTask (nextTask);
    }
}

function getAllTasksFromInbox (filter) {
    var tasks = doc.inboxTasks();
    if(typeof(filter)==='undefined') {
        return adaptTasks(tasks);
    }
    else {
        return adaptTasks(tasks.whose(filer));
    }
}

/*

function completedTodayFilter (tasks) {
    var thisMorning = new Date();
    thisMorning.setHours(0);
    return {
        _and: [
              {completed: true},
              {completionDate: { '>=' : thisMorning}}
        ]};
}

function getCompletedTodayTasksFromContext(name) {
    var context = _getContext(name);
    return adaptTasks(context.tasks.whose(completedTodayFilter ()));
}
function createInboxItem(name) {
    var inbox = app.defaultDocument.inboxTasks;
    var task = app.InboxTask({name : name});
    inbox.push(task);
}

function bringToForeground() {
    app.activate();
}

function log(message) {
    var date = new Date();
    console.log(date + ': ' + message);
}

function logInboxTaskNames() {
    var tasks = app.defaultDocument.inboxTasks();
    for (i in tasks) {
        var task = tasks[i];
        log(task.name());
    }
}

function allTasks() {
    return doc.flattenedTasks.whose({completed: false})();
}
*/