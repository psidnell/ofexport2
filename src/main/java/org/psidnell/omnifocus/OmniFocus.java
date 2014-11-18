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
package org.psidnell.omnifocus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.psidnell.omnifocus.filter.Filter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Document;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.osa.OSAClassDescriptor;
import org.psidnell.osascript.OSAScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OmniFocus {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmniFocus.class);
    
    private static final String HEADER_RESOURCE = "/org/psidnell/omnifocus/OmniFocus.js";

    private static final String EOL = System.getProperty("line.separator");

    private final String library;
    
    private HashMap<String, OSAClassDescriptor> descriptors = new HashMap<>();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HashMap<String, Task> taskCache = new HashMap<>();
    private HashMap<String, Project> projectCache = new HashMap<>();
    private HashMap<String, Context> contextCache = new HashMap<>();
    private HashMap<String, Folder> folderCache = new HashMap<>();

    public OmniFocus() throws IOException, ClassNotFoundException {
        try (Reader src = new InputStreamReader(OmniFocus.class.getResourceAsStream(HEADER_RESOURCE))) {
            // Load in the javascript header functions
            library = load(src);      
        }
        
        // Set up defaults
        descriptors.put("Document", new OSAClassDescriptor(Document.class));
        descriptors.put("Folder", new OSAClassDescriptor(Folder.class));
        descriptors.put("Project", new OSAClassDescriptor(Project.class));
        descriptors.put("Task", new OSAClassDescriptor(Task.class));
        descriptors.put("Context", new OSAClassDescriptor(Context.class));
        
    }
    
    protected String execute(Collection<OSAClassDescriptor> pathExpr) throws IOException, ScriptException, ClassNotFoundException {
         
        StringBuilder buf = new StringBuilder ();

       
        // Override with descriptors in the path expression
        for (OSAClassDescriptor desc : pathExpr) {
            descriptors.put(desc.getObjectName(), desc);
        }
        
        // Create mapping functions
        for (OSAClassDescriptor desc : descriptors.values()) {
            buf.append (desc.createMapperFunctions());
        }
        
        // Add command to translate root document
        buf.append("console.log(JSON.stringify(mapDocument(doc)));");
        
        return execute (buf.toString());
    }
    
    protected String execute(String script) throws IOException, ScriptException {
        
        LOGGER.debug("Executing: {}", script);
        
        StringBuilder combinedScript = new StringBuilder();
        combinedScript.append(library);
        combinedScript.append(script);

        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngineFactory factory = new OSAScriptEngineFactory(OSAScriptEngineFactory.JS);
        engineManager.registerEngineName(factory.getEngineName(), factory);
        ScriptEngine engine = engineManager.getEngineByName(factory.getEngineName());
        String javascript = combinedScript.toString();
        
        LOGGER.debug ("Full javascript: {}", javascript);
        
        String json = (String) engine.eval(javascript);
        
        LOGGER.debug("Response: {}", json);
        
        return json;
    }

    protected static String buildAccessor (String adaptMethod, String getMethod, String baseFilter, String... adaptFilters) {
        StringBuilder accessor = new StringBuilder ();
        accessor.append("console.log(JSON.stringify(");
        
        accessor.append(adaptMethod  + "(applyFilter(doc." + getMethod + "," + baseFilter + ")");
        
        for (String adaptFilter : adaptFilters) {
            accessor.append(",");
            accessor.append(adaptFilter);
        }
        
        accessor.append(")));");
        
        return accessor.toString();
    }
    
    public List<Folder> getFoldersByName(String folderName, String projectFilter, String taskFilter) throws IOException, ScriptException {
        String folderFilter = "{name : " + constant(folderName) + "}";
        return getFolders(folderFilter, projectFilter, taskFilter);
    }
    
    public List<Folder> getFolders(String folderFilter, String projectFilter, String taskFilter) throws IOException, ScriptException {
        String accessor = buildAccessor("adaptFolders", "folders", folderFilter, projectFilter, taskFilter);
        String json = execute (accessor);
        return asFolders(json);
    }
    
    public List<Project> getProjectsByName(String projectName, String taskFilter) throws IOException, ScriptException {
        String projectFilter = "{name : " + constant(projectName) + "}";
        return getProjects(projectFilter, taskFilter);
    }

    public List<Project> getProjects(String projectFilter, String taskFilter) throws IOException, ScriptException {
        String accessor = buildAccessor("adaptProjects", "flattenedProjects", projectFilter, taskFilter);
        String json = execute (accessor);
        return asProjects(json);
    }

    public List<Context> getContextsByName(String contextName, String taskFilter) throws IOException, ScriptException {
        String contextFilter = "{name : " + constant(contextName) + "}";
        return getContexts(contextFilter, taskFilter);
    }

    private List<Context> getContexts(String contextFilter, String taskFilter) throws IOException, ScriptException {
        String accessor = buildAccessor("adaptContexts", "flattenedContexts", contextFilter, taskFilter);
        String json = execute (accessor);
        return asContexts(json);
    }
    
    public List<Task> loadAllInboxTasks(String taskFilter) throws IOException, ScriptException {
        // TODO load into group to make more like other loadXXX methods?
        String accessor = buildAccessor("adaptTasks", "inboxTasks", taskFilter);
        String json = execute (accessor);        
        List<Task> tasks = asTasks(json);
        return tasks;
    }
    
    public void loadAllTasks(Project project, String filter) throws IOException, ScriptException {
        String id = constant(project.getId());
        String json = execute("console.log(JSON.stringify(getAllTasksFromProject (" + id + trailingOptArg(filter) + ")));");
        List<Task> tasks = asTasks(json);
        project.setTasks(tasks);
    }

    public void loadNextTask(Project project, String filter) throws IOException, ScriptException {
        String id = constant(project.getId());
        String json = execute("console.log(JSON.stringify(getNextTaskFromProject (" + id + trailingOptArg(filter) + ")));");
        List<Task> tasks;
        if (json.trim().equals("null")) {
            tasks = new LinkedList<>();
        } else {
            tasks = asTasks("[" + json + "]");
        }
        project.setTasks(tasks);
    }

    public void loadAllTasks(Context context, String filter) throws IOException, ScriptException {
        String id = constant(context.getId());
        String json = execute("console.log(JSON.stringify(getAllTasksFromContext (" + id + trailingOptArg(filter) + ")));");
        List<Task> tasks = asTasks(json);
        context.setTasks(tasks);
    }

    public void loadRemainingTasks(Context context, String filter) throws IOException, ScriptException {
        String id = constant(context.getId());
        String json = execute("console.log(JSON.stringify(getRemainingTasksFromContext (" + id + trailingOptArg(filter)
                + ")));");
        List<Task> tasks = asTasks(json);
        context.setTasks(tasks);
    }

    public void loadAvailableTasks(Context context, String filter) throws IOException, ScriptException {
        String id = constant(context.getId());
        String json = execute("console.log(JSON.stringify(getAvailableTasksFromContext (" + id + trailingOptArg(filter)
                + ")));");
        List<Task> tasks = asTasks(json);
        context.setTasks(tasks);
    }

    public void loadTasks(Context context, Availability availability, String filter) throws IOException,
            ScriptException {
        switch (availability) {
        case All:
            loadAllTasks(context, filter);
            break;
        case Available:
            loadAvailableTasks(context, filter);
            break;
        case Remaining:
            loadRemainingTasks(context, filter);
            break;
        case FirstAvailable:
            loadAvailableTasks(context, Filter.and("{next: true}", filter));
            break;
        case Completed:
            loadAllTasks(context, Filter.and("{completed: true}", filter));
            break;
        }
    }

    public void loadTasks(Project project, Availability availability, String filter) throws IOException,
            ScriptException {
        switch (availability) {
        case All:
            loadAllTasks(project, filter);
            break;
        case Available:
            loadAllTasks(project, Filter.and("{completed: false}", "{blocked:false}", filter));
            break;
        case Remaining:
            loadAllTasks(project, Filter.and("{completed: false}", filter));
            break;
        case FirstAvailable:
            loadAllTasks(project, Filter.and("{next: true}", filter));
            break;
        case Completed:
            loadAllTasks(project, Filter.and("{completed: true}", filter));
            break;
        }
    }

    protected <T extends Node> T cache(T node, Map<String, T> cache) {
        T cachedObject = null;
        if (node != null) {
            cachedObject = (T) cache.get(node.getId());
            if (cachedObject == null) {
                cache.put(node.getId(), node);
                cachedObject = node;
            }
        }
        return cachedObject;
    }

    protected List<Task> asTasks(String json) throws IOException {
        try {
            List<Task> tasks = MAPPER.readValue(json, new TypeReference<List<Task>>() {});
            LinkedList<Task> cachedTasks = new LinkedList<>();
            for (Task task : tasks) {
                cachedTasks.add(cache(task, taskCache));
            }
            return cachedTasks;
        } catch (Exception e) {
            throw new IOException(json, e);
        }
    }

    protected List<Project> asProjects(String json) throws IOException {
        try {
            List<Project> projects = MAPPER.readValue(json, new TypeReference<List<Project>>() {});
            LinkedList<Project> cachedProjects = new LinkedList<>();
            for (Project project : projects) {
                cachedProjects.add(cache(project, projectCache));
            }
            return cachedProjects;
        } catch (Exception e) {
            throw new IOException(json, e);
        }
    }

    protected List<Context> asContexts(String json) throws IOException {
        try {
            List<Context> contexts = MAPPER.readValue(json, new TypeReference<List<Context>>() {});
            LinkedList<Context> cachedContexts = new LinkedList<>();
            for (Context context : contexts) {
                cachedContexts.add(cache(context, contextCache));
            }
            return cachedContexts;
        } catch (Exception e) {
            throw new IOException(json, e);
        }
    }
    
    protected List<Folder> asFolders(String json) throws IOException {
        try {
            List<Folder> contexts = MAPPER.readValue(json, new TypeReference<List<Folder>>() {});
            LinkedList<Folder> cachedFolders = new LinkedList<>();
            for (Folder folder : contexts) {
                cachedFolders.add(cache(folder, folderCache));
            }
            return cachedFolders;
        } catch (Exception e) {
            throw new IOException("jsonBlock=" + json, e);
        }
    }
    
    protected Document asDocument(String json) throws IOException {
        try {
            return MAPPER.readValue(json, Document.class);
        } catch (Exception e) {
            throw new IOException(json, e);
        }
    }

    protected String load(Reader src) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(src)) {
            String line = in.readLine();
            while (line != null) {
                builder.append(line);
                builder.append(EOL);
                line = in.readLine();
            }
        }
        return builder.toString();
    }

    static String constant(String val) throws JsonProcessingException {
        return "\"" + val.replaceAll("\"", "\\\\").replaceAll("\n", "\\\\n") + "\"";
    }

    private String nullToEmpty(String val) {
        return val == null ? "" : val;
    }

    private String trailingOptArg(String val) {
        return val == null ? "" : ", " + val;
    }

    public HashMap<String, OSAClassDescriptor> getDescriptors() {
        return descriptors;
    }
}
