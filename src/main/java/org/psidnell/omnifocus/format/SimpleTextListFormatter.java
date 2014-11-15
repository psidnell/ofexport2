package org.psidnell.omnifocus.format;

import java.io.IOException;
import java.io.Writer;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;

public class SimpleTextListFormatter implements Formatter {

    private static final String INDENT = "  ";
    
    @Override
    public void format(Node root, Writer out) throws IOException {
        
        FormattingVisitor visitor = new FormattingVisitor(out);
        
        // The root node may not be interesting interesting
        if (root.getType().equals(Group.TYPE) && root.getName().equals("")) {
            Group group = (Group) root;
            for (Node child : group.getChildren()) {
                Traverser.traverse(visitor, child);
            }
        } else {
            Traverser.traverse(visitor, root);
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
            out.write(node.getName());
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
            out.write(node.getName());
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Group node) {
            depth--;
        }
    
        public void enter(Project node)
                throws IOException {
            out.write(indent(depth));
            out.write(node.getName());
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Project node) {
            depth--;
        }
    
        public void enter(Context node)
                throws IOException {
            out.write(indent(depth));
            out.write(node.getName());
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Context node) {
            depth--;
        }
    
        public void enter(Task node) throws IOException {
            out.write(indent(depth));
            out.write("- ");
            out.write(node.getName());
            out.write("\n");
            depth++;
        }
        
        @Override
        public void exit (Task node) {
            depth--;
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
