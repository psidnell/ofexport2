package org.psidnell.omnifocus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.osascript.OSAScriptEngineFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OmniFocus {

    private static final String LIBRARY_RESOURCE = "/org/psidnell/omnifocus/OmniFocus.js";

    private static final String EOL = System.getProperty("line.separator");

    private final String library;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HashMap<String, Task> taskCache = new HashMap<>();
    private HashMap<String, Project> projectCache = new HashMap<>();
    private HashMap<String, Context> contextCache = new HashMap<>();
    private HashMap<String, Folder> folderCache = new HashMap<>();

    public OmniFocus() throws IOException {
        try (Reader src = new InputStreamReader(OmniFocus.class.getResourceAsStream(LIBRARY_RESOURCE))) {
            library = load(src);
        }
    }

    protected String execute(String script) throws IOException, ScriptException {
        StringBuilder combinedScript = new StringBuilder();
        combinedScript.append(library);
        combinedScript.append(script);

        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngineFactory factory = new OSAScriptEngineFactory(OSAScriptEngineFactory.JS);
        engineManager.registerEngineName(factory.getEngineName(), factory);
        ScriptEngine engine = engineManager.getEngineByName(factory.getEngineName());
        String json = (String) engine.eval(combinedScript.toString());
        return json;
    }

    public List<Folder> getFoldersByName(String folderName) throws IOException, ScriptException {
        String filter = "{name : " + constant(folderName) + "}";
        return getFolders(filter);
    }
    
    public List<Folder> getFolders(String filter) throws IOException, ScriptException {
        String json = execute("console.log(JSON.stringify(getFolders (" + nullToEmpty(filter) + ")));");
        return asFolders(json);
    }
    
    public List<Project> getProjectsByName(String projectName) throws IOException, ScriptException {
        String filter = "{name : " + constant(projectName) + "}";
        return getProjects(filter);
    }

    public List<Project> getProjects(String filter) throws IOException, ScriptException {
        String json = execute("console.log(JSON.stringify(getProjects (" + nullToEmpty(filter) + ")));");
        return asProjects(json);
    }

    public List<Context> getContextsByName(String contextName) throws IOException, ScriptException {
        String filter = "{name : " + constant(contextName) + "}";
        return getContexts(filter);
    }

    private List<Context> getContexts(String filter) throws IOException, ScriptException {
        String json = execute("console.log(JSON.stringify(getContexts (" + nullToEmpty(filter) + ")));");
        return asContexts(json);
    }
    
    public List<Task> loadAllInboxTasks(String filter) throws IOException, ScriptException {
        // TODO load into group to make more like other loadXXX methods?
        String json = execute("console.log(JSON.stringify(getAllTasksFromInbox (" + nullToEmpty(filter) + ")));");
        List<Task> tasks = asTasks(json);
        return tasks;
    }
    
    public void loadAllProjects(Folder folder, String filter) throws IOException, ScriptException {
        String id = constant(folder.getId());
        String json = execute("console.log(JSON.stringify(getAllProjectsFromFolder (" + id + trailingOptArg(filter) + ")));");
        List<Project> projects = asProjects(json);
        folder.setProjects (projects);
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
}
