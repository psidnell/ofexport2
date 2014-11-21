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

public class Context extends Node {

    public static final String TYPE = "Context";

    private List<Task> tasks = new LinkedList<>();
    
    private List<Context> contexts = new LinkedList<>();
    
    private String parentContextId;

    private Context parent;
    
    @SQLiteProperty (name="parent")
    public String getParentContextId () {
        return parentContextId;
    }
    
    public void setParentContextId (String parentContextId) {
        this.parentContextId = parentContextId;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    
    public List<Context> getContexts() {
        return contexts;
    }
    
    public void setContexts (List<Context> contexts) {
        this.contexts = contexts;
    }

    @Override
    @JsonIgnore
    public String getType() {
        return TYPE;
    }

    @JsonIgnore
    public Context getParent () {
        return parent;
    }
    public void setParent(Context parent) {
        this.parent = parent;
    }

    @Override
    public List<Node> getProjectPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> getContextPath() {
        return getContextPath(parent);
    }

    public void add(Context child) {
        contexts.add(child);
        child.setParent(this);
    }

    public void add(Task child) {
        tasks.add(child);
        child.setContext(this);
    }
}
