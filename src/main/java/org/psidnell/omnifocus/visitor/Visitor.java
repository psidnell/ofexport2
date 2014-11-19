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

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Document;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public interface Visitor {
    
    default void enter(Document node) throws Exception {
    }

    default void exit(Document node) throws Exception {
    }
    
    default void enter(Folder node) throws Exception {
    }

    default void exit(Folder node) throws Exception {
    }
    
    default void enter(Task node) throws Exception {
    }

    default void exit(Task node) throws Exception {
    }

    default void enter(Context node) throws Exception {
    }

    default void exit(Context node) throws Exception {
    }

    default void enter(Project node) throws Exception {
    }

    default void exit(Project node) throws Exception {
    }

    default List<Folder> filterFolders(List<Folder> folders) {
        return folders;
    }

    default List<Project> filterProjects(List<Project> projects) {
        return projects;
    }

    default List<Context> filterContexts(List<Context> contexts) {
        return contexts;
    }

    default List<Task> filterTasks(List<Task> tasks) {
        return tasks;
    }

    default List<Node> filterChildren(List<Node> children) {
        return children;
    }
}
