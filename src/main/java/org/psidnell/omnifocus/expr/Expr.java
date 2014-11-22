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

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class Expr {

    private static final OgnlContext OGNL_CONTEXT = new OgnlContext();

    public static Object eval(Node node, String expr) {
        try {
            Object expression = Ognl.parseExpression(expr);
            Object value = Ognl.getValue(expression, OGNL_CONTEXT, node);
            return value;
        } catch (OgnlException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T eval (Node node, String expr, Class<T> clazz) {
        return (T) eval (node, expr);
    }
}
