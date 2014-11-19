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
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class Traverser {
    
    
    public static void traverse(Visitor visitor, Node node) {
        traverse(visitor, node, false);
    }
    
    public static void filter(Visitor visitor, Node node) throws Exception {
        traverse(visitor, node, true);
    }
    
    private static void traverse(Visitor visitor, Node node, boolean filter) {
        try {
            doTraverse(visitor, node, filter);
        } catch (Exception e) {
            throw new TraversalException(e);
        }
    }
    
    private static void doTraverse(Visitor visitor, Node node, boolean filter) throws Exception {
        switch (node.getType()) {
            case Folder.TYPE:
                doTraverse(visitor, (Folder) node, filter);
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
    
    private static void doTraverse(Visitor visitor, Folder node, boolean filter) throws Exception {
        visitor.enter (node);
        
        if (filter){
            node.setFolders(visitor.filterFoldersDown(node.getFolders()));
        }
        
        if (filter){
            node.setProjects(visitor.filterProjectsDown(node.getProjects()));
        }
        
        for (Folder child : node.getFolders()) {
            doTraverse (visitor, child, filter);
        }
       
        for (Project child : node.getProjects()) {
            doTraverse (visitor, child, filter);
        }
        
        if (filter){
            node.setFolders(visitor.filterFoldersUp(node.getFolders()));
        }
        
        if (filter){
            node.setProjects(visitor.filterProjectsUp(node.getProjects()));
        }
        
        visitor.exit (node);
    }

    private static void doTraverse(Visitor visitor, Task node, boolean filter) throws Exception {
        visitor.enter (node);
       
        if (filter) {
            node.setTasks(visitor.filterTasksDown(node.getTasks()));
        }
        
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child, filter);
        }
        
        if (filter) {
            node.setTasks(visitor.filterTasksUp(node.getTasks()));
        }
        
        visitor.exit (node);
    }

    private static void doTraverse(Visitor visitor, Context node, boolean filter) throws Exception {
        visitor.enter (node);
       
        if (filter) {
            node.setTasks(visitor.filterTasksDown(node.getTasks()));
        }
        
        if (filter) {
            node.setContexts(visitor.filterContextsDown(node.getContexts()));
        }
        
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child, filter);
        }
       
        for (Context child : node.getContexts()) {
            doTraverse (visitor, child, filter);
        }
        
        if (filter) {
            node.setTasks(visitor.filterTasksUp(node.getTasks()));
        }
        
        if (filter) {
            node.setContexts(visitor.filterContextsUp(node.getContexts()));
        }
        visitor.exit(node);
    }

    private static void doTraverse(Visitor visitor, Project node, boolean filter) throws Exception {
        visitor.enter (node);
       
        if (filter) {
            node.setTasks(visitor.filterTasksDown(node.getTasks()));
        }
        
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child, filter);
        }
        
        if (filter) {
            node.setTasks(visitor.filterTasksUp(node.getTasks()));
        }
        
        visitor.exit(node);
    }
}
