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

/**
 * @author psidnell
 *
 * Traverses the node tree applying the OGNL expression to each node type required.
 *
 * Two descriptors are supplied to describe which node types to visit and which node types
 * have the expression applied.
 */
public class ExprVisitor implements Visitor {

    protected VisitorDescriptor visitWhat;
    protected VisitorDescriptor applyToWhat;
    protected Expression expr;

    public ExprVisitor(String expr, VisitorDescriptor visitWhat, VisitorDescriptor applyToWhat) {
        this.expr = new Expression(expr);
        this.visitWhat = visitWhat;
        this.applyToWhat = applyToWhat;
    }

    @Override
    public VisitorDescriptor getWhat() {
        return visitWhat;
    }

    @Override
    public void enter(Context node) throws Exception {
        if (applyToWhat.getVisitContexts()) {
            evaluate(node);
        }
    }

    @Override
    public void enter(Folder node) throws Exception {
        if (applyToWhat.getVisitFolders()) {
            evaluate(node);
        }
    }

    @Override
    public void enter(Project node) throws Exception {
        if (applyToWhat.getVisitProjects()) {
            evaluate(node);
        }
    }

    @Override
    public void enter(Task node) throws Exception {
        if (applyToWhat.getVisitTasks()) {
            evaluate(node);
        }
    }

    protected void evaluate(Node node) {
        expr.eval(node);
    }
}
