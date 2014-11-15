package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

public class Context extends Node {

    public static final String TYPE = "Context";

    private List<Task> tasks = new LinkedList<>();

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public String getType() {
        return TYPE;
    }
}
