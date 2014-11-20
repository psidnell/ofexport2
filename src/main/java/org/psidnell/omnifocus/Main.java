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

import org.apache.commons.cli.Options;
import org.psidnell.omnifocus.cli.ActiveOption;
import org.psidnell.omnifocus.cli.ActiveOptionProcessor;
import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.organise.TaskSorter;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.psidnell.omnifocus.util.NoisyRunnable;
import org.psidnell.omnifocus.util.Wrap;
import org.psidnell.omnifocus.visitor.IncludedFilter;
import org.psidnell.omnifocus.visitor.IncludeVisitor;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;

public class Main {
    
    final static Options OPTIONS = new Options();

    private static final int BEFORE_LOAD = 0;
    private static final int AFTER_LOAD = 1;
    
    static {
        

        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "allfolders", false, "Load projects and tasks from all folders",
        //        (m,o)->m.processAllFolders(o)));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "h", "help", false, "print help",
                (m,o)->m.printHelp (),
                BEFORE_LOAD));
        
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
                "xa", "excludeAll", false, "exclude everything",
                (m,o)->m.processExcludeAll(o),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "ip", "projectname", true, "include tasks from project specified by name",
                (m,o)->m.processProjectName(o),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "projectMode", false, "the default mode",
                (m,o)->m.projectMode = true,
                BEFORE_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main> (
                "contextMode", false, "inverse of project mode)",
                (m,o)->m.projectMode = false,
                BEFORE_LOAD));
        
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
                "format", true, "output in this format",
                (m,o)->m.processFormat (o),
                AFTER_LOAD));
    }

    public static final String PROG = "ofexport2";
    
    private String taskExpr = null;
    private String projectExpr = null;
    private String contextExpr = null;
    private String folderExpr = null;
    private Availability availability = Availability.Available;
    private ActiveOptionProcessor<Main> processor;
    private String format = "SimpleTextList";
    private boolean projectMode = true;

    private List<Runnable> cmdLineActions = new LinkedList<>();
    private List<Visitor> filters = new LinkedList<>();

    private DataCache data;

    private Folder projectRoot;

    private Context contextRoot;

    private boolean exit = false;
        
    public Main(ActiveOptionProcessor<Main> processor) throws IOException, ClassNotFoundException {
        this.processor = processor;
    }

    private void processExcludeAll(ActiveOption<Main> o) {
        cmdLineActions.add(Wrap.runnable(()->{
            if (projectMode) {
                Traverser.traverse(new IncludeVisitor(false), projectRoot);
            }
            else {
                Traverser.traverse(new IncludeVisitor(false), contextRoot);
            }
        }));

    }

    private void processProjectName(ActiveOption<Main> o) {
        if (!projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        String projectName = o.nextValue();
        cmdLineActions.add(Wrap.runnable(()->{
            for (Project p : data.getProjects().values()) {
                if (projectName.equals(p.getName())) {
                    p.include(projectMode);
                }
            }
        }));
    }

    private void processFormat(ActiveOption<Main> o) {
        format = o.nextValue();
    }

    private void printHelp() {
       processor.printHelp ();
       exit  = true;
    }

    private void load () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, SQLException {
        data = SQLiteDAO.load();
        
        projectRoot = new Folder();
        projectRoot.setName("RootFolder");
        
        contextRoot = new Context();
        contextRoot.setName("RootContext");
        
        if (projectMode) {
            for (Folder child : data.getFolders().values()){
                if (child.getParent() == null) {
                    projectRoot.getFolders().add(child);
                }
            }
            
            for (Project child : data.getProjects().values()){
                if (child.getFolder() == null) {
                    projectRoot.getProjects().add(child);
                }
            }
        }
        else {
            for (Context child : data.getContexts().values()){
                if (child.getParent() == null) {
                    contextRoot.getContexts().add(child);
                }
            }            
        }
    }
    
    private void run () throws Exception {
        cmdLineActions.stream().forEach((a)->a.run());
        
        // Prune items not included
        filters.add(new IncludedFilter());
        
        if (projectMode) {
            for (Visitor filter : filters) {
                Traverser.filter(filter, projectRoot);
            }
        }
        else {
            for (Visitor filter : filters) {
                Traverser.filter(filter, contextRoot);
            }
        }

        String formatterClassName = "org.psidnell.omnifocus.format." + format + "Formatter";
        Formatter formatter = (Formatter) Class.forName(formatterClassName).newInstance();
        
        TaskSorter sorter = new TaskSorter();
        
        Writer out = new OutputStreamWriter(System.out) {
            @Override
            public void close() throws IOException {
                // Don't want to close System.out
            }
        };
        
        if (projectMode) {
            sorter.organise(projectRoot);
            formatter.format(projectRoot, out);
        }
        else {
            sorter.organise(contextRoot);
            formatter.format(contextRoot, out);
        }
        
        out.flush();
        out.close();
    }
    
    public static void main(String args[]) throws Exception {
        
        ActiveOptionProcessor<Main> processor = new ActiveOptionProcessor<>(PROG, OPTIONS);
        
        Main main = new Main (processor);
        
        // Load initial switches, help etc
        if (!processor.processOptions(main, args, BEFORE_LOAD) || main.exit) {
            return;
        }
        
        main.load ();
        
        // Load filters etc.
        processor.processOptions(main, args, AFTER_LOAD);

        main.run ();
    }
}
