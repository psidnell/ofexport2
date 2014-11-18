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
    
    public static void traverse(Visitor visitor, Node node) {
        try {
            switch (node.getType()) {
                case Document.TYPE:
                    doTraverse(visitor, (Document) node);
                    break;
                case Folder.TYPE:
                    doTraverse(visitor, (Folder) node);
                    break;
                case Group.TYPE:
                    doTraverse(visitor, (Group) node);
                    break;
                case Project.TYPE:
                    doTraverse(visitor, (Project) node);
                    break;
                case Context.TYPE:
                    doTraverse(visitor, (Context) node);
                    break;
                case Task.TYPE:
                    doTraverse(visitor, (Task) node);
                    break;
            }
        }
        catch (Exception e) {
            throw new TraversalException(e);
        }
    }

    private static void doTraverse(Visitor visitor, Document node) throws Exception {
        visitor.enter (node);
        for (Folder child : node.getFolders()) {
            doTraverse (visitor, child);
        }
        for (Project child : node.getProjects()) {
            doTraverse (visitor, child);
        }
        for (Context child : node.getContexts()) {
            doTraverse (visitor, child);
        }
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child);
        }
        
        visitor.exit (node);
    }
    
    private static void doTraverse(Visitor visitor, Folder node) throws Exception {
        visitor.enter (node);
        for (Folder child : node.getFolders()) {
            doTraverse (visitor, child);
        }
        for (Project child : node.getProjects()) {
            doTraverse (visitor, child);
        }
        visitor.exit (node);
    }

    private static void doTraverse(Visitor visitor, Task node) throws Exception {
        visitor.enter (node);
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child);
        }
        visitor.exit (node);
    }

    private static void doTraverse(Visitor visitor, Context node) throws Exception {
        visitor.enter (node);
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child);
        }
        for (Context child : node.getContexts()) {
            doTraverse (visitor, child);
        }
        visitor.exit(node);
    }

    private static void doTraverse(Visitor visitor, Project node) throws Exception {
        visitor.enter (node);
        for (Task child : node.getTasks()) {
            doTraverse (visitor, child);
        }
        visitor.exit(node);
    }

    private static void doTraverse(Visitor visitor, Group node) throws Exception {
        visitor.enter(node);
        for (Node child : node.getChildren()) {
            traverse (visitor, child);
        }
        visitor.exit(node);
    }
}
