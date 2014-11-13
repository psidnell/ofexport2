package org.psidnell.omnifocus.filter;

import org.psidnell.omnifocus.model.Node;

public interface Filter {
    public Node filter(Node root);
}
