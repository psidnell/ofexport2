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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author psidnell
 *
 *         Represents an OmniFocus project.
 *
 *         Note that projects aren't directly represented in the database.
 *
 *         The database has a ProjectInfo and a root task, the union of which make up a Project.
 */
@JsonPropertyOrder(alphabetic = true)
public class Project extends CommonProjectAndTaskAttributes {

    public static final String ONHOLD = "inactive";
    public static final String ACTIVE = "active";
    public static final String COMPLETED = "done";
    public static final String DROPPED = "dropped";
    public static final String TYPE = "Project";
    private String status = ACTIVE;
    private boolean singleActionList;

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
        setEstimatedMinutes(rootTask.getEstimatedMinutes());
        status = projInfo.getStatus();
        singleActionList = projInfo.isSingleActionList();
        for (Task childOfRootTask : new LinkedList<>(rootTask.getTasks())) {
            add(childOfRootTask);
        }
        rootTask.setProjectTask(true);
        rootTask.setProjectModeParent(this);
    }

    @Override
    @JsonIgnore
    @ExprAttribute(help = "the items type: '" + TYPE + "'.")
    public String getType() {
        return TYPE;
    }

    @Override
    @JsonIgnore
    public List<ProjectHierarchyNode> getProjectPath() {
        return getProjectPath(parent);
    }

    @ExprAttribute(help = "the items status: '" + ACTIVE + "', '" + ONHOLD + "', '" + COMPLETED + " or '" + DROPPED + "'.")
    public String getStatus() {
        // OmniFocus doesn't cascade folder state for us
        if (parent != null && !((Folder) parent).isActive()) {
            return DROPPED;
        }

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ExprAttribute(help = "project status is active.")
    public boolean isActive() {
        return ACTIVE.equals(getStatus());
    }

    public void setActive(boolean dummy) {
        // Keep jackson happy
    }

    public void add(Task child) {
        CommonProjectAndTaskAttributes oldParent = (CommonProjectAndTaskAttributes) child.getProjectModeParent();
        if (oldParent != null) {
            oldParent.getTasks().remove(child);
        }

        child.setProjectModeParent(this);
        tasks.add(child);
    }

    @Override
    @ExprAttribute(help = "item is remaining.")
    public boolean isRemaining() {
        return !isCompleted() && !COMPLETED.equals(getStatus());
    }

    @Override
    public void setRemaining(boolean ignored) {
        // Dummy setter for derived value since
        // we want the exported/imported value in the json/xml
    }

    @ExprAttribute(help = "true if it's a sinle action list")
    public boolean isSingleActionList() {
        return singleActionList;
    }

    public void setSingleActionList(boolean singleActionList) {
        this.singleActionList = singleActionList;
    }

    @Override
    @ExprAttribute(help = "item is complete.")
    public boolean isCompleted() {
        if (COMPLETED.equals(getStatus())) {
            return true;
        }

        if (parent != null && ((Folder)parent).isDropped()){
            return true;
        }

        return false;
    }

    public void setCompleted(boolean dummy) {
        // Keep Jackson happy
    }

    @ExprAttribute(help = "project status is on hold.")
    public boolean isOnHold() {
        return ONHOLD.equals(getStatus());
    }

    public void setOnHold(boolean dummy) {
        // Keep Jackson happy
    }

    @ExprAttribute(help = "project is dropped.")
    public boolean isDropped() {
        return DROPPED.equals(getStatus());
    }

    public void setDropped(boolean dummy) {
        // Keep Jackson happy
    }
}
