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

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
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
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.ProjectInfo;
import org.psidnell.omnifocus.model.Task;

public class SQLiteDAO {
    
    public static String DB = "/Users/psidnell/Library/Containers/com.omnigroup.OmniFocus2/Data/Library/Caches/com.omnigroup.OmniFocus2/OmniFocusDatabase2"; // TODO spring?

    public static String getDriverURL () {
        return "jdbc:sqlite:" + DB;
    }
    
    public static final SQLiteClassDescriptor<Task>TASK_DAO;
    public static final SQLiteClassDescriptor<ProjectInfo>PROJECT_INFO_DAO;
    public static final SQLiteClassDescriptor<Folder>FOLDER_DAO;
    public static final SQLiteClassDescriptor<Context>CONTEXT_DAO;
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");

            TASK_DAO = new SQLiteClassDescriptor<Task>(Task.class, "Task");
            PROJECT_INFO_DAO = new SQLiteClassDescriptor<ProjectInfo>(ProjectInfo.class, "ProjectInfo");
            FOLDER_DAO = new SQLiteClassDescriptor<Folder>(Folder.class, "Folder");
            CONTEXT_DAO = new SQLiteClassDescriptor<Context>(Context.class, "Context");
            
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            throw new IllegalStateException (e);
        }
    }

    public static Connection getConnection () throws SQLException {
        return DriverManager.getConnection(getDriverURL());
    }
    
    public static DataCache load () throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Connection c = getConnection()) {
            Collection<ProjectInfo> projInfos = load(c, PROJECT_INFO_DAO, null);
            Collection<Folder> folders = load(c, FOLDER_DAO, null);
            Collection<Task> tasks = load(c, TASK_DAO, null);
            Collection<Context> contexts = load(c, CONTEXT_DAO, null);
            return new DataCache(folders, projInfos, tasks, contexts);
        }
    }
    
    public static <T> Collection<T> load (Connection c, SQLiteClassDescriptor<T> desc, String whereClause) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        try (Statement stmt = c.createStatement()) {
            String optWhere = whereClause == null ? "" : " " + whereClause;
            try (ResultSet rs = stmt.executeQuery("select "+ desc.getColumnsForSelect() + " from " + desc.getTableName() + optWhere)) {
                return desc.load(rs);
            }
        }
    }
    
    public static void printTables(Connection c) throws SQLException {
        LinkedList<String> tableNames = getTableNames(c);
        for (String tableName : tableNames) {
            getColumnData(c, tableName);
        }
    }

    private static Map<String, String> getColumnData(Connection c, String tableName) throws SQLException {
        try (Statement stmt = c.createStatement()) {
            HashMap<String, String> data = new HashMap<>();
            stmt.setFetchSize(1);
            stmt.setMaxRows(1);
            System.out.println(tableName + ":");
            try (ResultSet rs = stmt.executeQuery("select * from " + tableName)) {
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

    private static LinkedList<String> getTableNames(Connection c) throws SQLException {
        LinkedList<String> tableNames = new LinkedList<>();
        DatabaseMetaData md = c.getMetaData();

        try (ResultSet rs = md.getTables(null, null, "%", null)) {
            while (rs.next()) {
                String tableName = rs.getString(3);
                System.out.println(tableName);
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }
}
