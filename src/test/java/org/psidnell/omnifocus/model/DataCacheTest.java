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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.stream.Collectors;

import org.junit.Test;

public class DataCacheTest {

    @Test
    public void testConstruction() {
        DataCache dataCache = new DataCache();

        assertTrue(dataCache.getContexts().isEmpty());
        assertTrue(dataCache.getProjects().isEmpty());
        assertTrue(dataCache.getFolders().isEmpty());
        assertTrue(dataCache.getTasks().isEmpty());
    }

    @Test
    public void testTaskHierarchy() {
        final String parentId = "zzz";

        DataCache dataCache = new DataCache();

        Task child = new Task();
        child.setName("child");
        child.setParentTaskId(parentId);

        Task parent = new Task();
        parent.setName("parent");
        parent.setId(parentId);

        dataCache.add(child);
        dataCache.add(parent);
        dataCache.build();

        assertEquals(2, dataCache.getTasks().size());
        assertSame(child, dataCache.getTasks().get(child.getId()));
        assertSame(parent, dataCache.getTasks().get(parent.getId()));
        assertSame(parent, child.getParent());
        assertEquals(1, parent.getTasks().size());
        assertTrue(parent.getTasks().contains(child));

        assertEquals("[parent, child]", child.getProjectPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());
        assertEquals("[No Context, child]", child.getContextPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getProjects().isEmpty());
        assertTrue(dataCache.getFolders().isEmpty());

        assertEquals(1, dataCache.getContexts().size());
        Context noContext = dataCache.getContexts().values().iterator().next();
        assertEquals ("No Context", noContext.getName());
    }

    @Test
    public void testFolderHierarchy() {
        final String parentId = "zzz";

        DataCache dataCache = new DataCache();

        Folder child = new Folder();
        child.setName("child");
        child.setParentFolderId(parentId);

        Folder parent = new Folder();
        parent.setName("parent");
        parent.setId(parentId);

        dataCache.add(child);
        dataCache.add(parent);
        dataCache.build();

        assertEquals(2, dataCache.getFolders().size());
        assertSame(child, dataCache.getFolders().get(child.getId()));
        assertSame(parent, dataCache.getFolders().get(parent.getId()));
        assertSame(parent, child.getParent());
        assertEquals(1, parent.getFolders().size());
        assertTrue(parent.getFolders().contains(child));

        assertEquals("[parent, child]", child.getProjectPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());
        try {
            child.getContextPath();
            fail();
        } catch (UnsupportedOperationException e) {
        }

        assertTrue(dataCache.getContexts().isEmpty());
        assertTrue(dataCache.getProjects().isEmpty());
        assertTrue(dataCache.getTasks().isEmpty());
    }

