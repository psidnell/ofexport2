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
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.organise.TaskSorter;

public class Main {
    
    final static Options OPTIONS = new Options();
    static {
        OPTIONS.addOption(new ActiveOption<Main> (
                "h", "help", false, "print help",
                (m,o)->m.printHelp ()));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "f", "foldername", true, "Load projects and tasks from folder specified by name",
                (m,o)->m.processFolderName(o)));
        
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
                "flagged", true, "flagged (true|false)",
                (m,o)->m.processFlagged (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "a", "availability", true, "availability (" + Arrays.asList(Availability.values()) + ")".replaceAll(",", "|"),
                (m,o)->m.processAvailability (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "F", "folderexpr", true, "expression",
                (m,o)->m.processFolderExpression (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "P", "projectexpr", true, "expression",
                (m,o)->m.processProjectExpression (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "T", "taskexpr", true, "expression",
                (m,o)->m.processTaskExpression (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "C", "contextexpr", true, "expression",
                (m,o)->m.processContextExpression (o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "format", true, "format",
                (m,o)->m.processFormat (o)));
    }

    public static final String PROG = "ofexport2";
    
    private final OmniFocus of;
    private final Group root;
    private String taskExpr = null;
    private String projectExpr = null;
    private String contextExpr = null;
    private String folderExpr = null;
    private Availability availability = Availability.Available;
    private ActiveOptionProcessor<Main> processor;
    private String format = "SimpleTextList";
        
    public Main(ActiveOptionProcessor<Main> processor) throws IOException {
        this.processor = processor;
        of = new OmniFocus();

        root = new Group();
        root.setName("");

        taskExpr = null;
    }

    private void processFormat(ActiveOption<Main> o) {
        format = o.nextValue();
    }

    private void processAvailability(ActiveOption<Main> o) {
        availability = Availability.valueOf(o.nextValue());
    }

    private void processFolderExpression(ActiveOption<Main> o) {
        folderExpr = Filter.and(folderExpr, o.nextValue());
    }
    
    private void processProjectExpression(ActiveOption<Main> o) {
        projectExpr = Filter.and(projectExpr, o.nextValue());
    }
    
    private void processTaskExpression(ActiveOption<Main> o) {
        taskExpr = Filter.and(taskExpr, o.nextValue());
    }
    
    private void processContextExpression(ActiveOption<Main> o) {
        contextExpr = Filter.and(contextExpr, o.nextValue());
    }

    private void printHelp() {
       processor.printHelp ();
    }

    private void processFolderName(ActiveOption<Main> o) throws IOException, ScriptException {
        String folderName = o.nextValue();
        for (Folder f : of.getFoldersByName(folderName, projectExpr, taskExpr)) {
            root.addChild(f);
        }
    }
    
    private void processProjectName(ActiveOption<Main> o) throws IOException, ScriptException {
        String projectName = o.nextValue();
        for (Project p : of.getProjectsByName(projectName, taskExpr)) {
            root.addChild(p);
        }
    }

    private void processContextName(ActiveOption<Main> o) throws IOException, ScriptException {
        String contextName = o.nextValue();
        for (Context c : of.getContextsByName(contextName, taskExpr)) {
            root.addChild(c);
        }
    }
    
    private void processInbox() throws IOException, ScriptException {
        Group inbox = new Group();
        inbox.setName("Inbox");
        for (Task t : of.loadAllInboxTasks(taskExpr)) {
            inbox.addChild(t);
        }
        root.addChild(inbox);
    }
    
    private void run () throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        String formatterClassName = "org.psidnell.omnifocus.format." + format + "Formatter";
        Formatter formatter = (Formatter) Class.forName(formatterClassName).newInstance();
        
        TaskSorter sorter = new TaskSorter();
        sorter.organise(root);
        
        Writer out = new OutputStreamWriter(System.out);
        formatter.format(root, out);
        out.close();
    }
    
    private void processFlagged(ActiveOption<Main> o) {
        Boolean flagged = Boolean.parseBoolean(o.nextValue().trim().toLowerCase());
        String flaggedFilter = "{flagged:" + flagged + "}";
        taskExpr = Filter.and(taskExpr, flaggedFilter);
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
