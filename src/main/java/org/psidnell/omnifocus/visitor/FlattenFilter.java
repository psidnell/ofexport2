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

import java.util.Set;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class FlattenFilter implements Visitor {

    private static final VisitorDescriptor WHAT = new VisitorDescriptor().visit(Folder.class, Project.class, Context.class);

    @Override
    public VisitorDescriptor getWhat() {
        return WHAT;
    }

    @Override
    public void enter(Folder node) {

        CollectingVisitor collector = new CollectingVisitor(new VisitorDescriptor().visit(Folder.class, Project.class));
        Traverser.traverse(collector, node);

        Set<Project> projects = collector.getProjects ();
        node.getFolders().clear();
        for (Project child : projects) {
            node.add(child);
        }

        node.getFolders().clear();
    }

    @Override
    public void enter(Project node) {

        CollectingVisitor collector = new CollectingVisitor(new VisitorDescriptor().visit(Project.class, Task.class));
        Traverser.traverse(collector, node);

        Set<Task> tasks = collector.getTasks ();
        node.getTasks().clear();
        for (Task child : tasks) {
            node.add(child);
        }
    }

    @Override
    public void enter(Context node) {

        CollectingVisitor collector = new CollectingVisitor(new VisitorDescriptor().visit(Context.class));
        Traverser.traverse(collector, node);

        Set<Context> contexts = collector.getContexts ();
        contexts.remove(node);
        node.getContexts().clear();
        for (Context child : contexts) {
            node.add(child);
        }
    }
}
