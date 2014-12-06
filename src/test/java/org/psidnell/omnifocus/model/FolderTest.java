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

import org.junit.Test;



public class FolderTest {

    @Test
    public void testAddFolder () {
        Folder parent = new Folder ();
        Folder child = new Folder ();

        parent.add(child);
        assertEquals (1, parent.getFolders().size());
        assertTrue (parent.getFolders().contains(child));
        assertSame (parent, child.getProjectModeParent());
    }

    @Test
    public void testAddFolderDisconnectsFromPrevious () {
        Folder parent1 = new Folder ();
        Folder child = new Folder ();

        parent1.add(child);
        assertEquals (1, parent1.getFolders().size());
        assertTrue (parent1.getFolders().contains(child));
        assertSame (parent1, child.getProjectModeParent());

        Folder parent2 = new Folder ();
        parent2.add(child);
        assertEquals (1, parent2.getFolders().size());
        assertTrue (parent2.getFolders().contains(child));
        assertSame (parent2, child.getProjectModeParent());

        assertTrue (parent1.getFolders().isEmpty());
    }

    @Test
    public void testAddProject () {
        Folder parent = new Folder ();
        Project child = new Project ();

        parent.add(child);
        assertEquals (1, parent.getProjects().size());
        assertTrue (parent.getProjects().contains(child));
        assertSame (parent, child.getFolder());
    }

    @Test
    public void testAddProjectDisconnectsFromPrevious () {
        Folder parent1 = new Folder ();
        Project child = new Project ();

        parent1.add(child);
        assertEquals (1, parent1.getProjects().size());
        assertTrue (parent1.getProjects().contains(child));
        assertSame (parent1, child.getFolder());

        Folder parent2 = new Folder ();
        parent2.add(child);
        assertEquals (1, parent2.getProjects().size());
        assertTrue (parent2.getProjects().contains(child));
        assertSame (parent2, child.getFolder());

        assertTrue (parent1.getProjects().isEmpty());
    }

    @Test public void testAvailability () {

        Folder f = new Folder ("f");
        assertTrue (f.isAvailable());

        f.setActive(false);
        assertFalse (f.isAvailable());
    }
}