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
package org.psidnell.omnifocus;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.script.ScriptException;

import org.apache.commons.cli.Options;
import org.psidnell.omnifocus.cli.ActiveOption;
import org.psidnell.omnifocus.cli.ActiveOptionProcessor;
import org.psidnell.omnifocus.expr.ExprParser;
import org.psidnell.omnifocus.filter.Filter;
import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Document;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.organise.TaskSorter;
import org.psidnell.omnifocus.osa.OSAClassDescriptor;

public class Main {
    
    final static Options OPTIONS = new Options();
    static {
        

        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "allfolders", false, "Load projects and tasks from all folders",
        //        (m,o)->m.processAllFolders(o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "h", "help", false, "print help",
                (m,o)->m.printHelp ()));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "f", "foldername", true, "Load projects and tasks from folder specified by name",
                (m,o)->m.processFolderName(o)));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "e", "documentexpr", true, "???????",
                (m,o)->m.processExpr(o)));
        
        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "c", "contextname", true, "Load tasks from context specified by name",
        //        (m,o)->m.processContextName(o)));
        
        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "p", "projectname", true, "Load tasks from project specified by name",
        //        (m,o)->m.processProjectName(o)));
        
        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "i", "inbox", false, "Load tasks from the inbox",
        //        (m,o)->m.processInbox()));
        
        //OPTIONS.addOption(new ActiveOption<Main> (
        //        "flagged", true, "flagged (true|false)",
        //        (m,o)->m.processFlagged (o)));
        
        //OPTIONS.addOption(new ActiveOption<Main> (
        //        "a", "availability", true, "availability (" + Arrays.asList(Availability.values()) + ")".replaceAll(",", "|"),
        //        (m,o)->m.processAvailability (o)));
        
        //OPTIONS.addOption(new ActiveOption<Main> (
        //        "F", "folderexpr", true, "expression",
        //        (m,o)->m.processFolderExpression (o)));
        
        //OPTIONS.addOption(new ActiveOption<Main> (
        //        "P", "projectexpr", true, "expression",
        //        (m,o)->m.processProjectExpression (o)));
        
        //OPTIONS.addOption(new ActiveOption<Main> (
        //        "T", "taskexpr", true, "expression",
        //        (m,o)->m.processTaskExpression (o)));
        
        //OPTIONS.addOption(new ActiveOption<Main> (
        //        "C", "contextexpr", true, "expression",
        //        (m,o)->m.processContextExpression (o)));
        
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
        
    public Main(ActiveOptionProcessor<Main> processor) throws IOException, ClassNotFoundException {
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

    private void processAllFolders(ActiveOption<Main> o) throws IOException, ScriptException {
        for (Folder f : of.getFolders(folderExpr, projectExpr, taskExpr)) {
            root.addChild(f);
        }
    }
    
    private void processFolderName(ActiveOption<Main> o) throws IOException, ScriptException, ClassNotFoundException {        
        HashMap<String, OSAClassDescriptor> descriptors = of.getDescriptors ();
        String folderName = o.nextValue();
        String exprStr = "Document.folders:flattenedFolders.where({name:'" + escape(folderName) + "'})";
        ExprParser.parse(exprStr, descriptors);
        
    }
    
    private void processExpr(ActiveOption<Main> o) throws IOException, ScriptException, ClassNotFoundException {        
        HashMap<String, OSAClassDescriptor> descriptors = of.getDescriptors ();
        String exprStr = o.nextValue();
        ExprParser.parse(exprStr, descriptors);
        
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
    
    private void run () throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, ScriptException {

        String json = of.execute(of.getDescriptors().values());
        Document d = of.asDocument(json);
        root.addChild(d);
        
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
    
    private static final String escape(String str) {
        return str.replaceAll("'", "\\'");
    }
}
