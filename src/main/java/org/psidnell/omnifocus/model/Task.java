package org.psidnell.omnifocus.model;

public class Task extends Common {

    public static final String TYPE = "Task";

    private String containingProject;
    private boolean next;
    private boolean blocked;

    public String getContainingProject() {
        return containingProject;
    }

    public void setContainingProject(String containingProject) {
        this.containingProject = containingProject;
    }

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
