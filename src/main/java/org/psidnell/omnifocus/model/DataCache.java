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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.psidnell.omnifocus.sqlite.SQLiteDAO;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        
        folders.stream().forEach((n)->add(n));
        projInfos.stream().forEach((n)->add(n));
        tasks.stream().forEach((n)->add(n));
        contexts.stream().forEach((n)->add(n));
    }
    
    public final void build() {
        Project inbox = new Project();
        inbox.setName("Inbox");
        inbox.setId("__%%Inbox"); // to give deterministic JSON/XML output

        Context noContext = new Context ();
        noContext.setName("No Context");
        noContext.setId("__%%NoContext");  // to give deterministic JSON/XML output
        
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
            
            if (task.isInInbox() && task.getParentTaskId() == null) {
                inbox.add(task);
            }
            
            if (task.getContextId() == null) {
                noContext.add(task);
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

        if (!inbox.getTasks().isEmpty()) {
            projects.put(inbox.getId(), inbox);
        }
        
        if (!noContext.getTasks().isEmpty()) {
            contexts.put(noContext.getId(), noContext);
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
    
    public HashMap<String,ProjectInfo> getProjectInfos() {
        return projInfos;
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

    public static DataCache importData (File file) throws FileNotFoundException, IOException {
        try (Reader in = new FileReader (file)) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, DataCache.class);
        }
    }

    public static void exportData (File file, Predicate<Node> filterFn, SQLiteDAO sqliteDAO) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, SQLException, JsonGenerationException, JsonMappingException, IOException {
        
        try (Connection c = sqliteDAO.getConnection(); Writer out = new FileWriter(file)) {
            
            Collection<Folder> folders = sqliteDAO.load(c, SQLiteDAO.FOLDER_DAO, null)
                    .stream().filter(filterFn).collect(Collectors.toList());
            Collection<Task> tasks = sqliteDAO.load(c, SQLiteDAO.TASK_DAO, null)
                    .stream().filter(filterFn).collect(Collectors.toList());
            Collection<Context> contexts = sqliteDAO.load(c, SQLiteDAO.CONTEXT_DAO, null)
                    .stream().filter(filterFn).collect(Collectors.toList());
    
            // ProjInfos don't have a name, have to use their associated root task names
            Set<String> taskIds = tasks.stream().map((t)->t.getId()).collect(Collectors.toSet());
            
            Collection<ProjectInfo> projInfos = sqliteDAO.load(c, SQLiteDAO.PROJECT_INFO_DAO, null)
                    .stream().filter((pi)->taskIds.contains(pi.getRootTaskId())).collect(Collectors.toList());
    
            DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, dataCache);
        }
    }
}
