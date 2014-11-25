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

import org.psidnell.omnifocus.sqlite.SQLiteProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProjectInfo {

    public String task;
    private String folderId;
    private Status status;
    
    
    @SQLiteProperty (name="status")
    public String getStatusString () {
        return status.toString();
    }
    
    public void setStatusString (String status) {
        switch (status.toLowerCase()) {
            case "active":
                this.status = Status.Active;
                break;
            case "inactive":
                this.status = Status.Inactive;
                break;
            case "done":
                this.status = Status.Done;
                break;
            default:
                throw new IllegalArgumentException ("Unexpected status: " + status);
        }
    }
    
    @SQLiteProperty(name="folder")
    public String getFolderId () {
        return folderId;
    }
    
    public void setFolderId (String folderId) {
        this.folderId = folderId;
    }
    
    @SQLiteProperty(name="task")
    public String getRootTaskId () {
        return task;
    }
    
    public void setRootTaskId (String task) {
        this.task = task;
    }

    @JsonIgnore
    public Status getStatus() {
        return status;
    }
}
