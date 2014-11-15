package org.psidnell.omnifocus.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Common extends Node {

    // Of the form: "2014-10-12T23:00:00.000Z"
    private static final DateTimeFormatter DATE_PARSER = ISODateTimeFormat.dateTime();

    private String context;
    private String note;
    private String deferDateString;
    private Date deferDate;
    private String dueDateString;
    private Date dueDate;
    private String completionDateString;
    private Date completionDate;
    private boolean completed;
    private boolean sequential;
    private boolean flagged;
    private List<Task> tasks = new LinkedList<>();

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDeferDate() {
        return deferDateString;
    }

    @JsonIgnore
    public Date getDeferDateAsDate() {
        return deferDate;
    }

    public void setDeferDate(String deferDate) {
        this.deferDateString = deferDate;
        this.deferDate = parseDate(deferDate);
    }

    public String getDueDate() {
        return dueDateString;
    }

    @JsonIgnore
    public Date getDueDateAsDate() {
        return dueDate;
    }

    public void setDueDate(String dueDateString) {
        this.dueDateString = dueDateString;
        this.dueDate = parseDate(dueDateString);
    }

    public String getCompletionDate() {
        return completionDateString;
    }

    @JsonIgnore
    public Date getCompletionDateAsDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDateString) {
        this.completionDateString = completionDateString;
        this.completionDate = parseDate(completionDateString);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

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
}
