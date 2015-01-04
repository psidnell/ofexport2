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

import java.util.Set;

import org.psidnell.omnifocus.ConfigParams;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.NodeFactory;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

/**
 * @author psidnell
 *
 *         Flatten node hierarchies to improve readability.
 */
public class FlattenFilter implements Visitor {

    private static final VisitorDescriptor WHAT = new VisitorDescriptor().visit(Folder.class, Project.class, Context.class);

    private ConfigParams config;
    private NodeFactory nodeFactory;

    public FlattenFilter(NodeFactory nodeFactory, ConfigParams config) {
        this.nodeFactory = nodeFactory;
        this.config = config;
    }

    @Override
    public VisitorDescriptor getWhat() {
        return WHAT;
    }

    @Override
    public void enter(Folder node) {

        if (node.isRoot()) {
            CollectingVisitor collector = new CollectingVisitor(new VisitorDescriptor().visitAll());
            Traverser.traverse(collector, node);

            node.getProjects().clear();
            node.getFolders().clear();
            Set<Task> projects = collector.getTasks();
            Project newParent = nodeFactory.createProject(config.getFlattenedRootName());
            node.add(newParent);
            for (Task child : projects) {
                newParent.add(child);
            }
        }
    }

    @Override
    public void enter(Context node) {

        if (node.isRoot()) {
            CollectingVisitor collector = new CollectingVisitor(new VisitorDescriptor().visitAll());
            Traverser.traverse(collector, node);

            Set<Task> tasks = collector.getTasks();
            node.getContexts().clear();
            node.getTasks().clear();
            Context newParent = nodeFactory.createContext(config.getFlattenedRootName());
            node.add(newParent);
            for (Task child : tasks) {
                newParent.add(child);
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
