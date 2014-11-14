package org.psidnell.omnifocus;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

import javax.script.ScriptException;

import org.apache.commons.cli.Options;
import org.psidnell.omnifocus.cli.ActiveOption;
import org.psidnell.omnifocus.cli.ActiveOptionProcessor;
import org.psidnell.omnifocus.filter.Filter;
import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public class Main {
    
    final static Options OPTIONS = new Options();
    static {
        OPTIONS.addOption(new ActiveOption<Main> (
                "h", "help", false, "print help",
                (m,o)->m.printHelp ()));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "c", "contextname", true, "Load tasks from context specified by name",
                (m,o)->m.processContextName(o)));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "p", "projectname", true, "Load tasks from project specified by name",
                (m,o)->m.processProjectName(o)));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "i", "inbox", false, "Load tasks from the inbox",
                (m,o)->m.processInbox()));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "f", "flagged", true, "flagged (true|false)",
                (m,o)->m.processFlagged (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "a", "availability", true, "availability (" + Arrays.asList(Availability.values()) + ")".replaceAll(",", "|"),
                (m,o)->m.processAvailability (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "e", "expr", true, "expression",
                (m,o)->m.processExpression (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "f", "format", true, "format",
                (m,o)->m.processFormat (o)));
    }

    public static final String PROG = "ofexport2";
    
    private final OmniFocus of;
    private final Group root;
    private String filter = null;
    private Availability availability = Availability.Available;
    private ActiveOptionProcessor<Main> processor;
    private String format = "SimpleTextList";
        
    public Main(ActiveOptionProcessor<Main> processor) throws IOException {
        this.processor = processor;
        of = new OmniFocus();

        root = new Group();
        root.setName("");

        filter = null;
    }

    private void processFormat(ActiveOption<Main> o) {
        format = o.nextValue();
    }

    private void processAvailability(ActiveOption<Main> o) {
        availability = Availability.valueOf(o.nextValue());
    }

    private void processExpression(ActiveOption<Main> o) {
        filter = Filter.and(o.nextValue());
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
    
    private void run () throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        String formatterClassName = "org.psidnell.omnifocus.format." + format + "Formatter";
        Formatter formatter = (Formatter) Class.forName(formatterClassName).newInstance();
        Writer out = new OutputStreamWriter(System.out);
        formatter.format(root, out);
        out.close();
    }
    
    private void processFlagged(ActiveOption<Main> o) {
        Boolean flagged = Boolean.parseBoolean(o.nextValue().trim().toLowerCase());
        String flaggedFilter = "{flagged:" + flagged + "}";
        filter = Filter.and(filter, flaggedFilter);
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
