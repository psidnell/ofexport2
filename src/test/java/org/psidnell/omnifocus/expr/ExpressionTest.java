/*
 * Copyright 2015 Paul Sidnell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.psidnell.omnifocus.expr;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ognl.OgnlException;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.psidnell.omnifocus.expr.Expression;
import org.psidnell.omnifocus.model.NodeFactory;
import org.psidnell.omnifocus.model.Task;
import org.springframework.context.ApplicationContext;

public class ExpressionTest {

    public static final long DAY = 1000 * 60 * 60 * 24;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private NodeFactory nodeFactory;

    @Before
    public void setup () {
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        nodeFactory = appContext.getBean("nodefactory", NodeFactory.class);
    }

    @Test
    public void testBasicEvaluation() throws OgnlException {

        Task t = nodeFactory.createTask("t");
        t.setName("foo");
        t.setCompletionDate(new Date ());

        assertEquals ("foo", new Expression("name").eval(t, String.class));
        assertFalse (new Expression("name=='bar'").eval(t, Boolean.class));
        assertTrue (new Expression("name=='foo'").eval(t, Boolean.class));
        assertTrue (new Expression("completed").eval(t, Boolean.class));
    }

    @Test
    public void testArrayAccess() throws OgnlException {

        Task t = nodeFactory.createTask("t");
        t.setName("foo");

        Task t2 = nodeFactory.createTask("t");
        t2.setName("bar");
        t.add(t2);

        assertTrue(new Expression("name=='foo' && type=='Task' && tasks[0].name=='bar'").eval(t, Boolean.class));
    }

    @Test
    public void testRegularExpressions () {
        Task t = nodeFactory.createTask("t");
        t.setName("fooXXX");

        assertTrue(new Expression("name.matches('foo.*')").eval(t, Boolean.class));
        assertFalse(new Expression("name.matches('bar.*')").eval(t, Boolean.class));
    }

    @Test
    public void testDateExpressions () throws ParseException {
        Task t = nodeFactory.createTask("t");
        t.setCompletionDate(new Date ());
        t.setDueDate(new Date ());

        assertFalse (new Expression("completion.is('yesterday')").eval(t, Boolean.class));
        assertTrue (new Expression("completion.is('today')").eval(t, Boolean.class));
        assertFalse (new Expression("completion.is('tomorrow')").eval(t, Boolean.class));

        assertTrue (new Expression("due.onOrAfter('yesterday')").eval(t, Boolean.class));
        assertTrue (new Expression("due.onOrAfter('today')").eval(t, Boolean.class));
        assertFalse (new Expression("due.onOrAfter('tomorrow')").eval(t, Boolean.class));

        assertTrue (new Expression("due.after('yesterday')").eval(t, Boolean.class));
        assertFalse (new Expression("due.after('today')").eval(t, Boolean.class));
        assertFalse (new Expression("due.after('tomorrow')").eval(t, Boolean.class));

        assertFalse (new Expression("due.onOrBefore('yesterday')").eval(t, Boolean.class));
        assertTrue (new Expression("due.onOrBefore('today')").eval(t, Boolean.class));
        assertTrue (new Expression("due.onOrBefore('tomorrow')").eval(t, Boolean.class));


        assertFalse (new Expression("due.before('yesterday')").eval(t, Boolean.class));
        assertFalse (new Expression("due.before('today')").eval(t, Boolean.class));
        assertTrue (new Expression("due.before('tomorrow')").eval(t, Boolean.class));

        assertFalse (new Expression("due.between('-2d', 'yesterday')").eval(t, Boolean.class));
        assertTrue (new Expression("due.between('today', 'today')").eval(t, Boolean.class));
        assertTrue (new Expression("due.between('yesterday', 'tomorrow')").eval(t, Boolean.class));
        assertFalse (new Expression("due.between('tomorrow', '2d')").eval(t, Boolean.class));
    }

    @Test
    public void testQuotes() throws OgnlException {

        Task t = nodeFactory.createTask("t");
        t.setName("foo");
        t.setCompletionDate(new Date ());

        assertEquals ("foo", new Expression("name").eval(t, String.class));
        assertTrue (new Expression("name=='foo'").eval(t, Boolean.class));
        assertTrue (new Expression("name==\"foo\"").eval(t, Boolean.class));
    }

    @Test
    public void testNumberFormatExceptionBug() throws OgnlException {

        Task t = nodeFactory.createTask("t");
        t.setName("foo");
        t.setCompletionDate(new Date ());

        try {
            // Interpreting as a char/int?
            new Expression("name=='x'").eval(t, Boolean.class);
            fail ();
        }
        catch (NumberFormatException e) {

        }
        assertFalse (new Expression("name=='xx'").eval(t, Boolean.class));
        assertFalse (new Expression("name==\"x\"").eval(t, Boolean.class));

        // Lesson: use double quotes
    }
}
