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
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;
import org.psidnell.omnifocus.visitor.VisitorDescriptor;

public class MarkdownFormatter implements Formatter {

    private static final String INDENT = "  ";

    @Override
    public void format(Node root, Writer out) throws IOException {
        
        FormattingVisitor visitor = new FormattingVisitor(out);
        
        Traverser.traverse(visitor, root);
    }

    private static class FormattingVisitor implements Visitor {
        
        private static final VisitorDescriptor WHAT = new VisitorDescriptor().visitAll();

        private int textDepth = 0;
        private int titleDepth = 1;
        private final Writer out;
        private boolean lastWasTitle = true;
        
        private FormattingVisitor (Writer out) {
            this.out = out;
        }

        @Override
        public VisitorDescriptor getWhat() {
            return WHAT;
        }
        
        @Override
        public void enter(Folder node) throws IOException {
            if (lastWasTitle == false) {
                out.write("\n");
            }
            out.write(title(titleDepth));
            out.write(node.getName());
            out.write("\n\n");
            lastWasTitle = true;
            titleDepth++;
        }
        
        @Override
        public void exit (Folder node) {
            titleDepth--;
        }
        
        @Override
        public void enter(Project node)
                throws IOException {
            if (lastWasTitle == false) {
                out.write("\n");
            }
            out.write(title(titleDepth));
            out.write(node.getName());
            out.write("\n\n");
            lastWasTitle = true;
            titleDepth++;
        }
        
        @Override
        public void exit (Project node) {
            titleDepth--;
        }
    
        @Override
        public void enter(Context node)
                throws IOException {
            if (lastWasTitle == false) {
                out.write("\n");
            }
            out.write(title(titleDepth));
            out.write(node.getName());
            out.write("\n\n");
            lastWasTitle = true;
            titleDepth++;
        }
        
        @Override
        public void exit (Context node) {
            titleDepth--;
        }
    
        @Override
        public void enter(Task node) throws IOException {
            out.write(indent(textDepth));
            out.write("- ");
            out.write(node.getName());
            out.write("\n");
            lastWasTitle = false;
            textDepth++;
        }
        
        @Override
        public void exit (Task node) {
            textDepth--;
        }
    
        String indent(int depth) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                indent.append(INDENT);
            }
            return indent.toString();
        }
        
        String title(int depth) {
            StringBuilder hashes = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                hashes.append('#');
            }
            return hashes.toString() + ' ';
        }
    }
}
