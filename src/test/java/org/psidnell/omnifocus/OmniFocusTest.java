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
package org.psidnell.omnifocus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptException;

import org.junit.Test;
import org.psidnell.omnifocus.expr.ExprParser;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Document;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.osa.OSAClassDescriptor;

import com.fasterxml.jackson.core.JsonProcessingException;

public class OmniFocusTest extends EnvironmentTest {
    
    @Test
    public void testLoadRootDocument_emptyByDefault () throws IOException, ClassNotFoundException, ScriptException {
        HashMap<String, OSAClassDescriptor> descs = new HashMap<>();
        
        ExprParser.parse("Document", descs);
        OmniFocus of = new OmniFocus();
        Collection<OSAClassDescriptor> exprs = descs.values();
        String json = of.execute(exprs);
        Document d = of.asDocument(json);
        assertEquals (0, d.getFolders().size());
        assertEquals (0, d.getTasks().size());
        assertEquals (0, d.getProjects().size());
        assertEquals (0, d.getContexts().size());
    }
    
    @Test
    public void testLoadRootDocument_withRootProjects () throws IOException, ClassNotFoundException, ScriptException {
        String value = "Document.projects:flattenedProjects.whose({name:'TestProject'})";
        HashMap<String, OSAClassDescriptor> descs = new HashMap<>();
        ExprParser.parse(value, descs);
        OmniFocus of = new OmniFocus();
        Collection<OSAClassDescriptor> exprs = descs.values();
        
        OSAClassDescriptor cd = new OSAClassDescriptor(Document.class);
        // Eg cmdline: -D projects:flattenedProjects.whose({name:'TestProject'})
        cd.override("projects", "flattenedProjects.whose({name:'TestProject'})");

        String json = of.execute(exprs);
        Document d = of.asDocument(json);
        assertEquals (0, d.getFolders().size());
        assertEquals (0, d.getTasks().size());
        assertNotEquals (0, d.getProjects().size());
        assertEquals (0, d.getContexts().size());
    }
    
    
    
    // --------- OLD
    @Test
    public void testGetProjects () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(NON_EMPTY_SEQ_PROJECT, null).get(0);
        assertNotNull (project);
        assertEquals (NON_EMPTY_SEQ_PROJECT, project.getName());
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(project));
    }
    
    @Test
    public void testGetContextsByName () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT, null).get(0);
        assertNotNull (context);
        assertEquals (NON_EMPTY_CONTEXT, context.getName());
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(context));
    }
    
    @Test
    public void testLoadAllInboxTasks () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        List<Task> tasks = of.loadAllInboxTasks(null);
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testLoadAllProjectTasks () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(NON_EMPTY_SEQ_PROJECT, null).get(0);
        of.loadAllTasks(project, null);
        List<Task> tasks = project.getTasks ();
        assertFalse (tasks.isEmpty());
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasks));
    }
    
    @Test
    public void testLoadNextProjectTask () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(NON_EMPTY_SEQ_PROJECT, null).get(0);
        of.loadNextTask(project, null);
        List<Task> tasks = project.getTasks ();
        assertFalse (tasks.isEmpty());
        // Make sure we're not getting the "project task"
        assertNotEquals (tasks.get(0).getId(), project.getId());
    }
    
    @Test
    public void testLoadNextProjectTask_empty() throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(EMPTY_SEQ_PROJECT, null).get(0);
        of.loadNextTask(project, null);
        List<Task> tasks = project.getTasks ();
        assertEquals (1, tasks.size());
        // A task representing the project is returned:
        assertEquals (tasks.get(0).getId(), project.getId());
    }
    
    @Test
    public void testLoadAllContextTasks () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT, null).get(0);
        of.loadAllTasks(context, null);
        List<Task> tasks = context.getTasks ();
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testLoadRemainingContextTasks () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT, null).get(0);
        of.loadRemainingTasks(context, null);
        List<Task> tasks = context.getTasks ();
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testLoadAvailableContextTasks () throws IOException, ScriptException, ClassNotFoundException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT, null).get(0);
        of.loadAvailableTasks(context, null);
        List<Task> tasks = context.getTasks ();
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testBuildAccessor () {
        assertEquals ("console.log(JSON.stringify(adapt(applyFilter(doc.get,baseFilt),filt2,filt3)));",
                OmniFocus.buildAccessor ("adapt", "get", "baseFilt", "filt2", "filt3"));
        
    }
    
    @Test
    public void testConstant () throws JsonProcessingException {
        assertEquals ("\"\"", OmniFocus.constant(""));
        assertEquals ("\"xxx\"", OmniFocus.constant("xxx"));
        assertEquals ("\"\\n\"", OmniFocus.constant("\n"));
    }
}
