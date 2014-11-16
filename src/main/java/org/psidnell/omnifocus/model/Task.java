package org.psidnell.omnifocus.model;

import org.psidnell.omnifocus.osa.OSAAdaptation;
import org.psidnell.omnifocus.osa.OSAIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Task extends Common {

    public static final String TYPE = "Task";

    private String containingProject;
    private boolean next;
    private boolean blocked;

    @OSAAdaptation(pattern="nameOf(o.%s())")
    public String getContainingProject() {
        return containingProject;
    }

    public void setContainingProject(String containingProject) {
        this.containingProject = containingProject;
    }

    @Override
    @JsonIgnore
    @OSAIgnore
    public String getType() {
        return TYPE;
    }

    public boolean getNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
