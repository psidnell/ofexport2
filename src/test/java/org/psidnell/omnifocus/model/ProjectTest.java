package org.psidnell.omnifocus.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.springframework.context.ApplicationContext;

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

    private NodeFactory nodeFactory;

    @Before
    public void setup () {
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        nodeFactory = appContext.getBean("nodefactory", NodeFactory.class);
    }

    @Test
    public void testAddTask () {
        Project parent = nodeFactory.createProject("p");
        Task child = nodeFactory.createTask("t");

        parent.add(child);
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));
        assertSame (parent, child.getProjectModeParent());
    }

    @Test
    public void testAddTaskDisconnectsFromPrevious () {
        Project parent1 = nodeFactory.createProject("p");
        Task child = nodeFactory.createTask("t");

        Task root = nodeFactory.createTask("t");
        root.add(child);

        parent1.add(child);
        assertEquals (1, parent1.getTasks().size());
        assertTrue (parent1.getTasks().contains(child));
        assertSame (parent1, child.getProjectModeParent());

        assertFalse (root.getTasks().contains(child));

        Project parent2 = nodeFactory.createProject("p");
        parent2.add(child);
        assertEquals (1, parent2.getTasks().size());
        assertTrue (parent2.getTasks().contains(child));
        assertSame (parent2, child.getProjectModeParent());

        assertTrue (parent1.getTasks().isEmpty());
    }

    @Test public void testStatusInheritedFromParentFolder () {

        Folder parent = nodeFactory.createFolder("parent");
        Project child = nodeFactory.createProject("child");
        parent.add(child);

        assertEquals ("active", child.getStatus());

        parent.setActive(false);
        assertEquals ("dropped", child.getStatus());
    }

    @Test public void testIsActiveInheritedFromParentFolder () {

        Folder parent = nodeFactory.createFolder("parent");
        Project child = nodeFactory.createProject("child");
        parent.add(child);

        assertTrue(child.isActive());

        parent.setActive(false);
        assertFalse(child.isActive());
    }

    @Test public void testIsDroppedInheritedFromParentFolder () {

        Folder parent = nodeFactory.createFolder("parent");
        Project child = nodeFactory.createProject("child");
        parent.add(child);

        assertFalse(child.isDropped());
        child.setStatus(Project.DROPPED);
        assertTrue(child.isDropped());

        parent.setActive(false);
        assertTrue(child.isDropped());
    }

    @Test public void testIsCompletedInheritedFromParentFolder () {

        Folder parent = nodeFactory.createFolder("parent");
        Project child = nodeFactory.createProject("child");
        parent.add(child);

        assertFalse(child.isCompleted());
        child.setStatus(Project.COMPLETED);
        assertTrue(child.isCompleted());

        parent.setActive(false);
        assertTrue(child.isCompleted());
    }

    @Test public void testOnHoldInheritedFromParentFolder () {

        Folder parent = nodeFactory.createFolder("parent");
        Project child = nodeFactory.createProject("child");
        parent.add(child);

        assertFalse(child.isOnHold());
        child.setStatus(Project.ONHOLD);
        assertTrue(child.isOnHold());

        parent.setActive(false);
        assertFalse(child.isOnHold());
    }
}