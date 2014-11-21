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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Project extends Common {

    public static final String TYPE = "Project";
    private Folder folder;
    private Status status;

    public Project () {
    }
    
    public Project (ProjectInfo projInfo, Task rootTask) {
        setId(rootTask.getId ());
        setName(rootTask.getName());
        status = projInfo.getStatus ();
        getTasks().addAll(rootTask.getTasks());
    }
    
    @Override
    @JsonIgnore
    public String getType() {
        return TYPE;
    }

    @Override
    public List<Node> getProjectPath() {
        return getProjectPath(folder);
    }

    @JsonIgnore
    public Folder getFolder () {
        return folder;
    }
    
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Status getStatus() {
        return status;
    }

    public void add(Task child) {
        tasks.add(child);
        child.setProject(this);
        child.setParent(null);
    }
}
