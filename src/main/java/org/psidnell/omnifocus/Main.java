package org.psidnell.omnifocus;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.script.ScriptException;

import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.format.SimpleTextListFormatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Project;

public class Main {

    public static void main (String args[]) throws IOException, ScriptException {
        OmniFocus of = new OmniFocus();
       
        Group root = new Group ();
        root.setName("");
        
        String filter = null; //"{flagged: true}";
        Availability availability = Availability.Remaining;
        
        for (String arg : args) {
            if (arg.startsWith("p:")) {
                String projectName = arg.substring(2);
                for (Project p : of.getProjectsByName(projectName)) {
                    of.loadTasks(p, availability, filter);
                    root.addChild(p);
                }
            }
            else if (arg.startsWith("c:")) {
                String contextName = arg.substring(2);
                for (Context c : of.getContextsByName(contextName)) {
                    of.loadTasks(c, availability, filter);
                    root.addChild(c);
                }
            }
        }
        
        Formatter formatter = new SimpleTextListFormatter();
        Writer out = new OutputStreamWriter(System.out);
        formatter.format(root, out);
        out.close();
    }
}
