package org.psidnell.omnifocus.visitor;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public interface Visitor {
    
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

    default void enter(Group node) throws Exception {
    }

    default void exit(Group node) throws Exception {
    }
}
