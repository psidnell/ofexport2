/*
 * Copyright 2015 Paul Sidnell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.psidnell.omnifocus.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author psidnell
 *
 * A repository for all the data loaded/saved by the application.
 *
 * Also weaves the flat object structure into it's tree form by
 * (for example) using the object references provided by the
 * database and connecting the objects directly.
 *
 *
 */
@JsonPropertyOrder(alphabetic=true)
public class DataCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCache.class);

    private HashMap<String, Folder> folders = new HashMap<>();
    private HashMap<String, ProjectInfo> projInfos = new HashMap<>();
    private HashMap<String, Task> tasks = new HashMap<>();
    private HashMap<String, Context> contexts = new HashMap<>();
    private HashMap<String, Project> projects = new HashMap<>();
    private NodeFactory nodeFactory;
    private RawData rawData;

    public DataCache() {
        this.folders = new HashMap<>();
        this.projInfos = new HashMap<>();
    }

    public void setRawData (RawData rawData) {
        this.rawData = rawData;

        // Add and wire the nodes, they didn't come from spring, must wire them manually
        rawData.getFolders().stream().forEach((n) -> {nodeFactory.initialise(n);add(n);});
        rawData.getProjects().stream().forEach((n) -> {add(n);}); // Not a node
        rawData.getTasks().stream().forEach((n) -> {nodeFactory.initialise(n);add(n);});
        rawData.getContexts().stream().forEach((n) -> {nodeFactory.initialise(n);add(n);});
    }

    public final void build() {
        LOGGER.info("Starting tree reconstruction");

        Project inbox = nodeFactory.createProject("Inbox");
        inbox.setId("__%%Inbox"); // to give deterministic JSON/XML output

        Context noContext = nodeFactory.createContext("Inbox");
        noContext.setName("No Context");
        noContext.setId("__%%NoContext"); // to give deterministic JSON/XML output

        // Build Folder Hierarchy
        for (Folder folder : folders.values()) {
            String parentId = folder.getParentFolderId();
            if (parentId != null) {
                Folder parent = folders.get(parentId);
                parent.add(folder);
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
                parent.add(context);
            }
        }

        // Add tasks to contexts
        for (Task task : tasks.values()) {
            String contextId = task.getContextId();
            if (contextId != null) {
                Context context = contexts.get(contextId);
                context.getTasks().add(task);
                task.setContextModeParent(context);
            }
        }

        // Create Projects from their root tasks
        // Must do this after task hierarchy is woven
        // since a copy of the root tasks subtasks is taken
        for (ProjectInfo projInfo : projInfos.values()) {
            Task rootTask = tasks.get(projInfo.getRootTaskId());
            Project project = nodeFactory.createProject(projInfo, rootTask);

            // Set containing Folder for project
            String folderId = projInfo.getFolderId();
            if (folderId != null) {
                Folder folder = folders.get(folderId);
                folder.add(project);
            }

            projects.put(project.getId(), project);

            // Discard the root task. But note that it'll still be
            // a child in any contexts
            tasks.remove(rootTask.getId());
        }

        if (!inbox.getTasks().isEmpty()) {
            projects.put(inbox.getId(), inbox);
        }

        if (!noContext.getTasks().isEmpty()) {
            contexts.put(noContext.getId(), noContext);
        }

        LOGGER.info("Finished tree reconstruction");

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

    public HashMap<String, Project> getProjects() {
        return projects;
    }

    public HashMap<String, ProjectInfo> getProjectInfos() {
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

    public void exportData(File file, Predicate<NodeImpl> filterFn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            InstantiationException, SQLException, JsonGenerationException, JsonMappingException, IOException {

        try (
            Writer out = new FileWriter(file)) {

            Collection<Folder> folders = rawData.getFolders().stream().filter(filterFn).collect(Collectors.toList());
            Collection<Task> tasks = rawData.getTasks().stream().filter(filterFn).collect(Collectors.toList());
            Collection<Context> contexts = rawData.getContexts().stream().filter(filterFn).collect(Collectors.toList());

            // ProjInfos don't have a name, have to use their associated root task names
            Set<String> taskIds = tasks.stream().map((t) -> t.getId()).collect(Collectors.toSet());

            Collection<ProjectInfo> projInfos = rawData.getProjects().stream().filter((pi) -> taskIds.contains(pi.getRootTaskId()))
                    .collect(Collectors.toList());

            RawData filteredData = new RawData ();
            filteredData.setProjects(new LinkedList<>(projInfos));
            filteredData.setContexts(new LinkedList<>(contexts));
            filteredData.setTasks(new LinkedList<>(tasks));
            filteredData.setFolders(new LinkedList<>(folders));

            RawData.exportRawData(filteredData, file);
        }
    }

    public void setNodeFactory (NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
}
