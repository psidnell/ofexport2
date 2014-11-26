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

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class CommonProjectTask extends Node {

    protected Context context;
    private String note;
    private Date deferDate;
    private Date dueDate;
    private Date completionDate;
    private boolean sequential = false;
    private boolean flagged = false;
    protected List<Task> tasks = new LinkedList<>();

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public abstract boolean isAvailable ();
    
    public abstract boolean isRemaining ();
    
    public void setAvailable (boolean ignored) {
        // Dummy setter for derived value since
        // we want the exported/imported value in the json/xml
    }
    
    public void setRemaining (boolean ignored) {
        // Dummy setter for derived value since
        // we want the exported/imported value in the json/xml
    }
    
    @ExprAttribute(help="number of tasks.")
    @JsonIgnore
    public int getTaskCount () {
        return tasks.size();
    }
    
    @ExprAttribute(help="the sub tasks")
    public List<Task> getTasks() {
        return tasks;
    }
    
    @JsonIgnore
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    
    @ExprAttribute (help="note text.")
    @SQLiteProperty (name="plainTextNote")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @SQLiteProperty(name="dateToStart")
    @ExprAttribute (help="date item is to start.")
    public Date getDeferDate() {
        return deferDate;
    }

    public void setDeferDate(Date deferDate) {
        this.deferDate = roundToDay(deferDate);
    }

    @SQLiteProperty(name="dateDue")
    @ExprAttribute (help="date item is due.")
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = roundToDay(dueDate);
    }

    @SQLiteProperty(name="dateCompleted")
    @ExprAttribute (help="date item was completed.")
    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = roundToDay(completionDate);
    }

    @JsonIgnore
    @ExprAttribute (help="item is complete.")
    public boolean isCompleted() {
        return completionDate != null;
    }

    @SQLiteProperty
    @ExprAttribute (help="item is sequential.")
    public boolean isSequential() {
        return sequential;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    @SQLiteProperty
    @ExprAttribute (help="item is flagged.")
    public boolean isFlagged() {
        return flagged;
    }

    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }
    
    @Override
    @JsonIgnore
    public List<Node> getContextPath() {
        return getContextPath(context);
    }
}
