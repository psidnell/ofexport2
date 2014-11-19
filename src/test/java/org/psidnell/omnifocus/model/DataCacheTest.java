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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.psidnell.omnifocus.format.SimpleTextListFormatter;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;

public class DataCacheTest {

    @Test
    public void testBuild () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, SQLException, IOException {
        // Useless "run it and see what breaks test"
        OutputStreamWriter out = new OutputStreamWriter(System.out);
        
        DataCache cache = SQLiteDAO.load();
        for (Context c : cache.getContexts().values()) {
            new SimpleTextListFormatter ().format(c, out);
        }
        out.flush();
    }
    
    @Test
    public void testConstruction () {
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        assertTrue (dataCache.getContexts().isEmpty());
        assertTrue (dataCache.getProjects().isEmpty());
        assertTrue (dataCache.getFolders().isEmpty());
        assertTrue (dataCache.getTasks().isEmpty());
    }
    
    @Test
    public void testTaskHierarchy () {
        final String parentId = "zzz";
        
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        Task child = new Task ();
        child.setParentTaskId(parentId);
        
        Task parent = new Task ();
        parent.setId(parentId);
        
        tasks.add(child);
        tasks.add(parent);
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        assertEquals (2, dataCache.getTasks().size());
        assertSame (child, dataCache.getTasks().get(child.getId()));
        assertSame (parent, dataCache.getTasks().get(parent.getId()));
        assertSame (parent, child.getParent());
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));

        assertTrue (dataCache.getContexts().isEmpty());
        assertTrue (dataCache.getProjects().isEmpty());
        assertTrue (dataCache.getFolders().isEmpty());
    }
    
    @Test
    public void testFolderHierarchy () {
        final String parentId = "zzz";
        
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        Folder child = new Folder ();
        child.setParentFolderId(parentId);
        
        Folder parent = new Folder ();
        parent.setId(parentId);
        
        folders.add(child);
        folders.add(parent);
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        assertEquals (2, dataCache.getFolders().size());
        assertSame (child, dataCache.getFolders().get(child.getId()));
        assertSame (parent, dataCache.getFolders().get(parent.getId()));
        assertSame (parent, child.getParent());
        assertEquals (1, parent.getFolders().size());
        assertTrue (parent.getFolders().contains(child));

        assertTrue (dataCache.getContexts().isEmpty());
        assertTrue (dataCache.getProjects().isEmpty());
        assertTrue (dataCache.getTasks().isEmpty());
    }
    
    @Test
    public void testContextHierarchy () {
        final String parentId = "zzz";
        
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        Context child = new Context ();
        child.setParentContextId(parentId);
        
        Context parent = new Context ();
        parent.setId(parentId);
        
        contexts.add(child);
        contexts.add(parent);
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        assertEquals (2, dataCache.getContexts().size());
        assertSame (child, dataCache.getContexts().get(child.getId()));
        assertSame (parent, dataCache.getContexts().get(parent.getId()));
        assertSame (parent, child.getParent());
        assertEquals (1, parent.getContexts().size());
        assertTrue (parent.getContexts().contains(child));

        assertTrue (dataCache.getFolders().isEmpty());
        assertTrue (dataCache.getProjects().isEmpty());
        assertTrue (dataCache.getTasks().isEmpty());
    }
    
    @Test
    public void testContextTaskHierarchy () {
        final String parentId = "zzz";
        
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        Task child = new Task ();
        child.setContextId(parentId);
        
        Context parent = new Context ();
        parent.setId(parentId);
        
        tasks.add(child);
        contexts.add(parent);
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        assertEquals (1, dataCache.getTasks().size());
        assertSame (child, dataCache.getTasks().get(child.getId()));
        assertEquals (1, dataCache.getContexts().size());
        assertSame (parent, dataCache.getContexts().get(parent.getId()));
        assertSame (parent, child.getContext());
        assertEquals (1, parent.getTasks().size());
        assertTrue (parent.getTasks().contains(child));

        assertTrue (dataCache.getFolders().isEmpty());
        assertTrue (dataCache.getProjects().isEmpty());
    }
    
    @Test
    public void testProjectCreatedFromRootTask () {
        final String id = "zzz";
        
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        Task rootTask = new Task ();
        rootTask.setId(id);
        
        ProjectInfo projInfo = new ProjectInfo ();
        projInfo.setRootTaskId(id);
                
        tasks.add(rootTask);
        projInfos.add(projInfo);
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        assertTrue (dataCache.getTasks().isEmpty());

        Project project = dataCache.getProjects().get(id);
        assertNotNull (project);
        assertEquals (id, project.getId());
        assertTrue (project.getTasks().isEmpty());

        assertTrue (dataCache.getTasks().isEmpty());
        assertTrue (dataCache.getContexts().isEmpty());
        assertTrue (dataCache.getFolders().isEmpty());
    }
    
    @Test
    public void testProjectOwnsRootTaskChildren () {
        final String rootId = "zzz";
        final String id = "xxx";
        
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        Task rootTask = new Task ();
        rootTask.setId(rootId);
        
        Task child = new Task ();
        child.setId(id);
        child.setParentTaskId(rootId);
        
        ProjectInfo projInfo = new ProjectInfo ();
        projInfo.setRootTaskId(rootId);
                
        tasks.add(rootTask);
        tasks.add(child);
        projInfos.add(projInfo);
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        Project project = dataCache.getProjects().get(rootId);
        assertNotNull (project);
        assertEquals (rootId, project.getId());

        assertEquals (1, dataCache.getTasks().size());
        assertSame (child, dataCache.getTasks().get(id));
        assertSame (project, child.getProject());
        assertNull (child.getParent());
        
        assertTrue (dataCache.getContexts().isEmpty());
        assertTrue (dataCache.getFolders().isEmpty());
    }
    
    @Test
    public void testProjectParentFolder () {
        final String id = "zzz";
        final String folderId = "xxx";
        
        Collection<Folder> folders = new LinkedList<>();
        Collection<ProjectInfo> projInfos = new LinkedList<>();
        Collection<Task> tasks = new LinkedList<>();
        Collection<Context> contexts = new LinkedList<>();
        
        Task rootTask = new Task ();
        rootTask.setId(id);
        
        ProjectInfo projInfo = new ProjectInfo ();
        projInfo.setRootTaskId(id);
        projInfo.setFolderId(folderId);
        
        Folder parent = new Folder();
        parent.setId(folderId);
        
        tasks.add(rootTask);
        projInfos.add(projInfo);
        folders.add(parent);
        
        DataCache dataCache = new DataCache(folders, projInfos, tasks, contexts);
        
        assertTrue (dataCache.getTasks().isEmpty());

        Project project = dataCache.getProjects().get(id);
        assertNotNull (project);
        assertEquals (id, project.getId());
        assertTrue (project.getTasks().isEmpty());
        assertEquals (1, dataCache.getFolders().size());
        assertSame (parent, dataCache.getFolders().get(folderId));

        assertTrue (dataCache.getTasks().isEmpty());
        assertTrue (dataCache.getContexts().isEmpty());
    }
    
}
