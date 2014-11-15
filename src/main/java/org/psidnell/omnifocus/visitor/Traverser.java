package org.psidnell.omnifocus.visitor;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class Traverser {
    
    public static void traverse(Visitor visitor, Node node) {
        try {
            switch (node.getType()) {
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

    private static void doTraverse(Visitor visitor, Folder node) throws Exception {
        visitor.enter (node);
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
