package org.psidnell.omnifocus;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Project;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EnvironmentTest {
    
    protected static final String NON_EMPTY_CONTEXT = "TestContext";
    protected static final String NON_EMPTY_SEQ_PROJECT = "TestProject";
    protected static final String EMPTY_SEQ_PROJECT = "TestProjectEmpty";
    
    protected ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testNON_EMPTY_SEQ_PROJECT () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(NON_EMPTY_SEQ_PROJECT).get(0);
        assertNotNull (project);
        assertEquals (NON_EMPTY_SEQ_PROJECT, project.getName());
        assertTrue (project.getSequential());
        
        // Sequential projects have a root task which is themselves - make sure that's not the case here
        of.loadAllTasks(project, null);
        assertFalse (project.getTasks().isEmpty());
        assertNotEquals(project.getId(), project.getTasks().get(0).getId());
        
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(project));
    }
    
    @Test
    public void testEMPTY_SEQ_PROJECT () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Project project = of.getProjectsByName(EMPTY_SEQ_PROJECT).get(0);
        assertNotNull (project);
        assertEquals (EMPTY_SEQ_PROJECT, project.getName());
        assertTrue (project.getSequential());
        
        // Sequential projects have a root task which is themselves - make sure that's the case here
        of.loadAllTasks(project, null);
        assertTrue (project.getTasks().isEmpty());        
    }
    
    @Test
    public void testNON_EMPTY_CONTEXT () throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
        Context context = of.getContextsByName(NON_EMPTY_CONTEXT).get(0);
        assertNotNull (context);
        assertEquals (NON_EMPTY_CONTEXT, context.getName());
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(context));
    }
}
