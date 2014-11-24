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
package org.psidnell.omnifocus.expr;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.Visitor;
import org.psidnell.omnifocus.visitor.VisitorDescriptor;

public class ExprVisitor implements Visitor {

    private VisitorDescriptor visitWhat;
    private VisitorDescriptor applyToWhat;
    private Expression expr;
    private boolean projectMode;

    public ExprVisitor (String expr, boolean projectMode, VisitorDescriptor visitWhat, VisitorDescriptor applyToWhat) {
        this.expr = new Expression (expr);
        this.projectMode = projectMode;
        this.visitWhat = visitWhat;
        this.applyToWhat = applyToWhat;
    }
    @Override
    public VisitorDescriptor getWhat() {
        return visitWhat;
    }
    
    @Override
    public void enter(Context node) throws Exception {
        if (applyToWhat.getVisitContexts ()) {
            evaluate(node);
        }
    }
    
    @Override
    public void enter(Folder node) throws Exception {
        if (applyToWhat.getVisitFolders ()) {
            evaluate(node);
        }
    }
    
    @Override
    public void enter(Project node) throws Exception {
        if (applyToWhat.getVisitProjects ()) {
            evaluate(node);
        }
    }
    
    @Override
    public void enter(Task node) throws Exception {
        if (applyToWhat.getVisitTasks ()) {
            evaluate(node);
        }
    }
    
    private void evaluate(Node node) {
        boolean include = expr.eval(node, Boolean.class);
        if (include) {
            node.include(projectMode);
        }
        else {
            node.exclude();
        }
    }
}
