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
package org.psidnell.omnifocus.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author psidnell
 *
 *         Simple bean for serializing the raw DB data
 */
public class RawData {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private LinkedList<ProjectInfo> projects;
    private LinkedList<Folder> folders;
    private LinkedList<Task> tasks;
    private LinkedList<Context> contexts;

    public static RawData importRawData(File file) throws IOException {
        try (
            Reader in = new FileReader(file)) {
            return OBJECT_MAPPER.readValue(in, RawData.class);
        }
    }

    public static void exportRawData(RawData rawData, File file) throws IOException {

        try (
            Writer out = new FileWriter(file)) {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(out, rawData);
        }
    }

    public void setProjects(LinkedList<ProjectInfo> projects) {
        this.projects = projects;
    }

    public LinkedList<ProjectInfo> getProjects() {
        return projects;
    }

    public void setFolders(LinkedList<Folder> folders) {
        this.folders = folders;
    }

    public LinkedList<Folder> getFolders() {
        return folders;
    }

    public void setTasks(LinkedList<Task> tasks) {
        this.tasks = tasks;
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    public void setContexts(LinkedList<Context> contexts) {
        this.contexts = contexts;
    }

    public LinkedList<Context> getContexts() {
        return contexts;
    }
}
