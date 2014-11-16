package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

import org.psidnell.omnifocus.osa.OSACollection;
import org.psidnell.omnifocus.osa.OSAIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Folder extends Node {

    public static final String TYPE = "Folder";

    private List<Project> projects = new LinkedList<>();

    private List<Folder> folders;

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @OSACollection(type=Project.class)
    public List<Project> getProjects() {
        return projects;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    @OSACollection(type=Folder.class)
    public List<Folder> getFolders() {
        return folders;
    }
    
    @Override
    @JsonIgnore
    @OSAIgnore
    public String getType() {
        return TYPE;
    }
}
