package org.psidnell.omnifocus;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.script.ScriptException;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.psidnell.omnifocus.cli.ActiveOption;
import org.psidnell.omnifocus.cli.ActiveOptionProcessor;
import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.format.SimpleTextListFormatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class Main {
    
    final static Options OPTIONS = new Options();
    static {
        OPTIONS.addOption(new ActiveOption<Main>(
                "c", true, "Load tasks from context specified by name",
                (m,o)->m.processContextName(o)));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "p", true, "Load tasks from project specified by name",
                (m,o)->m.processProjectName(o)));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "i", false, "Load tasks from the inbox",
                (m,o)->m.processInbox()));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "h", false, "print help",
                (m,o)->m.printHelp ()));
    }

    public static final String PROG = "ofexport2";
    
    private final OmniFocus of;
    private final Group root;
    private String filter = null;
    private Availability availability = Availability.Remaining;
    private ActiveOptionProcessor<Main> processor;
        
    public Main(ActiveOptionProcessor<Main> processor) throws IOException {
        this.processor = processor;
        of = new OmniFocus();

        root = new Group();
        root.setName("");

        filter = null;
    }

    private void printHelp() {
       processor.printHelp ();
    }

    private void processProjectName(ActiveOption<Main> o) throws IOException, ScriptException {
        String projectName = o.nextValue();
        for (Project p : of.getProjectsByName(projectName)) {
            of.loadTasks(p, availability, filter);
            root.addChild(p);
        }
    }

    private void processContextName(ActiveOption<Main> o) throws IOException, ScriptException {
        String contextName = o.nextValue();
        for (Context c : of.getContextsByName(contextName)) {
            of.loadTasks(c, availability, filter);
            root.addChild(c);
        }
    }
    
    private void processInbox() throws IOException, ScriptException {
        Group inbox = new Group();
        inbox.setName("Inbox");
        for (Task t : of.loadAllInboxTasks(filter)) {
            inbox.addChild(t);
        }
        root.addChild(inbox);
    }
    
    private void run () throws IOException {

        Formatter formatter = new SimpleTextListFormatter();
        Writer out = new OutputStreamWriter(System.out);
        formatter.format(root, out);
        out.close();
    }
    
    public static void main(String args[]) throws Exception {
        
        ActiveOptionProcessor<Main> processor = new ActiveOptionProcessor<>(PROG, OPTIONS);
        
        Main main = new Main (processor);
        
        if (!processor.processOptions(main, args)) {
            return;
        }

        main.run ();
    }
    

}
