package org.psidnell.omnifocus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptException;

import org.junit.Test;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Document;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.osa.OSA;

import com.fasterxml.jackson.core.JsonProcessingException;

public class OmniFocusTest extends EnvironmentTest {
    
    @Test
    // TODO I AM HERE
    public void testXXX () throws IOException, ClassNotFoundException, ScriptException {
        String[] exprs = {"Document:projects=flattenedProjects.whose({name:'Home'})"};
        OmniFocus of = new OmniFocus(Arrays.asList(exprs));
        String json = of.execute("console.log(JSON.stringify(mapDocument(doc)))");
        Document d = of.asDocument(json);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(d));
        assertEquals (0, d.getFolders().size());
        assertEquals (0, d.getTasks().size());
        //assertEquals (0, d.getProjects().size());
        assertEquals (0, d.getContexts().size());
    }
    
    @Test
    public void testLoadRootDocument () throws IOException, ClassNotFoundException, ScriptException {
        String[] exprs = {};
        OmniFocus of = new OmniFocus(Arrays.asList(exprs));
        String json = of.execute("console.log(JSON.stringify(mapDocument(doc)))");
        Document d = of.asDocument(json);
        assertEquals (0, d.getFolders().size());
        assertEquals (0, d.getTasks().size());
        assertEquals (0, d.getProjects().size());
        assertEquals (0, d.getContexts().size());
    }
    
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
