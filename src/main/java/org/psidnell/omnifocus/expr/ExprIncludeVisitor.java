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

import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.visitor.NodeTraversalAbortException;
import org.psidnell.omnifocus.visitor.VisitorDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author psidnell
 *
 *         Traverses the node tree including only those nodes where the OGNL expression evaluates
 *         true.
 */
public class ExprIncludeVisitor extends ExprVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExprIncludeVisitor.class);

    private boolean projectMode;

    private boolean includeMode = true;

    public ExprIncludeVisitor(String expr, boolean projectMode, VisitorDescriptor visitWhat, VisitorDescriptor applyToWhat) {
        super(stripPlusOrMinus(expr), visitWhat, applyToWhat);
        this.projectMode = projectMode;

        includeMode = !expr.startsWith("-:");
    }

    private static String stripPlusOrMinus(String expr) {
        if (expr.startsWith("+:") || expr.startsWith("-:")) {
            return expr.substring(2);
        }
        return expr;
    }

    @Override
    protected void evaluate(Node node) {
        if (!node.isRoot()) {
            LOGGER.debug("Applying {} to {}", exprString, node);
            boolean result = expr.eval(node, Boolean.class);
            if (includeMode && result) {
                LOGGER.debug("included");
                node.include(projectMode);
                throw new NodeTraversalAbortException();
            } else if (!includeMode && result) {
                LOGGER.debug("excluded");
                node.exclude();
                throw new NodeTraversalAbortException();
            }
        }
    }

    public boolean isIncludeMode() {
        return includeMode;
    }
}
