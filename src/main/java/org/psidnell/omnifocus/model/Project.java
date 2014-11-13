package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

public class Project extends Common {

    public static final String TYPE = "Project";
    
    private List<Task> tasks = new LinkedList<>();

    public void setTasks (List<Task> tasks) {
        this.tasks = tasks;
    }
    
    public List<Task> getTasks() {
        return tasks ;
    }
    
    public String getType () {
        return TYPE;
    }
}
