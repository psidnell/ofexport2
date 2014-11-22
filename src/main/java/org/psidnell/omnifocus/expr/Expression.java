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

public class Expression {

    private static final OgnlContext OGNL_CONTEXT = new OgnlContext();
    private Object expression;
    
    public Expression (String expression) {
        try {
            this.expression = Ognl.parseExpression(expression);
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
    public <T> T eval (Node node, Class<T> clazz) {
        return (T) eval (node);
    }
}
