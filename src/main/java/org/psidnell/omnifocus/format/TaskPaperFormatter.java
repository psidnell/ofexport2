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
package org.psidnell.omnifocus.format;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Document;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;

public class TaskPaperFormatter implements Formatter {

    private static final String INDENT = "\t";

    @Override
    public void format(Node root, Writer out) throws IOException {
        
        FormattingVisitor visitor = new FormattingVisitor(out);
        
        // The root node may not be interesting interesting
        if (root.getType().equals(Group.TYPE) && root.getName().equals("")) {
            Group group = (Group) root;
            for (Node child : group.getChildren()) {
                Traverser.traverse(visitor, child, false);
            }
        } else {
            Traverser.traverse(visitor, root, false);
        }
    }

    private static class FormattingVisitor implements Visitor {
        
        private int depth = 0;
        private final Writer out;
        
        private FormattingVisitor (Writer out) {
            this.out = out;
        }

        @Override
        public void enter(Folder node) throws IOException {
            out.write(indent(depth));
            out.write(tpProject(node.getName()));
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Folder node) {
            depth--;
        }
        
        @Override
        public void enter(Group node) throws IOException {
            out.write(indent(depth));
            out.write(tpProject(node.getName()));
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Group node) {
            depth--;
        }

        @Override
        public void enter(Document node) throws IOException {
            out.write(indent(depth));
            out.write(tpProject(node.getName()));
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Document node) {
            depth--;
        }
        
        @Override
        public void enter(Project node)
                throws IOException {
            out.write(indent(depth));
            out.write(tpProject(node.getName()));
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Project node) {
            depth--;
        }
    
        @Override
        public void enter(Context node)
                throws IOException {
            out.write(indent(depth));
            out.write(tpProject(node.getName()));
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Context node) {
            depth--;
        }
    
        @Override
        public void enter(Task node) throws IOException {
            out.write(indent(depth));
            out.write("- ");
            out.write(tpTask(node.getName()));
            if (node.getCompletionDate() != null) {
                out.write(' ');
                out.write(format(node.getCompletionDate()));
            }
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Task node) {
            depth--;
        }
    
        private String format(Date date) {
            // Produces a tag @2014-10-07-Tue
            return new SimpleDateFormat ("'@'yyyy'-'mm'-'dd'-'EEE").format(date);
        }
    
        String indent(int depth) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                indent.append(INDENT);
            }
            return indent.toString();
        }
        
        private String tpTask(String name) {
            while (name.endsWith(":")) {
                name = name.substring(0, name.length() - 1);
            }
            return name;
        }
        
        private String tpProject(String name) {
            if (!name.endsWith(":")) {
                name = name + ":";
            }
            return name;
        }
    }
}
