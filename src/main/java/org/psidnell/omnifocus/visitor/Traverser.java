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

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Document;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class Traverser {
    
    public static void traverse(Visitor visitor, Node node, boolean filter) {
        try {
            doTraverse(visitor, node, filter);
        } catch (Exception e) {
            throw new TraversalException(e);
        }
    }
    
    public static void doTraverse(Visitor visitor, Node node, boolean filter) throws Exception {
        switch (node.getType()) {
            case Document.TYPE:
                doTraverse(visitor, (Document) node, filter);
                break;
            case Folder.TYPE:
                doTraverse(visitor, (Folder) node, filter);
                break;
            case Group.TYPE:
                doTraverse(visitor, (Group) node, filter);
                break;
            case Project.TYPE:
                doTraverse(visitor, (Project) node, filter);
                break;
            case Context.TYPE:
                doTraverse(visitor, (Context) node, filter);
                break;
            case Task.TYPE:
                doTraverse(visitor, (Task) node, filter);
                break;
        }
    }

    private static void doTraverse(Visitor visitor, Document node, boolean filter) throws Exception {
        visitor.enter (node);
        if (filter) {
            node.setFolders(visitor.filterFolders(node.getFolders()));
        }
        for (Folder child : node.getFolders()) {
            doTraverse (visitor, child, filter);
        }
        if (filter) {
            node.setProjects(visitor.filterProjects(node.getProjects()));
        }
        for (Project child : node.getProjects()) {
            doTraverse (visitor, child, filter);
        }
        if (filter) {
            node.setContexts(visitor.filterContexts(node.getContexts()));
        }
        for (Context child : node.getContexts()) {
            doTraverse (visitor, child, filter);
        }
        if (filter) {
            node.setTasks(visitor.filterTasks(node.getTasks()));
        }
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child, filter);
        }
        
        visitor.exit (node);
    }
    
    private static void doTraverse(Visitor visitor, Folder node, boolean filter) throws Exception {
        visitor.enter (node);
        if (filter){
            node.setFolders(visitor.filterFolders(node.getFolders()));
        }
        for (Folder child : node.getFolders()) {
            doTraverse (visitor, child, filter);
        }
        if (filter){
            node.setProjects(visitor.filterProjects(node.getProjects()));
        }
        for (Project child : node.getProjects()) {
            doTraverse (visitor, child, filter);
        }
        visitor.exit (node);
    }

    private static void doTraverse(Visitor visitor, Task node, boolean filter) throws Exception {
        visitor.enter (node);
        if (filter) {
            node.setTasks(visitor.filterTasks(node.getTasks()));
        }
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child, filter);
        }
        visitor.exit (node);
    }

    private static void doTraverse(Visitor visitor, Context node, boolean filter) throws Exception {
        visitor.enter (node);
        if (filter) {
            node.setTasks(visitor.filterTasks(node.getTasks()));
        }
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child, filter);
        }
        if (filter) {
            node.setContexts(visitor.filterContexts(node.getContexts()));
        }
        for (Context child : node.getContexts()) {
            doTraverse (visitor, child, filter);
        }
        visitor.exit(node);
    }

    private static void doTraverse(Visitor visitor, Project node, boolean filter) throws Exception {
        visitor.enter (node);
        if (filter) {
            node.setTasks(visitor.filterTasks(node.getTasks()));
        }
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child, filter);
        }
        visitor.exit(node);
    }

    private static void doTraverse(Visitor visitor, Group node, boolean filter) throws Exception {
        visitor.enter(node);
        if (filter) {
            node.setChildren(visitor.filterChildren(node.getChildren()));
        }
        for (Node child : node.getChildren()) {
            traverse (visitor, child, filter);
        }
        visitor.exit(node);
    }
}
