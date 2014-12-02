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

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import org.psidnell.omnifocus.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author psidnell
 *
 *         A minimal adaptor around the OGNL expression parser.
 */
public class Expression {

    private static final Logger LOGGER = LoggerFactory.getLogger(Expression.class);
    private static final OgnlContext OGNL_CONTEXT = new OgnlContext();
    private Object expression;
    private String expressionStr;

    public Expression(String expression) {
        try {
            LOGGER.debug("new Expression(\"{}\")", expression);
            this.expression = Ognl.parseExpression(expression);
            this.expressionStr = expression;
        } catch (OgnlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Object eval(Node node) {
        try {
            return Ognl.getValue(expression, OGNL_CONTEXT, node);
        } catch (OgnlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T eval(Node node, Class<T> clazz) {
        LOGGER.debug("Evaluating {} on {}", expressionStr, node);
        return (T) eval(node);
    }
}
