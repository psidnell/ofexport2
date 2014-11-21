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

import org.psidnell.omnifocus.model.Node;

public class VisitorDescriptor {
    private boolean visitTasks = false;
    private boolean visitProjects = false;
    private boolean visitContexts = false;
    private boolean filterTasks = false;
    private boolean filterProjects = false;
    private boolean filterContexts = false;
    private boolean visitFolders = false;
    private boolean filterFolders;

    public VisitorDescriptor visitAll () {
        visitTasks = true;
        visitContexts = true;
        visitProjects = true;
        visitFolders = true;
        return this;
    }
    
    public VisitorDescriptor filterAll () {
        filterTasks = true;
        filterContexts = true;
        filterProjects = true;
        filterFolders = true;
        return this;
    }
    
    @SafeVarargs
    public final VisitorDescriptor visit (Class<? extends Node>... classes) {
        for (Class<? extends Node> clazz : classes) {
            switch (clazz.getSimpleName()) {
                case "Folder" :
                    visitFolders = true;
                    break;
                case "Project":
                    visitProjects = true;
                    break;
                case "Task":
                    visitTasks = true;
                    break;
                case "Context":
                    visitContexts = true;
                    break;
            }
        }
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public VisitorDescriptor filter (Class<? extends Node>... classes) {
        for (Class<? extends Node> clazz : classes) {
            switch (clazz.getSimpleName()) {
                case "Folder" :
                    filterFolders = true;
                    break;
                case "Project":
                    filterProjects = true;
                    break;
                case "Task":
                    filterTasks = true;
                    break;
                case "Context":
                    filterContexts = true;
                    break;
            }
        }
        return this;
    }
    
    boolean getFoldersTasks () {
        return visitFolders ;
    }
    
    boolean getVisitTasks () {
        return visitTasks;
    }
    
    boolean getVisitProjects () {
        return visitProjects;
    }
    
    boolean getVisitContexts () {
        return visitContexts;
    }
    
    boolean getVisitFolders () {
        return visitContexts;
    }
 
    boolean getFilterTasks () {
        return filterTasks;
    }
    
    boolean getFilterProjects () {
        return filterProjects;
    }
    
    boolean getFilterContexts () {
        return filterContexts;
    }
    
    boolean getFilterFolders () {
        return filterFolders;
    }
}
