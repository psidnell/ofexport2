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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author psidnell
 *
 *         Represents an OmniFocus Context.
 *
 */
@JsonPropertyOrder(alphabetic=true)
public class Context extends NodeImpl implements ContextHierarchyNode{

    public static final String TYPE = "Context";

    private List<Task> tasks = new LinkedList<>();

    private List<Context> contexts = new LinkedList<>();

    private String parentContextId;

    private Context parent;

    private boolean active = true;

    private boolean allowsNextAction = true;

    public Context() {
    }

    public Context(String name) {
        this.name = name;
    }

    @ExprAttribute(help = "number of tasks.")
    @JsonIgnore
    public int getTaskCount() {
        return tasks.size();
    }

    @ExprAttribute(help = "the sub tasks.")
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @ExprAttribute(help = "number of contexts.")
    @JsonIgnore
    public int getContextCount() {
        return contexts.size();
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }

    @SQLiteProperty(name = "parent")
    public String getParentContextId() {
        return parentContextId;
    }

    public void setParentContextId(String parentContextId) {
        this.parentContextId = parentContextId;
    }

    @Override
    @JsonIgnore
    @ExprAttribute(help = "the items type: '" + TYPE + "'.")
    public String getType() {
        return TYPE;
    }

    @JsonIgnore
    @Override
    public Context getContextModeParent() {
        return parent;
    }

    @Override
    public void setContextModeParent(Context parent) {
        this.parent = parent;
    }

    @SQLiteProperty(name="active")
    public boolean isActiveFlag() {
        return active;
    }

    public void setActiveFlag(boolean active) {
        this.active = active;
    }

    @ExprAttribute(help = "true if context is active.")
    public boolean isActive() {
        return active && allowsNextAction;
    }

    public void setActive(boolean dummy) {
        // To keep jackson happy
    }

    @ExprAttribute(help = "true if context is on hold.")
    public boolean isOnHold () {
        return active && !allowsNextAction;
    }

    public void setOnHold (boolean dummy) {
        // Keep Jackson happy
    }

    @ExprAttribute(help = "true if context is dropped.")
    public boolean isDropped () {
        // Unlike the other statuses dropped does cascade
        if (!active) {
            return true;
        }

        if (parent != null) {
            return parent.isDropped();
        }

        return false;
    }

    public void setDropped (boolean dummy) {
        // Keep Jackson happy
    }

    @SQLiteProperty
    public boolean getAllowsNextAction() {
        return allowsNextAction;
    }

    public void setAllowsNextAction(boolean allowsNextAction) {
        this.allowsNextAction = allowsNextAction;
    }

    @Override
    @JsonIgnore
    public List<ContextHierarchyNode> getContextPath() {
        return getContextPath(parent);
    }

    public void add(Context child) {
        Context oldParent = child.getContextModeParent();
        if (oldParent != null) {
            oldParent.getContexts().remove(child);
        }

        contexts.add(child);
        child.setContextModeParent(this);

    }

    public void add(Task child) {
        Context oldParent = child.getContextModeParent();
        if (oldParent != null) {
            oldParent.getTasks().remove(child);
        }

        tasks.add(child);
        child.setContextModeParent(this);
    }

    @Override
    public void cascadeMarked() {
        setMarked(true);
        tasks.stream().forEach((t) -> t.setMarked(true));
        contexts.stream().forEach((c) -> c.cascadeMarked());
    }
}
