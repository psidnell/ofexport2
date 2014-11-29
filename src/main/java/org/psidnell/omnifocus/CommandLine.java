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

import org.apache.commons.cli.Options;
import org.apache.log4j.Level;
import org.psidnell.omnifocus.cli.ActiveOption;
import org.psidnell.omnifocus.cli.ActiveOptionProcessor;
import org.psidnell.omnifocus.expr.ExprAttributePrinter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author psidnell
 * 
 * Command line processing. Contains the options themselves and the values
 * the options affect.
 */
public class CommandLine {

    protected final static Logger LOGGER = LoggerFactory.getLogger(CommandLine.class);

    final static Options OPTIONS = new Options();

    protected static final int BEFORE_LOAD = 0;
    protected static final int AFTER_LOAD = 1;

    public static final String PROG = "ofexport2";

    protected ActiveOptionProcessor<CommandLine> processor = new ActiveOptionProcessor<>(PROG, OPTIONS);
    protected String jsonInputFile = null;
    protected boolean exitBeforeLoad = false;
    protected String outputFile = null;
    protected OFExport ofexport = null;
    protected String format = null;

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
                (m,o)->m.ofexport.addProjectExpression(o.nextValue()),
                AFTER_LOAD));
                
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pn", "projectname", true, "include project specified by name.",
                (m,o)->m.ofexport.addProjectExpression("name=='" + escape(o.nextValue()) + "'"),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ps", "projectsort", true, "sort projects by field.",
                (m,o)->m.ofexport.addProjectComparator(o.nextValue()),
                AFTER_LOAD));

        // FOLDER
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fe", "folderexpr", true, "include items where folder expression is true.",
                (m,o)->m.ofexport.addFolderExpression (o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fn", "foldername", true, "include folder specified by name.",
                (m,o)->m.ofexport.addFolderExpression("name=='" + escape(o.nextValue()) + "' || included"),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fs", "foldersort", true, "sort folders by field.",
                (m,o)->m.ofexport.addFolderComparator(o.nextValue()),
                AFTER_LOAD));

        // TASK
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "te", "taskexpr", true, "include items where task expression is true.",
                (m,o)->m.ofexport.addTaskExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "tn", "taskname", true, "include tasks specified by name.",
                (m,o)->m.ofexport.addTaskExpression("name=='" + escape(o.nextValue()) + "' || included"),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ts", "tasksort", true, "sort tasks by field",
                (m,o)->m.ofexport.addTaskComparator(o.nextValue()),
                AFTER_LOAD));

        // CONTEXT
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ce", "contextexpr", true, "include items where context expression is true.",
                (m,o)->m.ofexport.addContextExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cn", "taskname", true, "include contexts specified by name.",
                (m,o)->m.ofexport.addContextExpression("name=='" + escape(o.nextValue()) + "' || included"),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cs", "contextsort", true, "sort contexts by field.",
                (m,o)->m.ofexport.addContextComparator(o.nextValue()),
                AFTER_LOAD));
        
        // GENERAL
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "e", "expr", true, "include items (eany type) where context expression is true.",
                (m,o)->m.ofexport.addExpression(o.nextValue()),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "m", "modexpr", true, "modify a node value.",
                (m,o)->m.ofexport.addModifyExpression(o.nextValue()),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "p", "prune", false, "prune empty folders, projects and contexts.",
                (m,o)->m.ofexport.addPruneFilter (),
                AFTER_LOAD));
        
        // MODES
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "c", "contextmode", false, "display context hierarchy instead of project hierarchy).",
                (m,o)->m.ofexport.setProjectMode(false),
                BEFORE_LOAD));
        
        // OUTPUT
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "f", "format", true, "output in this format",
                (m,o)->m.format = o.nextValue(),
                AFTER_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "o", "output", true, "write output to the file",
                (m,o)->m.outputFile = o.nextValue(),
                BEFORE_LOAD));
        
        // DEBUG/DEV
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "l", "load", true, "load data from JSON file instead of database (for testing).",
                (m,o)->m.jsonInputFile = o.nextValue(),
                BEFORE_LOAD));
        
        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "loglevel", true, "set log level [debug,info,warn,error].",
                (m,o)->m.setLogLevel (o.nextValue()),
                BEFORE_LOAD));
    }

    private void printHelp() throws IOException {
        processor.printHelp();
        exitBeforeLoad = true;
    }

    private void printAdditionalInfo() {
        ExprAttributePrinter.print(Folder.class);
        System.out.println();
        ExprAttributePrinter.print(Project.class);
        System.out.println();
        ExprAttributePrinter.print(Context.class);
        System.out.println();
        ExprAttributePrinter.print(Task.class);
        exitBeforeLoad = true;
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

    private static String escape(String val) {
        return val.replaceAll("'", "\'");
    }

    public void setOfexport(OFExport ofexport) {
        this.ofexport = ofexport;
    }
}
