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

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;
import org.psidnell.omnifocus.visitor.VisitorDescriptor;

// TODO - invalid at the moment
public class OPMLFormatter implements Formatter {

    private static final String INDENT = "  ";

    @Override
    public void format(Node root, Writer out) throws IOException {

        FormattingVisitor visitor = new FormattingVisitor(out);

        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.write("<opml version=\"2.0\">");
        out.write("<head>");
        out.write("    <title>" + root.getName() + "</title>");
        out.write("<head>");
        out.write("</body>");
        Traverser.traverse(visitor, root);
        out.write("</body>");
    }

    private static class FormattingVisitor implements Visitor {

        private static final VisitorDescriptor WHAT = new VisitorDescriptor().visitAll();

        private int depth = 0;
        private final Writer out;

        private FormattingVisitor(Writer out) {
            this.out = out;
        }

        @Override
        public VisitorDescriptor getWhat() {
            return WHAT;
        }

        @Override
        public void enter(Folder node) throws IOException {
            startNode(node.getName());
        }

        @Override
        public void exit(Folder node) throws IOException {
            endNode();
        }

        public void enter(Project node) throws IOException {
            startNode(node.getName());
        }

        @Override
        public void exit(Project node) throws IOException {
            endNode();
        }

        public void enter(Context node) throws IOException {
            startNode(node.getName());
        }

        @Override
        public void exit(Context node) throws IOException {
            endNode();
        }

        public void enter(Task node) throws IOException {
            startNode(node.getName());
        }

        @Override
        public void exit(Task node) throws IOException {
            endNode();
        }

        private void startNode(String name) throws IOException {
            out.write(indent(depth));
            out.write("<outline text=\"" + name + "\">\n");
            depth++;
        }

        private void endNode() throws IOException {
            depth--;
            out.write(indent(depth));
            out.write("</outline>\n");
        }

        String indent(int depth) {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                indent.append(INDENT);
            }
            return indent.toString();
        }
    }
}
