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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.springframework.context.ApplicationContext;

public class TaskTest {

    private NodeFactory nodeFactory;

    @Before
    public void setup () {
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        nodeFactory = appContext.getBean("nodefactory", NodeFactory.class);
    }

    @Test
    public void testAddTask () {
        Task parent = nodeFactory.createTask("t");
        Task child = nodeFactory.createTask("t");

        parent.add(child);
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));
        assertSame (parent, child.getProjectModeParent());
    }

    @Test
    public void testAddTaskDisconnectsFromPrevious () {
        Task parent1 = nodeFactory.createTask("t");
        Task child = nodeFactory.createTask("t");

        parent1.add(child);
        assertEquals (1, parent1.getTasks().size());
        assertTrue (parent1.getTasks().contains(child));
        assertSame (parent1, child.getProjectModeParent());

        Task parent2 = nodeFactory.createTask("t");
        parent2.add(child);
        assertEquals (1, parent2.getTasks().size());
        assertTrue (parent2.getTasks().contains(child));
        assertSame (parent2, child.getProjectModeParent());

        assertTrue (parent1.getTasks().isEmpty());
    }

    @Test public void testAvailability () {

        Task t = nodeFactory.createTask("t");
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

        Task parent = nodeFactory.createTask("parent");
        Task child = nodeFactory.createTask("child");
        parent.add(child);

        assertTrue (child.isAvailable());

        parent.setBlocked(true);
        assertTrue (child.isAvailable());

        parent.setBlocked(false);
        parent.setCompletionDate(new Date());
        // but completion does cascade
        assertFalse (child.isAvailable());

        parent.setBlocked(true);
        parent.setCompletionDate(null);
        assertTrue (child.isAvailable());
    }

    @Test public void testAvailabilityInheritedFromParentProject () {

        Project parent = nodeFactory.createProject("parent");
        Task child = nodeFactory.createTask("child");
        parent.add(child);

        assertTrue (child.isAvailable());

        parent.setStatus("inactive");
        assertFalse (child.isAvailable());
    }

    @Test public void testAvailabilityInheritedFromParentContext () {

        Context parent = nodeFactory.createContext("parent");
        Task child = nodeFactory.createTask("child");
        parent.add(child);

        assertTrue (child.isAvailable());

        parent.setAllowsNextAction(false);
        assertFalse (child.isAvailable());
    }

    @Test public void testRootTaskAvailabilityAsSequentialProject () {

        Task rootTask = nodeFactory.createTask("root");
        rootTask.setSequential(true);
        ProjectInfo pi = new ProjectInfo();
        pi.setSingleActionList(false);
        pi.setStatus("active");

        Project parent = nodeFactory.createProject(pi, rootTask);

        assertTrue (rootTask.isAvailable());

        Task child = nodeFactory.createTask("x");
        parent.add(child);

        assertFalse (rootTask.isAvailable());

        child.setCompletionDate(new Date ());

        assertTrue (rootTask.isAvailable());
    }

    @Test public void testRootTaskAvailabilityAsSingleActionListProject () {

        Task rootTask = nodeFactory.createTask("root");
        rootTask.setSequential(true);
        ProjectInfo pi = new ProjectInfo();
        pi.setSingleActionList(true);
        pi.setStatus("active");

        Project parent = nodeFactory.createProject(pi, rootTask);

        assertFalse (rootTask.isAvailable());

        Task child = nodeFactory.createTask("x");
        parent.add(child);

        assertFalse (rootTask.isAvailable());

        child.setCompletionDate(new Date ());

        assertFalse (rootTask.isAvailable());
    }

    @Test
    public void testUncompletedTaskCount () {
        Task parent = nodeFactory.createTask("parent");

        assertEquals (0, parent.getUncompletedTaskCount());

        Task child = nodeFactory.createTask("child");
        parent.add(child);

        assertEquals (1, parent.getUncompletedTaskCount());

        child.setCompletionDate(new Date ());

        assertEquals (0, parent.getUncompletedTaskCount());
    }

    @Test
    public void testIsComplete () {

        Folder f = nodeFactory.createFolder("f");

        Project p = nodeFactory.createProject("p");
        f.add(p);

        Task t1 = nodeFactory.createTask("t1");
        p.add(t1);

        Task t2 = nodeFactory.createTask("t2");
        t1.add(t2);

        assertFalse (t2.isCompleted());
        t2.setCompletionDate(new Date());
        assertTrue(t2.isCompleted());
        t2.setCompletionDate(null);

        assertFalse (t2.isCompleted());
        t1.setCompletionDate(new Date());
        assertTrue(t2.isCompleted());
        t1.setCompletionDate(null);

        assertFalse (t2.isCompleted());
        p.setStatus(Project.COMPLETED);
        assertTrue (t2.isCompleted());
        p.setStatus(Project.ACTIVE);

        assertFalse (t2.isCompleted());
        p.setStatus(Project.DROPPED);
        assertTrue (t2.isCompleted());
        p.setStatus(Project.ACTIVE);

        assertFalse (t2.isCompleted());
        f.setActive(false);
        assertTrue (t2.isCompleted());
    }

    @Test
    public void testIsAvailable () throws ParseException {

        Folder f = nodeFactory.createFolder("f");

        Project p = nodeFactory.createProject("p");
        f.add(p);

        Task t = nodeFactory.createTask("t");
        p.add(t);

        assertTrue (t.isAvailable());
        t.setCompletionDate(new Date());
        assertFalse(t.isAvailable());
        t.setCompletionDate(null);

        assertTrue (t.isAvailable());
        p.setStatus(Project.COMPLETED);
        assertFalse (t.isAvailable());
        p.setStatus(Project.ACTIVE);

        assertTrue (t.isAvailable());
        f.setActive(false);
        assertFalse (t.isAvailable());
    }
}
