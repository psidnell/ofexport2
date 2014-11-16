package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

import org.psidnell.omnifocus.osa.OSAIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Group extends Node {

    public static final String TYPE = "Group";

    private List<Node> children = new LinkedList<>();

    public List<? extends Node> getChildren() {
        return children;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    @Override
    @JsonIgnore
    @OSAIgnore
    public String getType() {
        return TYPE;
    }
}
