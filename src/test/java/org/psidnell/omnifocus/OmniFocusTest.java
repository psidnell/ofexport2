package org.psidnell.omnifocus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.script.ScriptException;

import org.junit.Test;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

import com.fasterxml.jackson.core.JsonProcessingException;

public class OmniFocusTest extends EnvironmentTest {
    
    @Test
    public void testGetProjects () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(NON_EMPTY_SEQ_PROJECT).get(0);
        assertNotNull (project);
        assertEquals (NON_EMPTY_SEQ_PROJECT, project.getName());
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(project));
    }
    
    @Test
    public void testGetContexts () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT).get(0);
        assertNotNull (context);
        assertEquals (NON_EMPTY_CONTEXT, context.getName());
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(context));
    }
    
    @Test
    public void testLoadAllInboxTasks () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        List<Task> tasks = of.loadAllInboxTasks(null);
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testLoadAllProjectTasks () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(NON_EMPTY_SEQ_PROJECT).get(0);
        of.loadAllTasks(project, null);
        List<Task> tasks = project.getTasks ();
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testLoadNextProjectTask () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(NON_EMPTY_SEQ_PROJECT).get(0);
        of.loadNextTask(project);
        List<Task> tasks = project.getTasks ();
        assertFalse (tasks.isEmpty());
        // Make sure we're not getting the "project task"
        assertNotEquals (tasks.get(0).getId(), project.getId());
    }
    
    @Test
    public void testLoadNextProjectTask_empty() throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(EMPTY_SEQ_PROJECT).get(0);
        of.loadNextTask(project);
        List<Task> tasks = project.getTasks ();
        assertEquals (1, tasks.size());
        // A task representing the project is returned:
        assertEquals (tasks.get(0).getId(), project.getId());
    }
    
    @Test
    public void testLoadAllContextTasks () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT).get(0);
        of.loadAllTasks(context, null);
        List<Task> tasks = context.getTasks ();
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testLoadRemainingContextTasks () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT).get(0);
        of.loadRemainingTasks(context, null);
        List<Task> tasks = context.getTasks ();
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testLoadAvailableContextTasks () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT).get(0);
        of.loadAvailableTasks(context, null);
        List<Task> tasks = context.getTasks ();
        assertFalse (tasks.isEmpty());
    }
    
    @Test
    public void testConstant () throws JsonProcessingException {
        assertEquals ("\"\"", OmniFocus.constant(""));
        assertEquals ("\"xxx\"", OmniFocus.constant("xxx"));
        assertEquals ("\"\\n\"", OmniFocus.constant("\n"));
    }
}
