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

public class ProjectTest {

    @Test
    public void testAddTask () {
        Project parent = new Project ();
        Task child = new Task ();

        parent.add(child);
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));
        assertSame (parent, child.getProjectModeParent());
    }

    @Test
    public void testAddTaskDisconnectsFromPrevious () {
        Project parent1 = new Project ();
        Task child = new Task ();

        Task root = new Task ();
        root.add(child);

        parent1.add(child);
        assertEquals (1, parent1.getTasks().size());
        assertTrue (parent1.getTasks().contains(child));
        assertSame (parent1, child.getProjectModeParent());

        assertFalse (root.getTasks().contains(child));

        Project parent2 = new Project ();
        parent2.add(child);
        assertEquals (1, parent2.getTasks().size());
        assertTrue (parent2.getTasks().contains(child));
        assertSame (parent2, child.getProjectModeParent());

        assertTrue (parent1.getTasks().isEmpty());
    }

    @Test public void testStatusInheritedFromParentFolder () {

        Folder parent = new Folder ("parent");
        Project child = new Project ("child");
        parent.add(child);

        assertEquals ("active", child.getStatus());

        parent.setActive(false);
        assertEquals ("dropped", child.getStatus());
    }

    @Test public void testIsActiveInheritedFromParentFolder () {

        Folder parent = new Folder ("parent");
        Project child = new Project ("child");
        parent.add(child);

        assertTrue(child.isActive());

        parent.setActive(false);
        assertFalse(child.isActive());
    }

    @Test public void testIsDroppedInheritedFromParentFolder () {

        Folder parent = new Folder ("parent");
        Project child = new Project ("child");
        parent.add(child);

        assertFalse(child.isDropped());
        child.setStatus(Project.DROPPED);
        assertTrue(child.isDropped());

        parent.setActive(false);
        assertTrue(child.isDropped());
    }

    @Test public void testIsCompletedInheritedFromParentFolder () {

        Folder parent = new Folder ("parent");
        Project child = new Project ("child");
        parent.add(child);

        assertFalse(child.isCompleted());
        child.setStatus(Project.COMPLETED);
        assertTrue(child.isCompleted());

        parent.setActive(false);
        assertTrue(child.isCompleted());
    }

    @Test public void testOnHoldInheritedFromParentFolder () {

        Folder parent = new Folder ("parent");
        Project child = new Project ("child");
        parent.add(child);

        assertFalse(child.isOnHold());
        child.setStatus(Project.ONHOLD);
        assertTrue(child.isOnHold());

        parent.setActive(false);
        assertFalse(child.isOnHold());
    }
}