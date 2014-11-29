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
package org.psidnell.omnifocus.visitor;

import java.util.List;

import org.psidnell.omnifocus.expr.ExpressionComparator;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

/**
 * @author psidnell
 * 
 * Sort all the nodes in the tree.
 * 
 * Any number of comparators can be added by node type.
 * 
 */
public class SortingFilter implements Visitor {

    private static final VisitorDescriptor WHAT = new VisitorDescriptor().visitAll().filterAll();

    private CompoundComparator<Project> projectComparator = new CompoundComparator<>();
    private CompoundComparator<Context> contextComparator = new CompoundComparator<>();
    private CompoundComparator<Folder> folderComparator = new CompoundComparator<>();
    private CompoundComparator<Task> taskComparator = new CompoundComparator<>();

    @Override
    public VisitorDescriptor getWhat() {
        return WHAT;
    }

    public void addProjectComparator(ExpressionComparator<Project> cmp) {
        projectComparator.add(cmp);
    }

    public void addContextComparator(ExpressionComparator<Context> cmp) {
        contextComparator.add(cmp);
    }

    public void addFolderComparator(ExpressionComparator<Folder> cmp) {
        folderComparator.add(cmp);
    }

    public void addTaskComparator(ExpressionComparator<Task> cmp) {
        taskComparator.add(cmp);
    }

    @Override
    public List<Context> filterContextsDown(List<Context> contexts) {
        contexts.sort(contextComparator);
        return contexts;
    }

    @Override
    public List<Folder> filterFoldersDown(List<Folder> folders) {
        folders.sort(folderComparator);
        return folders;
    }

    @Override
    public List<Task> filterTasksDown(List<Task> tasks) {
        tasks.sort(taskComparator);
        return tasks;
    }

    @Override
    public List<Project> filterProjectsDown(List<Project> projects) {
        projects.sort(projectComparator);
        return projects;
    }
}
