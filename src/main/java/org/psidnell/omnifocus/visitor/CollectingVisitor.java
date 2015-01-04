/*
 * Copyright 2015 Paul Sidnell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.psidnell.omnifocus.visitor;

import java.util.HashSet;
import java.util.Set;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

/**
 * @author psidnell
 *
 *         Visits a tree collecting all the enclosed nodes by type
 */
public class CollectingVisitor implements Visitor {

    private final VisitorDescriptor what;

    private HashSet<Project> projects = new HashSet<>();
    private HashSet<Task> tasks = new HashSet<>();
    private HashSet<Folder> folders = new HashSet<>();
    private HashSet<Context> contexts = new HashSet<>();

    public CollectingVisitor(VisitorDescriptor what) {
        this.what = what;
    }

    @Override
    public VisitorDescriptor getWhat() {
        return what;
    }

    @Override
    public void enter(Context node) throws Exception {
        contexts.add(node);
    }

    @Override
    public void enter(Folder node) throws Exception {
        folders.add(node);
    }

    @Override
    public void enter(Project node) throws Exception {
        projects.add(node);
    }

    @Override
    public void enter(Task node) throws Exception {
        tasks.add(node);
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Set<Context> getContexts() {
        return contexts;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
