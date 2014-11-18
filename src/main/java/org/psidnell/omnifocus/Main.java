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
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.script.ScriptException;

import org.apache.commons.cli.Options;
import org.psidnell.omnifocus.cli.ActiveOption;
import org.psidnell.omnifocus.cli.ActiveOptionProcessor;
import org.psidnell.omnifocus.filter.Filter;
import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Group;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.organise.TaskSorter;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;

public class Main {
    
    final static Options OPTIONS = new Options();
    static {
        

        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "allfolders", false, "Load projects and tasks from all folders",
        //        (m,o)->m.processAllFolders(o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "h", "help", false, "print help",
                (m,o)->m.printHelp ()));
        
        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "f", "foldername", true, "Load projects and tasks from folder specified by name",
        //        (m,o)->m.processFolderName(o)));
        
        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "e", "documentexpr", true, "???????",
        //        (m,o)->m.processExpr(o)));
        
        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "c", "contextname", true, "Load tasks from context specified by name",
        //        (m,o)->m.processContextName(o)));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "p", "projectname", true, "Load tasks from project specified by name",
                (m,o)->m.processProjectName(o)));
        
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
    
    private String taskExpr = null;
    private String projectExpr = null;
    private String contextExpr = null;
    private String folderExpr = null;
    private Availability availability = Availability.Available;
    private ActiveOptionProcessor<Main> processor;
    private String format = "SimpleTextList";

    private Visitor filter;
        
    public Main(ActiveOptionProcessor<Main> processor) throws IOException, ClassNotFoundException {
        this.processor = processor;
        taskExpr = null;
    }

    private void processProjectName(ActiveOption<Main> o) {
        String projectName = o.nextValue();
        filter = new Visitor () {
            @Override
            public List<Context> filterContexts(List<Context> contexts) {
                return new LinkedList<Context> ();
            }
            @Override
            public List<Folder> filterFolders(List<Folder> folders) {
                return new LinkedList<Folder> ();
            }
            @Override
            public List<Node> filterChildren(List<Node> children) {
                LinkedList<Node> result = new LinkedList<>();
                for (Node n : children) {
                    if (n instanceof Project) {
                        Project p = (Project) n;
                        if (p.getName().equals(projectName)) {
                            result.add (p);
                        }
                    }
                }
                return result;
            }
            @Override
            public List<Project> filterProjects(List<Project> projects) {
                LinkedList<Project> result = new LinkedList<>();
                for (Project p : projects) {
                    if (p.getName().equals(projectName)) {
                        result.add (p);
                    }
                }
                return result;
            }
        };
    }

    private void processFormat(ActiveOption<Main> o) {
        format = o.nextValue();
    }

    private void printHelp() {
       processor.printHelp ();
    }

    
    private void run () throws Exception {
        
        DataCache data = SQLiteDAO.load();
        
        Group root = new Group();
        root.setName("");
        for (Context child : data.getContexts().values()){
            root.addChild(child);
        }
        for (Folder child : data.getFolders().values()){
            root.addChild(child);
        }
        //for (Task child : data.getTasks().values()){
        //    root.addChild(child);
        //}
        for (Project child : data.getProjects().values()){
            root.addChild(child);
        }
        
        if (filter != null) {
            Traverser.doTraverse(filter, root, true);
        }
        
        String formatterClassName = "org.psidnell.omnifocus.format." + format + "Formatter";
        Formatter formatter = (Formatter) Class.forName(formatterClassName).newInstance();
        
        
        TaskSorter sorter = new TaskSorter();
        sorter.organise(root);
        
        Writer out = new OutputStreamWriter(System.out) {
            @Override
            public void close() throws IOException {
                // Don't want to close System.out
            }
        };
        formatter.format(root, out);
        out.flush();
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
    
    private static final String escape(String str) {
        return str.replaceAll("'", "\\'");
    }
}
