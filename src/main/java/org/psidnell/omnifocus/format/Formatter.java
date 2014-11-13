package org.psidnell.omnifocus.format;

import java.io.IOException;
import java.io.Writer;

import org.psidnell.omnifocus.model.Node;

public interface Formatter {
    public void format (Node root, Writer out) throws IOException;
  
}
