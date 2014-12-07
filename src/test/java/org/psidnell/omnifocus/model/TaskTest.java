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

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.psidnell.omnifocus.ConfigParams;

public class TaskTest {

    @Test
    public void testAddTask () {
        Task parent = new Task ();
        Task child = new Task ();

        parent.add(child);
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));
        assertSame (parent, child.getProjectModeParent());
    }

    @Test
    public void testAddTaskDisconnectsFromPrevious () {
        Task parent1 = new Task ();
        Task child = new Task ();

        parent1.add(child);
        assertEquals (1, parent1.getTasks().size());
        assertTrue (parent1.getTasks().contains(child));
        assertSame (parent1, child.getProjectModeParent());

        Task parent2 = new Task ();
        parent2.add(child);
        assertEquals (1, parent2.getTasks().size());
        assertTrue (parent2.getTasks().contains(child));
        assertSame (parent2, child.getProjectModeParent());

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

        Project parent = new Project ("parent");
        Task child = new Task ("child");
        parent.add(child);

        assertTrue (child.isAvailable());

        parent.setStatus("inactive");
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

    @Test public void testRootTaskAvailabilityAsSequentialProject () {

        Task rootTask = new Task ("root");
        rootTask.setSequential(true);
        ProjectInfo pi = new ProjectInfo();
        pi.setSingleActionList(false);
        pi.setStatus("active");

        Project parent = new Project (pi, rootTask);

        assertTrue (rootTask.isAvailable());

        Task child = new Task ("x");
        parent.add(child);

        assertFalse (rootTask.isAvailable());

        child.setCompletionDate(new Date ());

        assertTrue (rootTask.isAvailable());
    }

    @Test public void testRootTaskAvailabilityAsSingleActionListProject () {

        Task rootTask = new Task ("root");
        rootTask.setSequential(true);
        ProjectInfo pi = new ProjectInfo();
        pi.setSingleActionList(true);
        pi.setStatus("active");

        Project parent = new Project (pi, rootTask);

        assertFalse (rootTask.isAvailable());

        Task child = new Task ("x");
        parent.add(child);

        assertFalse (rootTask.isAvailable());

        child.setCompletionDate(new Date ());

        assertFalse (rootTask.isAvailable());
    }

    @Test
    public void testUncompletedTaskCount () {
        Task parent = new Task ("parent");

        assertEquals (0, parent.getUncompletedTaskCount());

        Task child = new Task ("child");
        parent.add(child);

        assertEquals (1, parent.getUncompletedTaskCount());

        child.setCompletionDate(new Date ());

        assertEquals (0, parent.getUncompletedTaskCount());
    }

    @Test
    public void testIsComplete () {

        Folder f = new Folder ("f");

        Project p = new Project ("p");
        f.add(p);

        Task t1 = new Task ("t1");
        p.add(t1);

        Task t2 = new Task ("t2");
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
    public void testIsDueSoon () throws ParseException {

        ConfigParams config = new ConfigParams();
        config.setDueSoon("1d");

        Folder f = new Folder ("f");

        Project p = new Project ("p");
        p.setConfigParams(config);
        f.add(p);

        Task t = new Task ("t");
        t.setConfigParams(config);
        p.add(t);

        assertFalse (t.isDueSoon());
        t.setDueDate(new Date());
        assertTrue(t.isDueSoon());
        t.setDueDate(null);

        assertFalse (t.isDueSoon());
        p.setDueDate(new Date());
        assertFalse (t.isDueSoon()); // this is cascaded by omnifocus
        p.setDueDate(null);

        assertFalse (t.isDueSoon());
        p.setStatus(Project.COMPLETED);
        p.setDueDate(new Date());
        assertFalse (t.isDueSoon());
        p.setStatus(Project.ACTIVE);

        assertFalse (t.isDueSoon());
        p.setDueDate(new Date());
        f.setActive(false);
        assertFalse (t.isDueSoon());
    }

    @Test
    public void testIsAvailable () throws ParseException {

        Folder f = new Folder ("f");

        Project p = new Project ("p");
        f.add(p);

        Task t = new Task ("t");
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
