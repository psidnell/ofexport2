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
import org.psidnell.omnifocus.visitor.FlattenFilter;
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
    protected boolean open = false;

    static {

        // HELP

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "h", false, "print help.",
                (m,o)->m.printHelp (),
                BEFORE_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "i", false, "print additional help information.",
                (m,o)->m.printAdditionalInfo (),
                BEFORE_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "available", false, "show available items.",
                (m,o)->m.ofexport.addAvailableFilter (),
                BEFORE_LOAD));

        // PROJECT

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pi", true, "include items where project expression is true.",
                (m,o)->m.ofexport.addProjectExpression(o.nextValue(), true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "px", true, "exclude items where project expression is true.",
                (m,o)->m.ofexport.addProjectExpression(o.nextValue(), false),
                AFTER_LOAD));


        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pn", true, "include project specified by name.",
                (m,o)->m.ofexport.addProjectExpression("name==\"" + escape(o.nextValue()) + "\"", true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ps", true, "sort projects by field.",
                (m,o)->m.ofexport.addProjectComparator(o.nextValue()),
                AFTER_LOAD));

        // FOLDER

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fi", true, "include items where folder expression is true.",
                (m,o)->m.ofexport.addFolderExpression (o.nextValue(), true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fx", true, "exclude items where folder expression is true.",
                (m,o)->m.ofexport.addFolderExpression (o.nextValue(), false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fn", true, "include folder specified by name.",
                (m,o)->m.ofexport.addFolderExpression("name==\"" + escape(o.nextValue()) + "\"", true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fs", true, "sort folders by field.",
                (m,o)->m.ofexport.addFolderComparator(o.nextValue()),
                AFTER_LOAD));

        // TASK

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ti", true, "include items where task expression is true.",
                (m,o)->m.ofexport.addTaskExpression(o.nextValue(), true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "tx", true, "include items where task expression is true.",
                (m,o)->m.ofexport.addTaskExpression(o.nextValue(), false),
                AFTER_LOAD));


        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "tn", true, "include tasks specified by name.",
                (m,o)->m.ofexport.addTaskExpression("name==\"" + escape(o.nextValue()) + "\"" , true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ts", true, "sort tasks by field",
                (m,o)->m.ofexport.addTaskComparator(o.nextValue()),
                AFTER_LOAD));

        // CONTEXT

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ci", true, "include items where context expression is true.",
                (m,o)->m.ofexport.addContextExpression(o.nextValue(), true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cx", true, "include contexts specified by name.",
                (m,o)->m.ofexport.addContextExpression("name==\"" + escape(o.nextValue()) + "\"", true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cn", true, "include contexts specified by name.",
                (m,o)->m.ofexport.addContextExpression("name==\"" + escape(o.nextValue()) + "\"" , true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cs", true, "sort contexts by field.",
                (m,o)->m.ofexport.addContextComparator(o.nextValue()),
                AFTER_LOAD));

        // GENERAL

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ai", true, "include items (any type) where context expression is true.",
                (m,o)->m.ofexport.addExpression(o.nextValue(), true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ax", true, "exclude items (any type) where context expression is true.",
                (m,o)->m.ofexport.addExpression(o.nextValue(), false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "m", true, "modify a node value.",
                (m,o)->m.ofexport.addModifyExpression(o.nextValue()),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "p", false, "prune empty folders, projects and contexts.",
                (m,o)->m.ofexport.addPruneFilter (),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "F", false, "Flatten hierarchies",
                (m,o)->m.ofexport.addFilter (new FlattenFilter()),
                AFTER_LOAD));

        // MODES

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "c", false, "display context hierarchy instead of project hierarchy).",
                (m,o)->m.ofexport.setProjectMode(false),
                BEFORE_LOAD));

        // OUTPUT

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "f", true, "output in this format",
                (m,o)->m.format = o.nextValue(),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "o", true, "write output to the file",
                (m,o)->m.outputFile = o.nextValue(),
                BEFORE_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "O", true, "write output to the file and open it",
                (m,o)->{m.outputFile = o.nextValue(); m.open=true;},
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

    protected void printHelp() throws IOException {
        processor.printHelp();
        exitBeforeLoad = true;
    }

    protected void printAdditionalInfo() {
        ExprAttributePrinter.print(Folder.class);
        System.out.println();
        ExprAttributePrinter.print(Project.class);
        System.out.println();
        ExprAttributePrinter.print(Context.class);
        System.out.println();
        ExprAttributePrinter.print(Task.class);
        exitBeforeLoad = true;
    }

    protected void setLogLevel(String logLevel) {
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
