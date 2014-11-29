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
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * @author psidnell
 *
 * @param <T>
 * 
 * Describes a class used for storing data extracted from SQLite.
 * 
 * The class is searched for annotated getter methods to determine
 * the mapping.
 */
public class SQLiteClassDescriptor<T> {
    private String columnsForSelect;
    private LinkedList<SQLITEPropertyDescriptor> properties = new LinkedList<>();
    private Class<T> clazz;
    private String tableName;

    public SQLiteClassDescriptor(Class<T> clazz, String tableName) throws NoSuchMethodException, SecurityException {
        this.clazz = clazz;
        this.tableName = tableName;

        StringBuilder columns = new StringBuilder();

        for (Method m : clazz.getMethods()) {
            SQLiteProperty p = m.getAnnotation(SQLiteProperty.class);
            if (p != null) {
                String methodName = m.getName();
                String setterName;
                String propName;
                if (methodName.startsWith("is")) {
                    propName = Character.toLowerCase(m.getName().charAt(2)) + m.getName().substring(3);
                    setterName = m.getName().replaceFirst("^is", "set");
                } else if (methodName.startsWith("get")) {
                    propName = Character.toLowerCase(m.getName().charAt(3)) + m.getName().substring(4);
                    setterName = m.getName().replaceFirst("^get", "set");
                } else {
                    throw new IllegalArgumentException("bad property accessor: " + m);
                }

                if (p.name().length() != 0) {
                    propName = p.name();
                }

                Method setter = clazz.getMethod(setterName, m.getReturnType());
                columns.append(columns.length() != 0 ? ',' : "");
                columns.append(propName);

                SQLITEPropertyDescriptor pd = new SQLITEPropertyDescriptor(propName, setter, m.getReturnType());
                properties.add(pd);
            }
        }
        columnsForSelect = columns.toString();
    }

    public Collection<T> load(ResultSet rs) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {
        LinkedList<T> tasks = new LinkedList<>();
        while (rs.next()) {
            T instance = clazz.newInstance();
            for (SQLITEPropertyDescriptor desc : properties) {
                Object rawValue = getValue(rs, desc);
                Object value = rawValue;
                try {
                    if (rawValue != null) {
                        switch (desc.getType().getSimpleName()) {
                            case "Boolean":
                            case "boolean":
                                value = 0 != (Integer) rawValue;
                                break;
                        }
                    }
                    desc.getSetter().invoke(instance, value);
                } catch (IllegalArgumentException e) {
                    String valTypeName = rawValue == null ? "null" : rawValue.getClass().getSimpleName();
                    throw new IllegalArgumentException("mismatch " + desc.getType().getSimpleName() + " and " + valTypeName, e);
                }
            }
            tasks.add(instance);
        }
        return tasks;
    }

    private Object getValue(ResultSet rs, SQLITEPropertyDescriptor desc) throws SQLException {
        Object rawValue;
        switch (desc.getType().getSimpleName()) {
            case "String":
                rawValue = rs.getString(desc.getName());
                break;
            case "Date":
                // I've deduced what's going on by experimentation.
                // Weird: Seems to be seconds since 2001 but comes in either
                // as a Double or as an Integer.
                rawValue = rs.getObject(desc.getName());
                if (rawValue == null) {
                    rawValue = null;
                } else {
                    int secondsSince2001;
                    if (rawValue instanceof Integer) {
                        secondsSince2001 = (Integer) rawValue;
                    } else {
                        secondsSince2001 = ((Double) rawValue).intValue();
                    }

                    Calendar cal = new GregorianCalendar();
                    cal.set(Calendar.YEAR, 2001);
                    cal.set(Calendar.MONTH, Calendar.JANUARY);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, secondsSince2001);
                    cal.set(Calendar.MILLISECOND, 0);
                    rawValue = cal.getTime();
                }
                break;
            default:
                rawValue = rs.getObject(desc.getName());
                break;
        }
        return rawValue;
    }

    Collection<SQLITEPropertyDescriptor> getProperties() {
        return properties;
    }

    public String getColumnsForSelect() {
        return columnsForSelect;
    }

    public String getTableName() {
        return tableName;
    }
}