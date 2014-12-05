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
package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

import org.psidnell.omnifocus.expr.ExprAttribute;
import org.psidnell.omnifocus.sqlite.SQLiteProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author psidnell
 *
 * Represents an OmniFocus Folder
 */
public class Folder extends Node {

    public static final String TYPE = "Folder";

    private List<Project> projects = new LinkedList<>();

    private List<Folder> folders = new LinkedList<>();

    private String parentFolderId;

    private Folder parent;

    private boolean active = true;

    public Folder() {
    }

    public Folder(String name) {
        this.name = name;
    }

    @ExprAttribute(help = "number of sub projects.")
    @JsonIgnore
    public int getProjectCount() {
        return projects.size();
    }

    @ExprAttribute(help = "the sub projects")
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @ExprAttribute(help = "number of sub folders.")
    @JsonIgnore
    public int getFolderCount() {
        return folders.size();
    }

    @ExprAttribute(help = "the sub folders.")
    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    @SQLiteProperty(name = "parent")
    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    @Override
    @JsonIgnore
    @ExprAttribute(help = "the items type: '" + TYPE + "'.")
    public String getType() {
        return TYPE;
    }

    @JsonIgnore
    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    @Override
    @JsonIgnore
    public List<Node> getProjectPath() {
        return getProjectPath(parent);
    }

    @Override
    @JsonIgnore
    public List<Node> getContextPath() {
        throw new UnsupportedOperationException();
    }

    public void add(Project child) {
        Folder parent = child.getFolder();
        if (parent != null) {
            parent.getProjects().remove(child);
        }

        projects.add(child);
        child.setFolder(this);
    }

    public void add(Folder child) {
        Folder oldParent = child.getParent();
        if (oldParent != null) {
            oldParent.getFolders().remove(child);
        }

        folders.add(child);
        child.setParent(this);
    }

    @Override
    @ExprAttribute(help="folder is available.")
    public boolean isAvailable() {
        boolean available = active;

        if (available && parent != null) {
            available = available && parent.isAvailable();
        }
        return available;
    }

    public void setAvailable (boolean dummy) {
        // Dummy method to allow Jackson deserialisation
    }

    @SQLiteProperty
    public boolean isActive () {
        return active;
    }

    public void setActive (boolean active) {
        this.active = active;
    }
}
