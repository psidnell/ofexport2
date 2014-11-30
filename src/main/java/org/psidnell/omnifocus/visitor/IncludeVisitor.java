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
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

/**
 * @author psidnell
 *
 * Set all nodes include flag to the specified value.
 *
 */
public class IncludeVisitor implements Visitor {

    private static final VisitorDescriptor WHAT = new VisitorDescriptor().visitAll();

    private boolean include;

    @Override
    public VisitorDescriptor getWhat() {
        return WHAT;
    }

    public IncludeVisitor(boolean include) {
        this.include = include;
    }

    @Override
    public void enter(Context node) throws Exception {
        node.setIncluded(include);
    }

    @Override
    public void enter(Folder node) throws Exception {
        node.setIncluded(include);
    }

    @Override
    public void enter(Project node) throws Exception {
        node.setIncluded(include);
    }

    @Override
    public void enter(Task node) throws Exception {
        node.setIncluded(include);
    }
}
