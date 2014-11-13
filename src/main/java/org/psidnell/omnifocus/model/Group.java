package org.psidnell.omnifocus.model;

import java.util.LinkedList;
import java.util.List;

public class Group extends Node {
    
    public static final String TYPE = "Group";
    
    private List<Node> children = new LinkedList<> ();
    
    public List<? extends Node> getChildren () {
        return children;
    }
    
    public void addChild (Node node) {
        children.add(node);
    }
    
    public String getType () {
        return TYPE;
    }
}
