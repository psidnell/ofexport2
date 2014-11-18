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
import java.util.Collection;
import java.util.LinkedList;

import org.psidnell.omnifocus.model.Task;

public class SQLiteClassDescriptor<T> {
    private String columnsForSelect;
    private LinkedList<SQLITEPropertyDescriptor> properties = new LinkedList<>();
    private Class<T> clazz;
    private String tableName;

    public SQLiteClassDescriptor (Class<T> clazz, String tableName) throws NoSuchMethodException, SecurityException
    {
        this.clazz = clazz;
        this.tableName = tableName;
        
        StringBuilder columns = new StringBuilder();
        
        for (Method m : clazz.getMethods()) {
            SQLiteProperty p = m.getAnnotation(SQLiteProperty.class);
            if (p != null) {
                String propName = p.name();
                if (propName.length() == 0) {
                    propName = Character.toLowerCase(m.getName().charAt(3)) +  m.getName().substring(4);
                }
                
                String setterName = m.getName().replaceFirst("get", "set");
                Method setter = clazz.getMethod(setterName, m.getReturnType());
                columns.append(columns.length() != 0 ? ',' : "");
                
                columns.append(propName);
                SQLITEPropertyDescriptor pd = new SQLITEPropertyDescriptor(propName, setter, m.getReturnType());
                properties.add(pd);
            }
        }
        columnsForSelect = columns.toString();
    }

    public Collection<T> load(ResultSet rs) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        LinkedList<T> tasks = new LinkedList<>();
        while (rs.next()) {
            T instance = clazz.newInstance();
            for (SQLITEPropertyDescriptor desc : properties) {
                Object rawValue = rs.getObject(desc.getName());
                // TODO type conversion
                desc.getSetter().invoke(instance, rawValue);
            }
            tasks.add (instance);
        }
        return tasks;
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