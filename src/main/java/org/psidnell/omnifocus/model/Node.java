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

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.psidnell.omnifocus.expr.ExprAttribute;
import org.psidnell.omnifocus.expr.ExpressionFunctions;
import org.psidnell.omnifocus.sqlite.SQLiteProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author psidnell
 *
 *         The root class for all nodes in the object tree.
 */
public abstract class Node extends ExpressionFunctions {

    protected String name;

    private String id = UUID.randomUUID().toString();

    private int rank;

    private boolean isRoot;

    private boolean marked = false;

    @SQLiteProperty
    @ExprAttribute(help = "item name/text.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SQLiteProperty(name = "persistentIdentifier")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SQLiteProperty
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @JsonIgnore
    public abstract String getType();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public abstract List<Node> getProjectPath();

    public abstract List<Node> getContextPath();

    @JsonIgnore
    protected List<Node> getProjectPath(Node parent) {
        List<Node> path;
        if (parent != null) {
            path = parent.getProjectPath();
        } else {
            path = new LinkedList<>();
        }
        path.add(this);
        return path;
    }

    @JsonIgnore
    protected List<Node> getContextPath(Node parent) {
        List<Node> path;
        if (parent != null) {
            path = parent.getContextPath();
        } else {
            path = new LinkedList<>();
        }
        path.add(this);
        return path;
    }

    @JsonIgnore
    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getType() + ":'" + name + "'";
    }

    @JsonIgnore
    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public abstract void cascadeMarked();

    public abstract boolean isAvailable();
}
