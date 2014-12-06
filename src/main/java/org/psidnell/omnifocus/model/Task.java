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
 *         Represents an OmniFocus task.
 *
 *         Note that the root of task represents the project.
 *
 *         When the model is build a Project is made from the root task which takes ownership of the
 *         tasks children.
 */
public class Task extends CommonProjectAndTaskAttributes {

    public static final String TYPE = "Task";

    private String parentTaskId;
    private String contextId;
    private CommonProjectAndTaskAttributes parent;
    private boolean blocked = false;
    private boolean inInbox;
    private boolean isProject;

    public Task() {
    }

    public Task(String name) {
        this.name = name;
    }

    @SQLiteProperty(name = "inInbox")
    @JsonIgnore
    public boolean isInInbox() {
        return inInbox;
    }

    public void setInInbox(boolean inInbox) {
        this.inInbox = inInbox;
    }

    @SQLiteProperty
    @ExprAttribute(help = "item is blocked.")
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @SQLiteProperty(name = "context")
    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    @SQLiteProperty(name = "parent")
    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentFolderId) {
        this.parentTaskId = parentFolderId;
    }

    @Override
    @JsonIgnore
    @ExprAttribute(help = "the items type: '" + TYPE + "'.")
    public String getType() {
        return TYPE;
    }

    @JsonIgnore
    public CommonProjectAndTaskAttributes getParent() {
        return parent;
    }

    public void setParent(CommonProjectAndTaskAttributes parent) {
        this.parent = parent;
    }

    @Override
    @JsonIgnore
    public List<Node> getProjectPath() {
        if (parent != null) {
            return getProjectPath(parent);
        } else {
            LinkedList<Node> result = new LinkedList<>();
            result.add(this);
            return result;
        }
    }

    public void add(Task child) {
        CommonProjectAndTaskAttributes oldParent = child.getParent();
        if (oldParent != null) {
            oldParent.getTasks().remove(child);
        }

        child.setParent(this);
        tasks.add(child);
    }

    @Override
    @ExprAttribute(help = "item is available.")
    public boolean isAvailable() {

        if (isCompleted() || isBlocked()) {
            return false;
        }

        if (context != null && !context.isAvailable()) {
            return false;
        }

        Project project = getEnclosingProject();

        if (project != null && !project.isAvailable()) {
            return false;
        }

        if (project != null && isProject) {
            // Should only ever be seeing the root tasks from context mode and single action
            // lists don't show up in context mode. All of this tasks subtasks have been moved
            // to the project
            if (project.getUncompletedTaskCount() == 0 && !project.isSingleActionList()) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    private Project getEnclosingProject () {
        CommonProjectAndTaskAttributes node = parent;
        while (node != null && node instanceof Task) {
            node = ((Task) node).getParent();
        }
        return (Project) node;
    }

    @Override
    public void setAvailable(boolean ignored) {
        // Dummy setter for derived value since
        // we want the exported/imported value in the json/xml
    }

    @Override
    @ExprAttribute(help = "item is remaining.")
    public boolean isRemaining() {
        return !isCompleted();
    }

    @Override
    public void setRemaining(boolean ignored) {
        // Dummy setter for derived value since
        // we want the exported/imported value in the json/xml
    }

    @JsonIgnore
    @ExprAttribute(help = "true if task represents a project.")
    public boolean isProjectTask() {
        return isProject;
    }

    public void setIsProjectTask(boolean isProject) {
        this.isProject = isProject;
    }
}
