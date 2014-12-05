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
package org.psidnell.omnifocus.model;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class TaskTest {

    @Test
    public void testAddTask () {
        Task parent = new Task ();
        Task child = new Task ();

        parent.add(child);
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));
        assertSame (parent, child.getParent());
    }

    @Test
    public void testAddTaskDisconnectsFromPrevious () {
        Task parent1 = new Task ();
        Task child = new Task ();

        parent1.add(child);
        assertEquals (1, parent1.getTasks().size());
        assertTrue (parent1.getTasks().contains(child));
        assertSame (parent1, child.getParent());

        Task parent2 = new Task ();
        parent2.add(child);
        assertEquals (1, parent2.getTasks().size());
        assertTrue (parent2.getTasks().contains(child));
        assertSame (parent2, child.getParent());

        assertTrue (parent1.getTasks().isEmpty());
    }

    @Test public void testAvailability () {

        Task t = new Task ("t");
        assertTrue (t.isAvailable());

        t.setBlocked(true);
        assertFalse (t.isAvailable());

        t.setBlocked(false);
        t.setCompletionDate(new Date());
        assertFalse (t.isAvailable());

        t.setBlocked(true);
        t.setCompletionDate(null);
        assertFalse (t.isAvailable());
    }

    @Test public void testAvailabilityInheritedFromParentTask () {

        Task parent = new Task ("parent");
        Task child = new Task ("child");
        parent.add(child);

        assertTrue (child.isAvailable());

        parent.setBlocked(true);
        assertFalse (child.isAvailable());

        parent.setBlocked(false);
        parent.setCompletionDate(new Date());
        assertFalse (child.isAvailable());

        parent.setBlocked(true);
        parent.setCompletionDate(null);
        assertFalse (child.isAvailable());
    }

    @Test public void testAvailabilityInheritedFromParentProject () {

        Project parent = new Project ("parent");
        Task child = new Task ("child");
        parent.add(child);

        assertTrue (child.isAvailable());

        parent.setStatus("inactive");
        assertFalse (child.isAvailable());

        parent.setStatus("active");
        parent.setCompletionDate(new Date());
        assertFalse (child.isAvailable());

        parent.setStatus("inactive");
        parent.setCompletionDate(null);
        assertFalse (child.isAvailable());
    }

    @Test public void testAvailabilityInheritedFromParentContext () {

        Context parent = new Context ("parent");
        Task child = new Task ("child");
        parent.add(child);

        assertTrue (child.isAvailable());

        parent.setAllowsNextAction(false);
        assertFalse (child.isAvailable());
    }
}
