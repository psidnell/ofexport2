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

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ognl.OgnlException;

import org.junit.Test;
import org.psidnell.omnifocus.expr.Expression;
import org.psidnell.omnifocus.model.Task;

public class ExpressionTest {
    
    public static final long DAY = 1000 * 60 * 60 * 24;
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    @Test
    public void testBasicEvaluation() throws OgnlException {

        Task t = new Task();
        t.setName("foo");
        Task t2 = new Task();
        t2.setName("bar");
        t.add(t2);

        assertEquals ("foo", new Expression("name").eval(t, String.class));
        assertFalse (new Expression("name=='bar'").eval(t, Boolean.class));
        assertTrue (new Expression("name=='foo'").eval(t, Boolean.class));
    }
    
    @Test
    public void testComplexEvaluation() throws OgnlException {

        Task t = new Task();
        t.setName("foo");
        Task t2 = new Task();
        t2.setName("bar");
        t.add(t2);

        boolean value = new Expression("name=='foo' && type=='Task' && tasks[0].name=='bar'").eval(t, Boolean.class);
        assertTrue (value);
    }
    
    @Test
    public void testRegularExpressions () {
        Task t = new Task();
        t.setName("fooXXX");
        
        assertTrue (t.getName().matches("foo.*"));
        
        boolean value = new Expression("name.matches('foo.*')").eval(t, Boolean.class);
        assertTrue(value);
    }
    
    @Test
    public void testDateExpressions () throws ParseException {
        Task t = new Task();
        Date date = DATE_FORMAT.parse("2014-10-22");
        t.setCompletionDate(date);
        
        boolean value = new Expression("completionDate==date('2014-10-22')").eval(t, Boolean.class);
        assertTrue(value);
        
        value = new Expression("date('2014-10-22')==completionDate").eval(t, Boolean.class);
        assertTrue(value);
        
        t.setCompletionDate(null);
        value = new Expression("completionDate==date('2014-10-22')").eval(t, Boolean.class);
        assertFalse(value);
        
        Date today = new Date (DAY * (System.currentTimeMillis() / DAY));
        t.setCompletionDate(today);
        value = new Expression("completionDate==date('today')").eval(t, Boolean.class);
        assertTrue(value);
    }
}
