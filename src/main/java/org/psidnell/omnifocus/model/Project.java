package org.psidnell.omnifocus.model;

import org.psidnell.omnifocus.osa.OSAIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Project extends Common {

    public static final String TYPE = "Project";

    @Override
    @JsonIgnore
    @OSAIgnore
    public String getType() {
        return TYPE;
    }
}
