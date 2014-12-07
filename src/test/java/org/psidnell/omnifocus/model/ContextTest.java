package org.psidnell.omnifocus.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

public class ContextTest {

    @Test
    public void testAddContext () {
        Context parent = new Context ();
        Context child = new Context ();

        parent.add(child);
        assertEquals (1, parent.getContexts().size());
        assertTrue (parent.getContexts().contains(child));
        assertSame (parent, child.getContextModeParent());
    }

    @Test
    public void testAddContextDisconnectsFromPrevious () {
        Context parent1 = new Context ();
        Context child = new Context ();

        parent1.add(child);
        assertEquals (1, parent1.getContexts().size());
        assertTrue (parent1.getContexts().contains(child));
        assertSame (parent1, child.getContextModeParent());

        Context parent2 = new Context ();
        parent2.add(child);
        assertEquals (1, parent2.getContexts().size());
        assertTrue (parent2.getContexts().contains(child));
        assertSame (parent2, child.getContextModeParent());

        assertTrue (parent1.getContexts().isEmpty());
    }

    @Test
    public void testAddTask () {
        Context parent = new Context ();
        Task child = new Task ();

        parent.add(child);
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));
        assertSame (parent, child.getContextModeParent());
    }

    @Test
    public void testAddTaskDisconnectsFromPrevious () {
        Context parent1 = new Context ();
        Task child = new Task ();

        parent1.add(child);
        assertEquals (1, parent1.getTasks().size());
        assertTrue (parent1.getTasks().contains(child));
        assertSame (parent1, child.getContextModeParent());

        Context parent2 = new Context ();
        parent2.add(child);
        assertEquals (1, parent2.getTasks().size());
        assertTrue (parent2.getTasks().contains(child));
        assertSame (parent2, child.getContextModeParent());

        assertTrue (parent1.getTasks().isEmpty());
    }

    @Test
    public void testActive () {
        Context c = new Context ("c");

        assertTrue (c.isActive());
        c.setActiveFlag(false);
        assertFalse(c.isActive());
        c.setActiveFlag(true);

        assertTrue (c.isActive());
        c.setAllowsNextAction(false);
        assertFalse(c.isActive());
    }

    @Test
    public void testOnHold () {
        Context c = new Context ("c");

        assertFalse (c.isOnHold());
        c.setAllowsNextAction(false);
        assertTrue(c.isOnHold());
    }

    @Test
    public void testDropped () {
        Context c = new Context ("c");

        assertFalse (c.isDropped());
        c.setActiveFlag(false);
        assertTrue(c.isDropped());
    }

    @Test
    public void testDroppedInheritedFromParentContext () {
        Context parent = new Context ("parent");

        Context child = new Context ("child");
        parent.add(child);

        assertFalse (child.isDropped());
        parent.setActiveFlag(false);
        assertTrue(child.isDropped());
    }
}