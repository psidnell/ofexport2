package org.psidnell.omnifocus.format;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class TaskPaperFormatter implements Formatter {

    private static final String INDENT = "\t";

    @Override
    public void format(Node root, Writer out) throws IOException {
        // The root node may not be interesting interesting
        if (root.getType().equals(Group.TYPE) && root.getName().equals("")) {
            Group group = (Group) root;
            for (Node child : group.getChildren()) {
                doFormat(child, out, 0);
            }
        } else {
            doFormat(root, out, 0);
        }
    }

    private void doFormat(Node node, Writer out, int depth) throws IOException {
        switch (node.getType()) {
        case Group.TYPE:
            doFormat((Group) node, out, depth);
            break;
        case Project.TYPE:
            doFormat((Project) node, out, depth);
            break;
        case Context.TYPE:
            doFormat((Context) node, out, depth);
            break;
        case Task.TYPE:
            doFormat((Task) node, out, depth);
            break;
        }
    }

    private void doFormat(Group node, Writer out, int depth) throws IOException {
        out.write(indent(depth));
        out.write(tpProject(node.getName()));
        out.write("\n");
        for (Node child : node.getChildren()) {
            doFormat(child, out, depth + 1);
        }
    }

    private void doFormat(Project node, Writer out, int depth)
            throws IOException {
        out.write(indent(depth));
        out.write(tpProject(node.getName()));
        out.write("\n");
        for (Task child : node.getTasks()) {
            doFormat(child, out, depth + 1);
        }
    }

    private void doFormat(Context node, Writer out, int depth)
            throws IOException {
        out.write(indent(depth));
        out.write(tpProject(node.getName()));
        out.write("\n");
        for (Task child : node.getTasks()) {
            doFormat(child, out, depth + 1);
        }
    }

    private void doFormat(Task node, Writer out, int depth) throws IOException {
        out.write(indent(depth));
        out.write("- ");
        out.write(tpTask(node.getName()));
        if (node.getCompletionDate() != null) {
            out.write (" ");
            out.write (format (node.getCompletionDateAsDate()));
        }
        
        out.write("\n");
    }

    private String format(Date date) {
        //@2014-10-07-Tue
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
