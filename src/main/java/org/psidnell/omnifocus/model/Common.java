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

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.psidnell.omnifocus.sqlite.SQLiteProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Common extends Node {

    // Of the form: "2014-10-12T23:00:00.000Z"
    private static final DateTimeFormatter DATE_PARSER = ISODateTimeFormat.dateTime();

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
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @SQLiteProperty(name="dateToStart")
    public Date getDeferDate() {
        return deferDate;
    }

    public void setDeferDate(Date deferDate) {
        this.deferDate = deferDate;
    }

    @SQLiteProperty(name="dateDue")
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @SQLiteProperty(name="dateCompleted")
    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    @JsonIgnore
    public boolean isCompleted() {
        return completionDate != null;
    }

    @SQLiteProperty
    public boolean getSequential() {
        return sequential;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean getFlagged() {
        return flagged;
    }

    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }

    protected Date parseDate(String stringForm) {
        Date result = null;
        if (stringForm != null) {
            result = DATE_PARSER.parseDateTime(stringForm).toDate();
        }
        return result;
    }
    
    @Override
    public List<Node> getContextPath() {
        return getContextPath(context);
    }
}
