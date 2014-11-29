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

import java.lang.reflect.Method;

/**
 * @author psidnell
 * 
 * Describes a property used in the SQLite object relational mapping.
 * 
 */
public class SQLITEPropertyDescriptor {

    private String name;
    private Class<?> type;
    private Method setter;

    public SQLITEPropertyDescriptor(String name, Method setter, Class<?> type) {
        this.name = name;
        this.setter = setter;
        this.type = type;
    }

    public Method getSetter() {
        return setter;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}