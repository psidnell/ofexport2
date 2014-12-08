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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.psidnell.omnifocus.expr.ExprAttribute;
import org.psidnell.omnifocus.sqlite.SQLiteProperty;
import org.psidnell.omnifocus.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author psidnell
 *
 * Represents the attributes shared between a Project and a Task.
 *
 */
public abstract class CommonProjectAndTaskAttributes extends NodeImpl implements ProjectHierarchyNode, ContextHierarchyNode{

    protected Context context;
    private String note;
    private Date deferDate;
    private Date dueDate;
    protected Date completionDate;
    private boolean sequential = false;
    private boolean flagged = false;
    protected int estimatedMinutes = -1;
    protected ProjectHierarchyNode parent;
    protected List<Task> tasks = new LinkedList<>();

    @ExprAttribute(help = "number of tasks.")
    @JsonIgnore
    public int getTaskCount() {
        return tasks.size();
    }

    @ExprAttribute(help = "number of uncompleted tasks.")
    @JsonIgnore
    public int getUncompletedTaskCount() {
        int count = 0;
        for (Task child : tasks) {
            if (!child.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    @ExprAttribute(help = "the sub tasks.")
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public abstract boolean isRemaining();

    public abstract void setRemaining(boolean remaining);

    @ExprAttribute(help = "the context name or null.")
    public String getContextName() {
        return context == null ? null : context.getName();
    }

    public void setContextName (String dummy) {
        // Keep Jackson happy.
    }

    @JsonIgnore
    @Override
    public Context getContextModeParent() {
        return context;
    }

    @Override
    public void setContextModeParent(Context context) {
        this.context = context;
    }

    @ExprAttribute(help = "note text.")
    @SQLiteProperty(name = "plainTextNote")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @SQLiteProperty(name = "effectiveDateToStart")
    @ExprAttribute(help = "date item is to start or null.")
    public Date getDeferDate() {
        return deferDate;
    }

    public void setDeferDate(Date deferDate) {
        this.deferDate = roundToDay(deferDate);
    }

    @SQLiteProperty(name = "effectiveDateDue")
    @ExprAttribute(help = "date item is due or null.")
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = roundToDay(dueDate);
    }

    @SQLiteProperty(name = "dateCompleted")
    @ExprAttribute(help = "date item was completed or null.")
    public Date getCompletionDate() {
        // We don't have an "effective" completion date, a parent item
        // might be completed but we'd still have a null completionDate.
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = roundToDay(completionDate);
    }

    @SQLiteProperty
    @ExprAttribute(help = "item is sequential.")
    public boolean isSequential() {
        return sequential;
    }

    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }

    @SQLiteProperty (name="effectiveFlagged")
    @ExprAttribute(help = "item is flagged.")
    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    @JsonIgnore
    @ExprAttribute(help = "item is not flagged.")
    public boolean isUnflagged () {
        return !flagged;
    }

    @SQLiteProperty
    @ExprAttribute(help="estimated minutes.")
    public Integer getEstimatedMinutes () {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes (Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes == null ? -1 : estimatedMinutes;
    }

    @Override
    @JsonIgnore
    public List<ContextHierarchyNode> getContextPath() {
        return getContextPath(context);
    }

    public String formatNote(int depth, String indent) {
        return formatNote(depth, indent, "");
    }

    public String formatNote(int depth, String indent, String lineSuffix) {
        // TODO This isn't ideal, don't know what encoding is coming in from
        // SQLite, but email attachments in particular seem full of accented
        // chars that arn't really there. This is clearly not going to support
        // wide characters as is.

        StringBuilder ascii = new StringBuilder();
        for (byte byt : note.getBytes()) {
            int c = 0xFF & byt;
            if (c >= '\t' && c <= '~') {
                ascii.append((char) c);
            }
        }

        String[] lines = ascii.toString().replaceAll("\r", "").split("\n");
        String eol = lineSuffix + "\n";
        String indentChars = StringUtils.times(indent, depth);
        String delimiter = eol + indentChars;
        return StringUtils.join(lines, delimiter, indentChars, eol);
    }


    @Override
    public void cascadeMarked() {
        setMarked(true);
        tasks.stream().forEach((t) -> t.cascadeMarked());
    }

    public void setDueSoon (boolean dummy) {
        // To satisfy Jackson
    }

    @Override
    @JsonIgnore
    public ProjectHierarchyNode getProjectModeParent() {
        return parent;
    }

    @Override
    public void setProjectModeParent(ProjectHierarchyNode node) {
        this.parent = node;
    }

    public abstract boolean isCompleted ();

    @ExprAttribute(help="completion date.")
    @JsonIgnore
    public org.psidnell.omnifocus.expr.Date getCompletion () {
        return new org.psidnell.omnifocus.expr.Date(completionDate, config);
    }

    @ExprAttribute(help="defer date.")
    @JsonIgnore
    public org.psidnell.omnifocus.expr.Date getDefer () {
        return new org.psidnell.omnifocus.expr.Date(deferDate, config);
    }

    @ExprAttribute(help="due date.")
    @JsonIgnore
    public org.psidnell.omnifocus.expr.Date getDue () {
        return new org.psidnell.omnifocus.expr.Date(dueDate, config);
    }
}
