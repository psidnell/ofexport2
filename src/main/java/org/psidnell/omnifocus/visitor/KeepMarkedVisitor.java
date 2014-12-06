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

/**
 * @author psidnell
 *
 * Keep marked items, delete others.
 */
public class KeepMarkedVisitor implements Visitor {

    private static final VisitorDescriptor WHAT = new VisitorDescriptor().visitAll().filterAll();
    private boolean projectMode;
    private boolean cascade;

    public KeepMarkedVisitor (boolean projectMode, boolean cascade) {
        this.projectMode = projectMode;
        this.cascade = cascade;
    }

    @Override
    public VisitorDescriptor getWhat() {
        return WHAT;
    }

    @Override
    public void enter(Context node) throws Exception {
        markPath(node);
    }

    @Override
    public void enter(Folder node) throws Exception {
        markPath(node);
    }

    @Override
    public void enter(Project node) throws Exception {
        markPath(node);
    }

    @Override
    public void enter(Task node) throws Exception {
        markPath(node);
    }

    @Override
    public boolean includeUp(Context c) {
        return c.isMarked();
    }

    @Override
    public boolean includeUp(Folder f) {
        return f.isMarked();
    }

    @Override
    public boolean includeUp(Project p) {
        return p.isMarked();
    }

    @Override
    public boolean includeUp(Task t) {
        return t.isMarked();
    }

    private void markPath (Node node) { // NOPMD it's confused and thinks it's unused
        if (node.isMarked()) {
            if (projectMode) {
                node.getProjectPath().stream().forEach((n)->n.setMarked(true));
            }
            else {
                node.getContextPath().stream().forEach((n)->n.setMarked(true));
            }

            if (cascade) {
                node.cascadeMarked();
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
