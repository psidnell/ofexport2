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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author psidnell
 *
 * Represents an OmniFocus project.
 *
 * Note that projects aren't directly represented in the database.
 *
 * The database has a ProjectInfo and a root task, the union of
 * which make up a Project.
 */
public class Project extends CommonProjectAndTaskAttributes {

    public static final String TYPE = "Project";
    private Folder folder;
    private String status;

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public Project(ProjectInfo projInfo, Task rootTask) {
        setId(rootTask.getId());
        setName(rootTask.getName());
        setCompletionDate(rootTask.getCompletionDate());
        setDeferDate(rootTask.getDeferDate());
        setDueDate(rootTask.getDueDate());
        setFlagged(rootTask.isFlagged());
        setNote(rootTask.getNote());
        setRank(rootTask.getRank());
        setSequential(rootTask.isSequential());
        status = projInfo.getStatus();
        for (Task childOfRootTask : new LinkedList<>(rootTask.getTasks())) {
            add(childOfRootTask);
        }
    }

    @Override
    @JsonIgnore
    @ExprAttribute(help = "the items type")
    public String getType() {
        return TYPE;
    }

    @Override
    @JsonIgnore
    public List<Node> getProjectPath() {
        return getProjectPath(folder);
    }

    @JsonIgnore
    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @ExprAttribute(help = "the items status: active, inactive, done")
    public String getStatus() {
        return status;
    }

    public void add(Task child) {
        tasks.add(child);
        Project oldProject = child.getProject();
        Task oldParent = child.getParent();
        child.setParent(null);
        child.setProject(this);
        if (oldProject != null) {
            oldProject.getTasks().remove(child);
        }
        if (oldParent != null) {
            oldParent.getTasks().remove(child);
        }
    }

    @Override
    @ExprAttribute(help = "item is available.")
    public boolean isAvailable() {
        return !isCompleted() && "Active".equals(status);
    }

    @Override
    @ExprAttribute(help = "item is remaining.")
    public boolean isRemaining() {
        return !isCompleted() && !"Done".equals(getStatus());
    }
}
