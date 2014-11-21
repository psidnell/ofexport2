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
package org.psidnell.omnifocus.visitor;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.easymock.EasyMock;
import org.junit.Test;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class TraverserTest {

    @Test
    public void testTraverseTask_whenDisabled() {

        Task parent = new Task();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitTasks()).andReturn(false);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }

    @Test
    public void testTraverseTask_whenEnabled() throws Exception {

        Task parent = new Task();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitTasks()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        v.enter(parent);
        v.enter(child);
        v.exit(child);
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }

    @Test
    public void testTraverseProject_whenDisabled() {

        Project parent = new Project();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitProjects()).andReturn(false);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }

    @Test
    public void testTraverseProject_whenEnabled() throws Exception {

        Project parent = new Project();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitProjects()).andReturn(true);
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(false);
        v.enter(parent);
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }

    @Test
    public void testTraverseProjectAndTask_whenEnabled() throws Exception {

        Project parent = new Project();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitProjects()).andReturn(true);
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(true).atLeastOnce();
        v.enter(parent);
        v.enter(child);
        v.exit(child);
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }

    @Test
    public void testTraverseFolder_whenDisabled() {

        Folder parent = new Folder();
        Project childProject = new Project();
        parent.add(childProject);
        Folder childFolder = new Folder();
        parent.add(childFolder);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitFolders()).andReturn(false);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testTraverseFolder_whenEnabled() throws Exception {

        Folder parent = new Folder();
        Project childProject = new Project();
        parent.add(childProject);
        Folder childFolder = new Folder();
        parent.add(childFolder);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitFolders()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getVisitProjects()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterFolders()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterProjects()).andReturn(false).atLeastOnce();
        v.enter(parent);
        v.enter(childFolder);
        v.exit(childFolder);
        v.exit(parent);
        
        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testTraverseFolderAndProject_whenEnabled() throws Exception {

        Folder parent = new Folder();
        Project childProject = new Project();
        parent.add(childProject);
        Folder childFolder = new Folder();
        parent.add(childFolder);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitFolders()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getVisitProjects()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterFolders()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterProjects()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(false).atLeastOnce();
        v.enter(parent);
        v.enter(childFolder);
        v.exit(childFolder);
        v.enter(childProject);
        v.exit(childProject);
        v.exit(parent);
        
        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testTraverseContext_whenDisabled() {

        Context parent = new Context();
        Context childContext = new Context();
        parent.add(childContext);
        Task childTask = new Task();
        parent.add(childTask);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitContexts()).andReturn(false);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testTraverseContext_whenEnabled() throws Exception {

        Context parent = new Context();
        Context childContext = new Context();
        parent.add(childContext);
        Task childTask = new Task();
        parent.add(childTask);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitContexts()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterContexts()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(false).atLeastOnce();
        v.enter(parent);
        v.enter(childContext);
        v.exit(childContext);
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testTraverseContextAndTask_whenEnabled() throws Exception {

        Context parent = new Context();
        Context childContext = new Context();
        parent.add(childContext);
        Task childTask = new Task();
        parent.add(childTask);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitContexts()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterContexts()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(true).atLeastOnce();
        v.enter(parent);
        v.enter(childContext);
        v.exit(childContext);
        v.enter(childTask);
        v.exit(childTask);
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testFilterTaskInTask () throws Exception {

        Task parent = new Task();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getFilterTasks()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(true).atLeastOnce();
        v.enter(parent);
        EasyMock.expect(v.filterTasksDown(parent.getTasks())).andReturn(new LinkedList<Task>(parent.getTasks()));
        v.enter(child);
        EasyMock.expect(v.filterTasksDown(child.getTasks())).andReturn(new LinkedList<Task>(child.getTasks()));
        EasyMock.expect(v.filterTasksUp(child.getTasks())).andReturn(new LinkedList<Task>(child.getTasks()));
        v.exit(child);
        EasyMock.expect(v.filterTasksUp(parent.getTasks())).andReturn(new LinkedList<Task>(parent.getTasks()));
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testFilterTaskInProject () throws Exception {

        Project parent = new Project();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitProjects()).andReturn(true);
        EasyMock.expect(desc.getFilterTasks()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(true).atLeastOnce();
        v.enter(parent);
        EasyMock.expect(v.filterTasksDown(parent.getTasks())).andReturn(new LinkedList<Task>(parent.getTasks()));
        v.enter(child);
        EasyMock.expect(v.filterTasksDown(child.getTasks())).andReturn(new LinkedList<Task>(child.getTasks()));
        EasyMock.expect(v.filterTasksUp(child.getTasks())).andReturn(new LinkedList<Task>(child.getTasks()));
        v.exit(child);
        EasyMock.expect(v.filterTasksUp(parent.getTasks())).andReturn(new LinkedList<Task>(parent.getTasks()));
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testFilterTaskInContext () throws Exception {

        Context parent = new Context();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getFilterTasks()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterContexts()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getVisitContexts()).andReturn(true).atLeastOnce();
        v.enter(parent);
        EasyMock.expect(v.filterTasksDown(parent.getTasks())).andReturn(new LinkedList<Task>(parent.getTasks()));
        v.enter(child);
        EasyMock.expect(v.filterTasksDown(child.getTasks())).andReturn(new LinkedList<Task>(child.getTasks()));
        EasyMock.expect(v.filterTasksUp(child.getTasks())).andReturn(new LinkedList<Task>(child.getTasks()));
        v.exit(child);
        EasyMock.expect(v.filterTasksUp(parent.getTasks())).andReturn(new LinkedList<Task>(parent.getTasks()));
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testFilterProjectInFolder () throws Exception {

        Folder parent = new Folder();
        Project child = new Project();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitProjects()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getVisitFolders()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterFolders()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterProjects()).andReturn(true).atLeastOnce();
        v.enter(parent);
        EasyMock.expect(v.filterProjectsDown(parent.getProjects())).andReturn(new LinkedList<Project>(parent.getProjects()));
        v.enter(child);
        v.exit(child);
        EasyMock.expect(v.filterProjectsUp(parent.getProjects())).andReturn(new LinkedList<Project>(parent.getProjects()));
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testFilterFolderInFolder () throws Exception {

        Folder parent = new Folder();
        Folder child = new Folder();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitProjects()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getVisitFolders()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterFolders()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterProjects()).andReturn(false).atLeastOnce();
        v.enter(parent);
        EasyMock.expect(v.filterFoldersDown(parent.getFolders())).andReturn(new LinkedList<Folder>(parent.getFolders()));
        v.enter(child);
        EasyMock.expect(v.filterFoldersDown(child.getFolders())).andReturn(new LinkedList<Folder>(child.getFolders()));
        EasyMock.expect(v.filterFoldersUp(child.getFolders())).andReturn(new LinkedList<Folder>(child.getFolders()));
        v.exit(child);
        EasyMock.expect(v.filterFoldersUp(parent.getFolders())).andReturn(new LinkedList<Folder>(parent.getFolders()));
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testFilterContextInContext () throws Exception {

        Context parent = new Context();
        Context child = new Context();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitContexts()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getVisitTasks()).andReturn(false).atLeastOnce();
        EasyMock.expect(desc.getFilterContexts()).andReturn(true).atLeastOnce();
        EasyMock.expect(desc.getFilterTasks()).andReturn(false).atLeastOnce();
        v.enter(parent);
        EasyMock.expect(v.filterContextsDown(parent.getContexts())).andReturn(new LinkedList<Context>(parent.getContexts()));
        v.enter(child);
        EasyMock.expect(v.filterContextsDown(child.getContexts())).andReturn(new LinkedList<Context>(child.getContexts()));
        EasyMock.expect(v.filterContextsUp(child.getContexts())).andReturn(new LinkedList<Context>(child.getContexts()));
        v.exit(child);
        EasyMock.expect(v.filterContextsUp(parent.getContexts())).andReturn(new LinkedList<Context>(parent.getContexts()));
        v.exit(parent);

        // Replay
        EasyMock.replay(mocks);

        Traverser.traverse(v, parent);

        // Verify
        EasyMock.verify(mocks);
    }
    
    @Test
    public void testTraversalException() throws Exception {
        final Exception exception = new Exception ();
        
        Task parent = new Task();
        Task child = new Task();
        parent.add(child);

        // Create Mocks
        Visitor v = EasyMock.createMock(Visitor.class);
        VisitorDescriptor desc = EasyMock.createMock(VisitorDescriptor.class);
        Object mocks[] = {v, desc};

        // Record
        EasyMock.expect(v.getWhat()).andReturn(desc).once();
        EasyMock.expect(desc.getVisitTasks()).andReturn(true);
        v.enter (parent);
        EasyMock.expectLastCall().andThrow(exception);

        // Replay
        EasyMock.replay(mocks);

        try {
            Traverser.traverse(v, parent);
            fail ();
        }
        catch (TraversalException e) {
            assertSame (exception, e.getCause());
        }
            
        // Verify
        EasyMock.verify(mocks);
    }
}
