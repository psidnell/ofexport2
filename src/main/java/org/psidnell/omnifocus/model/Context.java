package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

import org.psidnell.omnifocus.osa.OSACollection;
import org.psidnell.omnifocus.osa.OSAIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Context extends Node {

    public static final String TYPE = "Context";

    private List<Task> tasks = new LinkedList<>();

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @OSACollection(type=Task.class)
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    @JsonIgnore
    @OSAIgnore
    public String getType() {
        return TYPE;
    }
}
