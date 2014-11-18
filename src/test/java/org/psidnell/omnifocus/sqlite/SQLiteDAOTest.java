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
package org.psidnell.omnifocus.sqlite;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.junit.Test;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.ProjectInfo;
import org.psidnell.omnifocus.model.Task;

public class SQLiteDAOTest {
    
    @Test
    public void testDumpTables() throws SQLException, ClassNotFoundException {
        try (Connection c = SQLiteDAO.getConnection()) {
            SQLiteDAO.printTables(c);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadTasks() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = SQLiteDAO.getConnection()) {
            Collection<Task> tasks = SQLiteDAO.load(c, SQLiteDAO.TASK_DAO, null);
            assertFalse (tasks.isEmpty());
            Task task = tasks.iterator().next();
            System.out.println(task.getId() + ":" + task.getName());
        }
    }
    
    @Test
    public void testLoadFolders() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = SQLiteDAO.getConnection()) {
            Collection<Folder> folders = SQLiteDAO.load(c, SQLiteDAO.FOLDER_DAO, null);
            assertFalse (folders.isEmpty());
            Folder folder = folders.iterator().next();
            System.out.println(folder.getId() + ":" + folder.getName());
        }
    }
    
    @Test
    public void testLoadProjectsInfo() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = SQLiteDAO.getConnection()) {
            Collection<ProjectInfo> projects = SQLiteDAO.load(c, SQLiteDAO.PROJECT_INFO_DAO, null);
            assertFalse (projects.isEmpty());
            ProjectInfo project = projects.iterator().next();
            System.out.println(project.getRootTaskId());
        }
    }

    @Test
    public void testLoadContexts() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = SQLiteDAO.getConnection()) {
            Collection<Context> contexts = SQLiteDAO.load(c, SQLiteDAO.CONTEXT_DAO, null);
            assertFalse (contexts.isEmpty());
            Context context = contexts.iterator().next();
            System.out.println(context.getId() + ":" + context.getName());
        }
    }
    
    @Test
    public void testLoadDataCache () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, SQLException {
        DataCache dataCache = SQLiteDAO.load();
        assertFalse (dataCache.getFolders().isEmpty());
        assertFalse (dataCache.getProjInfos().isEmpty());
        assertFalse (dataCache.getTasks().isEmpty());
        assertFalse (dataCache.getContexts().isEmpty());
    }

}
