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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Options;
import org.apache.log4j.Level;
import org.psidnell.omnifocus.cli.ActiveOption;
import org.psidnell.omnifocus.cli.ActiveOptionProcessor;
import org.psidnell.omnifocus.expr.AttribPrinter;
import org.psidnell.omnifocus.expr.ExprVisitor;
import org.psidnell.omnifocus.expr.ExpressionComparator;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.IncludedFilter;
import org.psidnell.omnifocus.visitor.SortingFilter;
import org.psidnell.omnifocus.visitor.Visitor;
import org.psidnell.omnifocus.visitor.VisitorDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLine {
    
    protected final static Logger LOGGER = LoggerFactory.getLogger(CommandLine.class);
    
    final static Options OPTIONS = new Options();

    protected static final int BEFORE_LOAD = 0;
    protected static final int AFTER_LOAD = 1;
    
    static {
        
        // HELP
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "h", "help", false, "print help.",
                (m,o)->m.printHelp (),
                BEFORE_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "i", "info", false, "print additional help information.",
                (m,o)->m.printAdditionalInfo (),
                BEFORE_LOAD));
        
        // PROJECT
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pe", "projectexpr", true, "include items where project expression is true.",
                (m,o)->m.processProjectExpression(o.nextValue()),
                AFTER_LOAD));
                
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pn", "projectname", true, "include project specified by name",
                (m,o)->m.processProjectName(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ps", "projectsort", true, "sort projects by field",
                (m,o)->m.sortingFilter.addProjectComparator(new ExpressionComparator<>(o.nextValue(), Project.class)),
                AFTER_LOAD));

        // FOLDER
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fe", "folderexpr", true, "include items where folder expression is true",
                (m,o)->m.processFolderExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fn", "foldername", true, "include folder specified by name",
                (m,o)->m.processFolderName(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fs", "foldersort", true, "sort folders by field",
                (m,o)->m.sortingFilter.addFolderComparator(new ExpressionComparator<>(o.nextValue(), Folder.class)),
                AFTER_LOAD));

        // TASK
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "te", "taskexpr", true, "include items where task expression is true",
                (m,o)->m.processTaskExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "tn", "taskname", true, "include tasks specified by name",
                (m,o)->m.processTaskName(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ts", "tasksort", true, "sort tasks by field",
                (m,o)->m.sortingFilter.addTaskComparator(new ExpressionComparator<>(o.nextValue(), Task.class)),
                AFTER_LOAD));

        // CONTEXT
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ce", "contextexpr", true, "include items where context expression is true",
                (m,o)->m.processContextExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cn", "taskname", true, "include contexts specified by name",
                (m,o)->m.processContextName(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cs", "contextsort", true, "sort contexts by field",
                (m,o)->m.sortingFilter.addContextComparator(new ExpressionComparator<>(o.nextValue(), Context.class)),
                AFTER_LOAD));

        // MODES
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "c", "contextmode", false, "display context hierarchy instead of project hierarchy)",
                (m,o)->m.projectMode = false,
                BEFORE_LOAD));
        
        // OUTPUT
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "f", "format", true, "output in this format",
                (m,o)->m.processFormat (o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "o", "output", true, "write output to the file",
                (m,o)->m.outputFile = o.nextValue(),
                BEFORE_LOAD));
        
        // DEBUG/DEV
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "l", "load", true, "load data from JSON file instead of database (for testing)",
                (m,o)->m.jsonInputFile = o.nextValue(),
                BEFORE_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "loglevel", true, "set log level [debug,info,warn,error]",
                (m,o)->m.setLogLevel (o.nextValue()),
                BEFORE_LOAD));
    }

    public static final String PROG = "ofexport2";
    
    protected ActiveOptionProcessor<CommandLine> processor;
    protected String format = "SimpleTextList";
    protected boolean projectMode = true;
    protected List<Visitor> filters = new LinkedList<>();
    protected String jsonInputFile;
    protected boolean exit = false;
    protected String outputFile;
    protected SortingFilter sortingFilter = new SortingFilter();

        
    public CommandLine() {
        processor = new ActiveOptionProcessor<>(PROG, OPTIONS);
    }

    private void processProjectExpression(String expression) {
        if (!projectMode) {
            throw new IllegalArgumentException ("project filters only valid in project mode");
        }
        VisitorDescriptor visitwhat = new  VisitorDescriptor().visit(Folder.class, Project.class);
        VisitorDescriptor applyToWhat = new  VisitorDescriptor().visit(Project.class);
        filters.add(new ExprVisitor(expression, projectMode, visitwhat, applyToWhat));
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
        filters.add(new ExprVisitor(expression, projectMode, visitWhat, applyToWhat));
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
            filters.add(new ExprVisitor(expression, projectMode, visitWhat, applyToWhat));
        }
        else {
            VisitorDescriptor visitWhat = new  VisitorDescriptor().visit(Context.class, Task.class);
            VisitorDescriptor applyToWhat = new  VisitorDescriptor().visit(Task.class);
            filters.add(new ExprVisitor(expression, projectMode, visitWhat, applyToWhat));
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
        filters.add(new ExprVisitor(expression, projectMode, visitWhat, applyToWhat));
        filters.add(new IncludedFilter());
    }

    private void processFormat(String format) {
        this.format = format;
    }

    private void printHelp() {
       processor.printHelp ();
       exit  = true;
    }
    
    private void printAdditionalInfo() {
        AttribPrinter.print(Folder.class);
        System.out.println();
        AttribPrinter.print(Project.class);
        System.out.println();
        AttribPrinter.print(Context.class);
        System.out.println();
        AttribPrinter.print(Task.class);
        exit  = true;
     }
    
    private void setLogLevel(String logLevel) {
        Level level = Level.DEBUG;
        switch (logLevel.toLowerCase()) {
            case "debug":
                level = Level.DEBUG;
                break;
            case "info":
                level = Level.INFO;
                break;
            case "warn":
                level = Level.WARN;
                break;
            case "error":
                level = Level.ERROR;
                break;
        }
        org.apache.log4j.Logger.getLogger("org.psidnell").setLevel(level);
        LOGGER.debug("Log level is " + logLevel);
    }
    
    private String escape (String val) {
        return val.replaceAll("'", "\'");
    }
}
