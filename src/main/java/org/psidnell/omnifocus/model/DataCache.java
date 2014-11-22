/*
Copyright 2014 Paul Sidnell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.psidnell.omnifocus.model;

import java.util.Collection;
import java.util.HashMap;

public class DataCache {
    
    private HashMap<String, Folder> folders = new HashMap<>();
    private HashMap<String, ProjectInfo> projInfos = new HashMap<>();
    private HashMap<String, Task> tasks = new HashMap<>();
    private HashMap<String, Context> contexts = new HashMap<>();
    private HashMap<String, Project> projects = new HashMap<>();

    public DataCache () {
        this.folders = new HashMap<>();
        this.projInfos = new HashMap<>();
    }
    
    public DataCache (
            Collection<Folder> folders,
            Collection<ProjectInfo> projInfos,
            Collection<Task> tasks,
            Collection<Context> contexts) {
               
        for (Folder folder : folders) {
            add(folder);
        }
        
        for (ProjectInfo projInfo : projInfos) {
            add(projInfo);
        }
                
        for (Task task : tasks) {
            add(task);
        }
        
        for (Context context : contexts) {
            add(context);
        }
        
        build();
    }
    
    public final void build() {

        // Build Folder Hierarchy
        for (Folder folder : folders.values()) {
            String parentId = folder.getParentFolderId();
            if (parentId != null) {
                Folder parent = folders.get(parentId);
                parent.add (folder);
            }
        }
        
        // Build Task Hierarchy
        for (Task task : tasks.values()) {
            String parentId = task.getParentTaskId();
            if (parentId != null) {
                Task parent = tasks.get(parentId);
                parent.add(task);
            }
        }
        
        // Build Context Hierarchy
        for (Context context : contexts.values()) {
            String parentId = context.getParentContextId();
            if (parentId != null) {
                Context parent = contexts.get(parentId);
                parent.add (context);
            }
        }
       
        // Add tasks to contexts
        for (Task task : tasks.values()) {
            String contextId = task.getContextId();
            if (contextId != null) {
                Context context = contexts.get(contextId);
                context.getTasks().add(task);
                task.setContext(context);
            }
        }
        
        // Create Projects from their root tasks
        // Must do this after task hierarchy is woven
        // since a copy of the root tasks subtasks is taken
        for (ProjectInfo projInfo : projInfos.values()) {
            Task rootTask = tasks.get(projInfo.getRootTaskId());
            Project project = new Project (projInfo, rootTask);
            
            // Set containing Folder for project
            String folderId = projInfo.getFolderId();
            if (folderId != null) {
                Folder folder = folders.get(folderId);
                folder.add(project);
            }
            
            projects.put (project.getId (), project);
            
            // Discard the root task
            tasks.remove(rootTask.getId());   
        }
    }

    public HashMap<String, Folder> getFolders() {
        return folders;
    }

    public HashMap<String, Task> getTasks() {
        return tasks;
    }

    public HashMap<String, Context> getContexts() {
        return contexts;
    }

    public HashMap<String,Project> getProjects() {
        return projects;
    }
    
    public void add(Context context) {
        this.contexts.put(context.getId(), context);
    }

    public void add(Task task) {
        this.tasks.put(task.getId(), task);
    }

    public void add(ProjectInfo projInfo) {
        this.projInfos.put(projInfo.getRootTaskId(), projInfo);
    }

    public void add(Folder folder) {
        this.folders.put(folder.getId(), folder);
    }
}
