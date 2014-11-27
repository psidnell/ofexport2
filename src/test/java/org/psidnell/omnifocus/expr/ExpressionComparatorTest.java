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
import java.util.Date;

import org.junit.Test;
import org.psidnell.omnifocus.model.Task;

public class ExpressionComparatorTest {

    @Test
    public void testWithNullFields () {
        Task t1 = new Task ();
        Task t2 = new Task ();
        
        assertEquals (0, new ExpressionComparator<>("completionDate", Task.class).compare(t1, t2));
    }
    
    @Test
    public void testWithIdenticalNonNullFields () throws ParseException {
        Date date = new Date ();
        Task t1 = new Task ();
        t1.setCompletionDate(date);
        Task t2 = new Task ();
        t2.setCompletionDate(date);
        
        assertEquals (0, new ExpressionComparator<>("completionDate", Task.class).compare(t1, t2));
    }
    
    @Test
    public void testWithMixedNullFields () throws ParseException {
        Date date = new Date ();
        Task t1 = new Task ();
        t1.setCompletionDate(date);
        Task t2 = new Task ();
        
        assertEquals (-1, new ExpressionComparator<>("completionDate", Task.class).compare(t1, t2));
        
        t1.setCompletionDate(null);
        t2.setCompletionDate(date);
        
        assertEquals (1, new ExpressionComparator<>("completionDate", Task.class).compare(t1, t2));
    }
    
    @Test
    public void testWithNonIdenticalNonNullFields () throws ParseException {
        Task t1 = new Task ();
        t1.setCompletionDate(new Date ());
        Task t2 = new Task ();
        t2.setCompletionDate(new Date (0)); // Older
        
        assertEquals (1, new ExpressionComparator<>("completionDate", Task.class).compare(t1, t2));
    }
    
    @Test
    public void testReverseSort () throws ParseException {
        Task t1 = new Task ();
        t1.setCompletionDate(new Date ());
        Task t2 = new Task ();
        t2.setCompletionDate(new Date (0)); // Older
        
        assertEquals (-1, new ExpressionComparator<>("r:completionDate", Task.class).compare(t1, t2));
    }
}
