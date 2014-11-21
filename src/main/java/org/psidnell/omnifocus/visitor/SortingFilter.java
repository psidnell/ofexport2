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

import java.util.Comparator;
import java.util.List;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class SortingFilter implements Visitor { 
    
    private static final VisitorDescriptor WHAT = new VisitorDescriptor().visitAll();

    private static final NodeComparator CMP = new NodeComparator();
    
    @Override
    public VisitorDescriptor getWhat() {
        return WHAT;
    }
    
    @Override
    public List<Context> filterContextsDown(List<Context> contexts) {
        contexts.sort(CMP);
        return contexts;
    }
    
    @Override
    public List<Folder> filterFoldersDown(List<Folder> folders)  {
        folders.sort(CMP);
        return folders;
    }
    
    @Override
    public List<Task> filterTasksDown(List<Task> tasks) {
        tasks.sort(CMP);
        return tasks;
    }
    
    @Override
    public List<Project> filterProjectsDown(List<Project> projects) {
        projects.sort(CMP);
        return projects;
    }
    
    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getRank() - o2.getRank();
        }
    }
}
