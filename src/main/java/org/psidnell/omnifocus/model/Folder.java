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

import org.psidnell.omnifocus.sqlite.SQLiteProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Folder extends Node {

    public static final String TYPE = "Folder";

    private List<Project> projects = new LinkedList<>();

    private List<Folder> folders = new LinkedList<>();

    private String parentFolderId;

    private Folder parent;
    
    @SQLiteProperty (name="parent")
    public String getParentFolderId () {
        return parentFolderId;
    }
    
    public void setParentFolderId (String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
    
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
    
    @Override
    @JsonIgnore
    public String getType() {
        return TYPE;
    }

    @JsonIgnore
    public Folder getParent () {
        return parent;
    }
    
    public void setParent(Folder parent) {
        this.parent = parent;
    }

    @Override
    public List<Node> getProjectPath() {
        return getProjectPath(parent);
    }

    @Override
    public List<Node> getContextPath() {
        throw new UnsupportedOperationException();
    }

    public void add(Project child) {
        projects.add(child);
        child.setFolder(this);
    }

    public void add(Folder child) {
        folders.add (child);
        child.setParent(this);
    }
}
