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

/**
 * @author psidnell
 *
 *         Filter out all nodes who's include flag is false.
 *
 */
public class PruningFilter implements Visitor {

    private static final VisitorDescriptor WHAT = new VisitorDescriptor().visit(Folder.class, Project.class, Context.class).filter(Folder.class,
            Project.class, Context.class);

    @Override
    public VisitorDescriptor getWhat() {
        return WHAT;
    }

    @Override
    public boolean includeUp(Context c) {
        return c.getTaskCount() != 0 || c.getContextCount() != 0;
    }

    @Override
    public boolean includeUp(Folder f) {
        return f.getFolderCount() != 0 || f.getProjectCount() != 0;
    }

    @Override
    public boolean includeUp(Project p) {
        return p.getTaskCount() != 0;
    }

    @Override
    public String toString() {
        return "PruningFilter";
    }
}
