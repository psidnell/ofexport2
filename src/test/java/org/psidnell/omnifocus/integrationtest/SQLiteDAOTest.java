/*
 * Copyright 2015 Paul Sidnell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.psidnell.omnifocus.integrationtest;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.ProjectInfo;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

/**
 * @author psidnell
 *
 * Requires access to the OmniFocus database.
 */
@ContextConfiguration({"classpath:" + ApplicationContextFactory.CONFIG_XML, "classpath:/test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@SqlGroup({
        @Sql(scripts = "/of-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/of-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class SQLiteDAOTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private SQLiteDAO sqliteDAO;

    @Test
    public void testDumpTables() throws SQLException, ClassNotFoundException {
        try (Connection c = dataSource.getConnection()) {
            sqliteDAO.printTables(c);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadTasks() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = dataSource.getConnection()) {
            final List<Task> tasks = sqliteDAO.load(c, SQLiteDAO.TASK_DAO);
            assertEquals(tasks.size(), 3);

            final Task task1 = tasks.get(0);
            assertEquals("TEST-TASK-1", task1.getId());
            assertFalse(task1.isBlocked());
            assertEquals("TEST-CONTEXT-1", task1.getContextId());
            assertEquals(date(2007, 3, 15), task1.getDateAdded());
            assertEquals(date(2015, 5, 4), task1.getCompletionDate());
            assertEquals(date(2012, 11, 30), task1.getDueDate());
            assertEquals(date(1992, 6, 23), task1.getDeferDate());
            assertTrue(task1.isFlagged());
            assertTrue(task1.isInInbox());
            assertEquals(Integer.valueOf(2456), task1.getEstimatedMinutes());
            assertEquals("Task Test #1", task1.getName());
            assertEquals("TEST-PARENT-1", task1.getParentTaskId());
            assertEquals("Test Note #1 Contents", task1.getNote());
            assertEquals(45, task1.getRank());

            final Task task2 = tasks.get(1);
            assertEquals("TEST-TASK-2", task2.getId());
            assertTrue(task2.isBlocked());
            assertEquals("TEST-CONTEXT-2", task2.getContextId());
            assertEquals(date(1980, 11, 11), task2.getDateAdded());
            assertEquals(date(2034, 8, 30), task2.getCompletionDate());
            assertNull(task2.getDueDate());
            assertNull(task2.getDeferDate());
            assertFalse(task2.isFlagged());
            assertFalse(task2.isInInbox());
            assertEquals(Integer.valueOf(-1), task2.getEstimatedMinutes()); // setter translates NULL to -1
            assertNull(task2.getName());
            assertNull(task2.getParentTaskId());
            assertNull(task2.getNote());
            assertEquals(97, task2.getRank());

            final Task task3 = tasks.get(2);
            assertEquals("TEST-TASK-3", task3.getId());
            assertEquals("îøóöêå", task3.getName());
            assertEquals("いろはにほへとちりぬるを çà et là la qualité de son œuvre ’“”", task3.getNote());
            assertEquals(97, task3.getRank());
        }
    }
    
    @Test
    public void testLoadFolders() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = dataSource.getConnection()) {
            final List<Folder> folders = sqliteDAO.load(c, SQLiteDAO.FOLDER_DAO);
            assertEquals(folders.size(), 1);
            final Folder folder = folders.get(0);
            assertEquals("TEST-FOLDER", folder.getId());
            assertEquals(false, folder.isActive());
            assertEquals(date(2007, 3, 15), folder.getDateAdded());
            assertEquals(date(2015, 5, 4), folder.getDateModified());
            assertEquals("Folder Test", folder.getName());
            assertEquals(19, folder.getRank());
        }
    }

    @Test
    public void testLoadProjectsInfo() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = dataSource.getConnection()) {
            final List<ProjectInfo> projects = sqliteDAO.load(c, SQLiteDAO.PROJECT_INFO_DAO);
            assertEquals (1, projects.size());
            final ProjectInfo projectInfo = projects.get(0);
            assertTrue(projectInfo.isSingleActionList());
            assertEquals("TEST-FOLDER", projectInfo.getFolderId());
            assertEquals("TEST-STATUS", projectInfo.getStatus());
            assertEquals("TEST-TASK", projectInfo.getRootTaskId());
        }
    }

    @Test
    public void testLoadContexts() throws ClassNotFoundException, SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = dataSource.getConnection()) {
            final List<Context> contexts = sqliteDAO.load(c, SQLiteDAO.CONTEXT_DAO);
            assertEquals(2, contexts.size());

            final Context context1 = contexts.get(0);
            assertEquals("TEST-CONTEXT-1", context1.getId());
            assertTrue(context1.isActiveFlag());
            assertFalse(context1.getAllowsNextAction());
            assertEquals(date(2011, 11, 11), context1.getDateAdded());
            assertEquals(date(2005, 9, 9), context1.getDateModified());
            assertEquals("Test Context #1", context1.getName());
            assertEquals("TEST-PARENT-1", context1.getParentContextId());
            assertEquals(89, context1.getRank());

            final Context context2 = contexts.get(1);
            assertEquals("TEST-CONTEXT-2", context2.getId());
            assertFalse(context2.isActiveFlag());
            assertTrue(context2.getAllowsNextAction());
            assertEquals(date(2016, 4, 2), context2.getDateAdded());
            assertEquals(date(2017, 1, 1), context2.getDateModified());
            assertEquals("Test Context #2", context2.getName());
            assertEquals("TEST-PARENT-2", context2.getParentContextId());
            assertEquals(43, context2.getRank());
        }
    }


    private static Date date(int year, int month, int dayOfMonth) {
        final Instant instant = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

}
