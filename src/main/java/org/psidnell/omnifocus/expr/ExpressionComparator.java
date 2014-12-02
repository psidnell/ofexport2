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

import java.util.Comparator;

import org.psidnell.omnifocus.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author psidnell
 *
 * @param <T>
 *
 *            A comparator that uses OGNL expressions to extract the value to be used for comparison.
 */
public class ExpressionComparator<T extends Node> implements Comparator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionComparator.class);

    private final Expression expression;
    private final boolean reverse;

    public ExpressionComparator(String expr, Class<T> clazz) { // NOPMD unused clazz
        reverse = expr.startsWith("r:");
        if (reverse) {
            expr = expr.substring(2);
        }
        expression = new Expression(expr);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public int compare(T o1, T o2) {

        LOGGER.debug("Comparing {} and {} (reverse={})", o1, o2, reverse);

        Object val1 = expression.eval(o1);
        Object val2 = expression.eval(o2);

        int result;

        if (val1 == val2) {
            result = 0;
        } else if (val1 == null && val2 != null) {
            result = 1;
        } else if (val1 != null && val2 == null) {
            result = -1;
        } else {
            // Neither is null
            result = ((Comparable) val1).compareTo(val2);
        }

        if (reverse) {
            result = -result;
        }

        LOGGER.debug("Result is {}", result);

        return result;
    }

}
