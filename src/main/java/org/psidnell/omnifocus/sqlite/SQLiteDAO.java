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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.NodeFactory;
import org.psidnell.omnifocus.model.ProjectInfo;
import org.psidnell.omnifocus.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author psidnell
 *
 * The main SQLite data access object.
 */
public class SQLiteDAO implements BeanFactoryAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDAO.class);

    private static final int THREE = 3;
    private String[] possibleDBLocations;

    private NodeFactory nodeFactory;

    private BeanFactory beanFactory;

    public String getDriverURL() throws SQLException {
        for (String location : possibleDBLocations) {
            File file = new File(location);
            LOGGER.info("Checking database file: {}", file);
            if (file.exists() && file.isFile()) {
                LOGGER.info("Found database file: {}", file);
                return "jdbc:sqlite:" + location;
            }
        }
        throw new SQLException("Unable to find the OmniFocus SQLite database in any configured locations");
    }

    public static final SQLiteClassDescriptor<Task> TASK_DAO;
    public static final SQLiteClassDescriptor<ProjectInfo> PROJECT_INFO_DAO;
    public static final SQLiteClassDescriptor<Folder> FOLDER_DAO;
    public static final SQLiteClassDescriptor<Context> CONTEXT_DAO;

    static {
        try {
            Class.forName("org.sqlite.JDBC");

            TASK_DAO = new SQLiteClassDescriptor<Task>(Task.class, "Task");
            PROJECT_INFO_DAO = new SQLiteClassDescriptor<ProjectInfo>(ProjectInfo.class, "ProjectInfo");
            FOLDER_DAO = new SQLiteClassDescriptor<Folder>(Folder.class, "Folder");
            CONTEXT_DAO = new SQLiteClassDescriptor<Context>(Context.class, "Context");

        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        // Failed attempts at specifying charset so non-ascii notes load correctly
        // Properties props = new Properties();
        // props.put("characterEncoding", "UTF-8");
        // props.put("useUnicode", "true");
        return DriverManager.getConnection(getDriverURL());
    }

    public DataCache load() throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {

        LOGGER.info("Starting DB load");

        try (
            Connection c = getConnection()) {
            Collection<ProjectInfo> projInfos = load(c, PROJECT_INFO_DAO);
            Collection<Folder> folders = load(c, FOLDER_DAO);
            Collection<Task> tasks = load(c, TASK_DAO);
            Collection<Context> contexts = load(c, CONTEXT_DAO);

            LOGGER.info("Loaded {} folders", folders.size());
            LOGGER.info("Loaded {} projects", projInfos.size());
            LOGGER.info("Loaded {} contexts", contexts.size());
            LOGGER.info("Loaded {} tasks", tasks.size());

            return new DataCache(folders, projInfos, tasks, contexts, beanFactory);
        }
    }

    public <T> Collection<T> load(Connection c, SQLiteClassDescriptor<T> desc) throws SQLException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        try (
            PreparedStatement stmt = c.prepareStatement("select " + desc.getColumnsForSelect() + " from " + desc.getTableName())) {
            try (
                ResultSet rs = stmt.executeQuery()) {
                return desc.load(rs, nodeFactory);
            }
        }
    }

    public void printTables(Connection c) throws SQLException {
        LinkedList<String> tableNames = getTableNames(c);
        for (String tableName : tableNames) {
            getColumnData(c, tableName);
        }
    }

    private Map<String, String> getColumnData(Connection c, String tableName) throws SQLException {
        try (
            Statement stmt = c.createStatement()) {
            HashMap<String, String> data = new HashMap<>();
            stmt.setFetchSize(1);
            stmt.setMaxRows(1);
            System.out.println(tableName + ":");
            try (
                ResultSet rs = stmt.executeQuery("select * from " + tableName)) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnType = rsmd.getColumnTypeName(i);
                    data.put(columnName, columnType);
                    System.out.println("    " + columnName + ":" + columnType);
                }
            }
            return data;
        }
    }

    private LinkedList<String> getTableNames(Connection c) throws SQLException {
        LinkedList<String> tableNames = new LinkedList<>();
        DatabaseMetaData md = c.getMetaData();

        try (
            ResultSet rs = md.getTables(null, null, "%", null)) {
            while (rs.next()) {
                String tableName = rs.getString(THREE);
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }

    public void setPossibleDBLocations(String[] possibleDBLocations) {
        this.possibleDBLocations = possibleDBLocations;
    }

    public void setNodeFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
