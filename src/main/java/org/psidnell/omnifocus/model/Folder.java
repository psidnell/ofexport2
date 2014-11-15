package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

public class Folder extends Node {

    public static final String TYPE = "Folder";

    private List<Project> projects = new LinkedList<>();

    private List<Folder> folders;

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<Folder> getFolders() {
        return folders;
    }
    public String getType() {
        return TYPE;
    }
}
