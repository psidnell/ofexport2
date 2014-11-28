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

public class Context extends Node {

    public static final String TYPE = "Context";

    private List<Task> tasks = new LinkedList<>();

    private List<Context> contexts = new LinkedList<>();

    private String parentContextId;

    private Context parent;

    private boolean active;

    private boolean allowsNextAction;

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

    @ExprAttribute(help = "the sub tasks")
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
    @ExprAttribute(help = "the items type")
    public String getType() {
        return TYPE;
    }

    @JsonIgnore
    public Context getParent() {
        return parent;
    }

    public void setParent(Context parent) {
        this.parent = parent;
    }

    @SQLiteProperty
    @ExprAttribute(help = "true if context is active")
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @SQLiteProperty
    @ExprAttribute(help = "true if context allows next action")
    public boolean getAllowsNextAction() {
        return allowsNextAction;
    }

    public void setAllowsNextAction(boolean allowsNextAction) {
        this.allowsNextAction = allowsNextAction;
    }

    @Override
    @JsonIgnore
    public List<Node> getProjectPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    @JsonIgnore
    public List<Node> getContextPath() {
        return getContextPath(parent);
    }

    public void add(Context child) {
        contexts.add(child);
        Context oldParent = child.getParent();
        child.setParent(this);
        if (oldParent != null) {
            oldParent.getContexts().remove(child);
        }
    }

    public void add(Task child) {
        tasks.add(child);
        Context oldParent = child.getContext();
        child.setContext(this);
        if (oldParent != null) {
            oldParent.getTasks().remove(child);
        }
    }
}
