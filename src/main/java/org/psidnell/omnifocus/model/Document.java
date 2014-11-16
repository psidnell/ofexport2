package org.psidnell.omnifocus.model;

import java.util.List;

import org.psidnell.omnifocus.osa.OSACollection;
import org.psidnell.omnifocus.osa.OSAIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Document extends Node {

    public static final String TYPE = "Document";
    private List<Project> projects;
    private List<Folder> folders;
    private List<Context> contexts;
    private List<Task> tasks;
    
    public Document () {
        name = "Document";
    }
    
    @OSACollection(type=Project.class)
    public List<Project> getProjects () {
        return projects;
    }
    
    public void setProjects (List<Project> projects) {
        this.projects = projects;
    }
    
    @OSACollection(type=Folder.class)
    public List<Folder> getFolders () {
        return folders;
    }
    
    public void setFolders (List<Folder> folders) {
        this.folders = folders;
    }
    
    @OSACollection(type=Context.class)
    public List<Context> getContexts () {
        return contexts;
    }
    
    public void setContexts (List<Context> contexts) {
        this.contexts = contexts;
    }
    
    @OSACollection(type=Task.class)
    public List<Task> getTasks () {
        return tasks;
    }
    
    public void setTasks (List<Task> tasks) {
        this.tasks = tasks;
    }
    
    @Override
    @JsonIgnore
    @OSAIgnore
    public String getType() {
        return TYPE;
    }

}
