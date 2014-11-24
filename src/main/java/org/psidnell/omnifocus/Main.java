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
import org.psidnell.omnifocus.expr.ExprVisitor;
import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.psidnell.omnifocus.visitor.IncludeVisitor;
import org.psidnell.omnifocus.visitor.IncludedFilter;
import org.psidnell.omnifocus.visitor.SortingFilter;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;
import org.psidnell.omnifocus.visitor.VisitorDescriptor;

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
                "pe", "projectexpr", true, "include items where project expression is true",
                (m,o)->m.processProjectExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "pn", "projectname", true, "include tasks from project specified by name",
                (m,o)->m.processProjectName(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "fe", "folderexpr", true, "include items where folder expression is true",
                (m,o)->m.processFolderExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "fn", "foldername", true, "include tasks from project specified by name",
                (m,o)->m.processFolderName(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "te", "taskexpr", true, "include items where task expression is true",
                (m,o)->m.processTaskExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "tn", "taskname", true, "include tasks specified by name",
                (m,o)->m.processTaskName(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "ce", "contextexpr", true, "include items where context expression is true",
                (m,o)->m.processContextExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<Main>(
                "cn", "taskname", true, "include contexts specified by name",
                (m,o)->m.processContextName(o.nextValue()),
                AFTER_LOAD));
        
        //OPTIONS.addOption(new ActiveOption<Main>(
        //        "a", "availability", true, "FirstAvailable, Available (default), Remaining, All, Completed",
        //        (m,o)->{m.availability = Availability.valueOf(o.nextValue());},
        //        AFTER_LOAD));
        
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
    
    private ActiveOptionProcessor<Main> processor;
    private String format = "SimpleTextList";
    private boolean projectMode = true;

    private List<Visitor> filters = new LinkedList<>();

    private DataCache data;

    private Folder projectRoot;

    private Context contextRoot;

    private boolean exit = false;
        
    public Main(ActiveOptionProcessor<Main> processor) throws IOException, ClassNotFoundException {
        this.processor = processor;
    }

    private void processProjectExpression(String expression) {
        if (!projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        VisitorDescriptor visitwhat = new  VisitorDescriptor().visit(Folder.class, Project.class);
        VisitorDescriptor applyToWhat = new  VisitorDescriptor().visit(Project.class);
        filters.add(new ExprVisitor(expression, visitwhat, applyToWhat));
        filters.add(new IncludedFilter());
    }

    private void processProjectName(String projectName) {
        if (!projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        processProjectExpression("name=='" + escape(projectName) + "'");
    }
    
    private void processFolderExpression(String expression) {
        if (!projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        VisitorDescriptor visitWhat = new  VisitorDescriptor().visit(Folder.class);
        VisitorDescriptor applyToWhat = new  VisitorDescriptor().visit(Folder.class);
        filters.add(new ExprVisitor(expression, visitWhat, applyToWhat));
        filters.add(new IncludedFilter());
    }

    private void processFolderName(String folderName) {
        if (!projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        processFolderExpression("name=='" + escape(folderName) + "'");
    }
    
    private void processTaskName(String taskName) {
        if (!projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        processTaskExpression("name=='" + escape(taskName) + "'");
    }
    
    private void processTaskExpression(String expression) {
        if (projectMode) {
            VisitorDescriptor visitWhat = new  VisitorDescriptor().visit(Folder.class, Project.class, Task.class);
            VisitorDescriptor applyToWhat = new  VisitorDescriptor().visit(Task.class);
            filters.add(new ExprVisitor(expression, visitWhat, applyToWhat));
        }
        else {
            VisitorDescriptor visitWhat = new  VisitorDescriptor().visit(Context.class, Task.class);
            VisitorDescriptor applyToWhat = new  VisitorDescriptor().visit(Task.class);
            filters.add(new ExprVisitor(expression, visitWhat, applyToWhat));
        }
        filters.add(new IncludedFilter());
    }

    private void processContextName(String taskName) {
        if (projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        processContextExpression("name=='" + escape(taskName) + "'");
    }

    private void processContextExpression(String expression) {
        if (projectMode) {
            throw new IllegalArgumentException ("context filters only valid in context mode");
        }
        VisitorDescriptor visitWhat = new  VisitorDescriptor().visit(Context.class);
        VisitorDescriptor applyToWhat = new  VisitorDescriptor().visit(Context.class);
        filters.add(new ExprVisitor(expression, visitWhat, applyToWhat));
        filters.add(new IncludedFilter());
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
            // Add root projects/folders to the fabricated root folder
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
            Traverser.traverse(new IncludeVisitor(false), projectRoot);
        }
        else {
            // Add root contexts to the fabricated root context
            for (Context child : data.getContexts().values()){
                if (child.getParent() == null) {
                    contextRoot.getContexts().add(child);
                }
            }
            
            Traverser.traverse(new IncludeVisitor(false), contextRoot);
        }
    }
    
    private void run () throws Exception {
        
        filters.add(new SortingFilter());
        
        if (projectMode) {
            filters.stream().forEachOrdered((f)->Traverser.traverse(f, projectRoot));
        }
        else {
            filters.stream().forEachOrdered((f)->Traverser.traverse(f, contextRoot));
        }

        String formatterClassName = "org.psidnell.omnifocus.format." + format + "Formatter";
        Formatter formatter = (Formatter) Class.forName(formatterClassName).newInstance();
                
        Writer out = new OutputStreamWriter(System.out) {
            @Override
            public void close() throws IOException {
                // Don't want to close System.out
            }
        };
        
        if (projectMode) {
            formatter.format(projectRoot, out);
        }
        else {
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
    
    private String escape (String val) {
        return val.replaceAll("'", "\'");
    }
}