    @Test
    public void testContextHierarchy() {
        final String parentId = "zzz";

        DataCache dataCache = new DataCache();

        Context child = new Context();
        child.setName("child");
        child.setParentContextId(parentId);

        Context parent = new Context();
        parent.setName("parent");
        parent.setId(parentId);

        dataCache.add(child);
        dataCache.add(parent);
        dataCache.build();

        assertEquals(2, dataCache.getContexts().size());
        assertSame(child, dataCache.getContexts().get(child.getId()));
        assertSame(parent, dataCache.getContexts().get(parent.getId()));
        assertSame(parent, child.getParent());
        assertEquals(1, parent.getContexts().size());
        assertTrue(parent.getContexts().contains(child));

        try {
            child.getProjectPath();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        assertEquals("[parent, child]", child.getContextPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getFolders().isEmpty());
        assertTrue(dataCache.getProjects().isEmpty());
        assertTrue(dataCache.getTasks().isEmpty());
    }

    @Test
    public void testContextTaskHierarchy() {
        final String parentId = "zzz";

        DataCache dataCache = new DataCache();

        Task child = new Task();
        child.setName("child");
        child.setContextId(parentId);

        Context parent = new Context();
        parent.setName("parent");
        parent.setId(parentId);

        dataCache.add(child);
        dataCache.add(parent);
        dataCache.build();

        assertEquals(1, dataCache.getTasks().size());
        assertSame(child, dataCache.getTasks().get(child.getId()));
        assertEquals(1, dataCache.getContexts().size());
        assertSame(parent, dataCache.getContexts().get(parent.getId()));
        assertSame(parent, child.getContext());
        assertEquals(1, parent.getTasks().size());
        assertTrue(parent.getTasks().contains(child));

        assertEquals("[child]", child.getProjectPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());
        assertEquals("[parent, child]", child.getContextPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getFolders().isEmpty());
        assertTrue(dataCache.getProjects().isEmpty());
    }

    @Test
    public void testProjectCreatedFromRootTask() {
        final String id = "zzz";

        DataCache dataCache = new DataCache();

        Task rootTask = new Task();
        rootTask.setName("rootTask");
        rootTask.setId(id);

        ProjectInfo projInfo = new ProjectInfo();
        projInfo.setRootTaskId(id);

        dataCache.add(rootTask);
        dataCache.add(projInfo);
        dataCache.build();

        assertTrue(dataCache.getTasks().isEmpty());

        Project project = dataCache.getProjects().get(id);
        assertNotNull(project);
        assertEquals(id, project.getId());
        assertTrue(project.getTasks().isEmpty());

        assertEquals("[rootTask]", project.getProjectPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());
        assertEquals("[rootTask]", project.getContextPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getTasks().isEmpty());
        assertTrue(dataCache.getFolders().isEmpty());

        assertEquals(1, dataCache.getContexts().size());
        Context noContext = dataCache.getContexts().values().iterator().next();
        assertEquals ("No Context", noContext.getName());
    }

    @Test
    public void testProjectOwnsRootTaskChildren() {
        final String rootId = "zzz";
        final String id = "xxx";

        DataCache dataCache = new DataCache();

        Task rootTask = new Task();
        rootTask.setName("rootTask");
        rootTask.setId(rootId);

        Task child = new Task();
        child.setId(id);
        child.setName("child");
        child.setParentTaskId(rootId);

        ProjectInfo projInfo = new ProjectInfo();
        projInfo.setRootTaskId(rootId);

        dataCache.add(rootTask);
        dataCache.add(child);
        dataCache.add(projInfo);
        dataCache.build();

        Project project = dataCache.getProjects().get(rootId);
        assertNotNull(project);
        assertEquals(rootId, project.getId());

        assertEquals(1, dataCache.getTasks().size());
        assertSame(child, dataCache.getTasks().get(id));
        assertSame(project, child.getParent());
        assertEquals (1, project.getTasks().size());
        assertTrue (project.getTasks().contains(child));

        assertEquals("[rootTask, child]", child.getProjectPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());
        assertEquals("[No Context, child]", child.getContextPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getFolders().isEmpty());

        assertEquals(1, dataCache.getContexts().size());
        Context noContext = dataCache.getContexts().values().iterator().next();
        assertEquals ("No Context", noContext.getName());
    }

    @Test
    public void testProjectParentFolder() {
        final String id = "zzz";
        final String folderId = "xxx";

        DataCache dataCache = new DataCache();

        Task rootTask = new Task();
        rootTask.setName("rootTask");
        rootTask.setId(id);

        ProjectInfo projInfo = new ProjectInfo();
        projInfo.setRootTaskId(id);
        projInfo.setFolderId(folderId);

        Folder parent = new Folder();
        parent.setName("parent");
        parent.setId(folderId);

        dataCache.add(rootTask);
        dataCache.add(projInfo);
        dataCache.add(parent);
        dataCache.build();

        assertTrue(dataCache.getTasks().isEmpty());

        Project project = dataCache.getProjects().get(id);
        assertNotNull(project);
        assertEquals(id, project.getId());
        assertTrue(project.getTasks().isEmpty());
        assertEquals(1, dataCache.getFolders().size());
        assertSame(parent, dataCache.getFolders().get(folderId));

        assertEquals ("[parent, rootTask]", project.getProjectPath().stream().map((x)->x.getName()).collect(Collectors.toList()).toString());
        assertEquals ("[rootTask]", project.getContextPath().stream().map((x)->x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getTasks().isEmpty());

        assertEquals(1, dataCache.getContexts().size());
        Context noContext = dataCache.getContexts().values().iterator().next();
        assertEquals ("No Context", noContext.getName());
    }

    @Test
    public void testInbox () {

        DataCache dataCache = new DataCache();

        Task child = new Task();
        child.setName("child");
        child.setInInbox(true);

        dataCache.add(child);
        dataCache.build();

        assertEquals(1, dataCache.getTasks().size());
        assertSame(child, dataCache.getTasks().get(child.getId()));

        assertEquals (1, dataCache.getProjects().size());
        Project inbox = dataCache.getProjects().values().iterator().next();

        assertSame(inbox, child.getParent());
        assertEquals(1, inbox.getTasks().size());
        assertTrue(inbox.getTasks().contains(child));

        assertEquals("[Inbox, child]", child.getProjectPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());
        assertEquals("[No Context, child]", child.getContextPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getFolders().isEmpty());

        assertEquals(1, dataCache.getContexts().size());
        Context noContext = dataCache.getContexts().values().iterator().next();
        assertEquals ("No Context", noContext.getName());
    }

    @Test
    public void testNoContext() {

        DataCache dataCache = new DataCache();

        Task child = new Task();
        child.setName("child");

        dataCache.add(child);
        dataCache.build();

        assertEquals(1, dataCache.getTasks().size());
        assertSame(child, dataCache.getTasks().get(child.getId()));
        assertEquals(1, dataCache.getContexts().size());
        assertEquals (1, dataCache.getContexts().size());
        Context noContext = dataCache.getContexts().values().iterator().next();
        assertSame(noContext, child.getContext());
        assertEquals(1, noContext.getTasks().size());
        assertTrue(noContext.getTasks().contains(child));

        assertEquals("[child]", child.getProjectPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());
        assertEquals("[No Context, child]", child.getContextPath().stream().map((x) -> x.getName()).collect(Collectors.toList()).toString());

        assertTrue(dataCache.getFolders().isEmpty());
        assertTrue(dataCache.getProjects().isEmpty());
    }

}
