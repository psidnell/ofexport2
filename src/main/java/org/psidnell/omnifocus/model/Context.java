package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Context extends Node {

    public static final String TYPE = "Context";
    
    private List<Task> tasks = new LinkedList<>();

    public void setTasks (List<Task> tasks) {
        this.tasks = tasks;
    }
    
    @JsonIgnore
    public List<Task> getTasks() {
        return tasks ;
    }
    
    public String getType () {
        return TYPE;
    }
}
