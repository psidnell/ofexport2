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

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.springframework.context.ApplicationContext;

public class FolderTest {

    private NodeFactory nodeFactory;

    @Before
    public void setup () {
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        nodeFactory = appContext.getBean("nodefactory", NodeFactory.class);
    }

    @Test
    public void testAddFolder () {
        Folder parent = nodeFactory.createFolder("f");
        Folder child = nodeFactory.createFolder("f");

        parent.add(child);
        assertEquals (1, parent.getFolders().size());
        assertTrue (parent.getFolders().contains(child));
        assertSame (parent, child.getProjectModeParent());
    }

    @Test
    public void testAddFolderDisconnectsFromPrevious () {
        Folder parent1 = nodeFactory.createFolder("f");
        Folder child = nodeFactory.createFolder("f");

        parent1.add(child);
        assertEquals (1, parent1.getFolders().size());
        assertTrue (parent1.getFolders().contains(child));
        assertSame (parent1, child.getProjectModeParent());

        Folder parent2 = nodeFactory.createFolder("f");
        parent2.add(child);
        assertEquals (1, parent2.getFolders().size());
        assertTrue (parent2.getFolders().contains(child));
        assertSame (parent2, child.getProjectModeParent());

        assertTrue (parent1.getFolders().isEmpty());
    }

    @Test
    public void testAddProject () {
        Folder parent = nodeFactory.createFolder("f");
        Project child = nodeFactory.createProject("p");

        parent.add(child);
        assertEquals (1, parent.getProjects().size());
        assertTrue (parent.getProjects().contains(child));
        assertSame (parent, child.getProjectModeParent());
    }

    @Test
    public void testAddProjectDisconnectsFromPrevious () {
        Folder parent1 = nodeFactory.createFolder("f");
        Project child = nodeFactory.createProject("p");

        parent1.add(child);
        assertEquals (1, parent1.getProjects().size());
        assertTrue (parent1.getProjects().contains(child));
        assertSame (parent1, child.getProjectModeParent());

        Folder parent2 = nodeFactory.createFolder("f");
        parent2.add(child);
        assertEquals (1, parent2.getProjects().size());
        assertTrue (parent2.getProjects().contains(child));
        assertSame (parent2, child.getProjectModeParent());

        assertTrue (parent1.getProjects().isEmpty());
    }

    @Test
    public void testIsActiveInheritsFromParentProject () {
        Folder parent = nodeFactory.createFolder("f");
        Folder child = nodeFactory.createFolder("f");
        parent.add(child);

        assertTrue (parent.isActive());

        parent.setActive(false);
        assertFalse (child.isActive());
    }
}