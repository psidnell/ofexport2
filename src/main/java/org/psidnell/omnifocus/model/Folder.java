package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

public class Folder extends Node {

    public static final String TYPE = "Folder";

    private List<Project> projects = new LinkedList<>();

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public String getType() {
        return TYPE;
    }
}
