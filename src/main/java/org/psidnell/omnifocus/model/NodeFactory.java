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
package org.psidnell.omnifocus.model;

import org.psidnell.omnifocus.ConfigParams;

/**
 * @author psidnell
 *
 *         Creates properly initialised nodes (this can't fully be done with spring because they can
 *         be deserialised from jackson)
 */
public class NodeFactory {

    private ConfigParams config;

    @SuppressWarnings({"unchecked", "deprecation"})
    public <T> T create(String name, Class<T> clazz) {
        Node node;
        switch (clazz.getSimpleName()) {
            case "ProjectInfo":
                // Not really a node
                return (T) new ProjectInfo();
            case "Task":
                node = new Task();
                break;
            case "Project":
                node = new Project();
                break;
            case "Folder":
                node = new Folder();
                break;
            case "Context":
                node = new Context();
                break;
            default:
                throw new IllegalArgumentException("" + clazz);
        }

        initialise(node);

        return (T) node;
    }

    public Task createTask(String name) {
        @SuppressWarnings("deprecation")
        Task node = new Task();
        node.setName(name);
        initialise(node);
        return node;
    }

    public Folder createFolder(String name) {
        @SuppressWarnings("deprecation")
        Folder node = new Folder();
        node.setName(name);
        initialise(node);
        return node;
    }

    public Project createProject(String name) {
        @SuppressWarnings("deprecation")
        Project node = new Project();
        node.setName(name);
        initialise(node);
        return node;
    }

    public Project createProject(ProjectInfo pi, Task rootTask) {
        @SuppressWarnings("deprecation")
        Project node = new Project(pi, rootTask);
        initialise(node);
        return node;
    }

    public Context createContext(String name) {
        @SuppressWarnings("deprecation")
        Context node = new Context();
        node.setName(name);
        initialise(node);
        return node;
    }

    public void initialise(Node node) {
        node.setConfigParams(config);
    }

    public void setConfigParams(ConfigParams config) {
        this.config = config;
    }
}
