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
import org.psidnell.omnifocus.model.NodeFactory;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.SimplifyFilter;
import org.psidnell.omnifocus.visitor.FlattenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author psidnell
 *
 * Command line processing. Contains the options themselves and the values
 * the options affect.
 */
public class CommandLine implements BeanFactoryAware {

    protected static final String PRINT_HELP = "printHelp";
    protected static final String PRINT_INFO = "printInfo";

    protected final static Logger LOGGER = LoggerFactory.getLogger(CommandLine.class);

    protected BeanFactory beanFactory;

    final static Options OPTIONS = new Options();

    protected static final int STARTUP = 0;
    protected static final int BEFORE_LOAD = 1;
    protected static final int AFTER_LOAD = 2;

    public static final String PROG = "ofexport2";

    protected ActiveOptionProcessor<CommandLine> processor;
    protected String jsonInputFile = null;
    protected String outputFile = null;
    protected OFExport ofexport = null;
    protected String format = null;
    protected boolean open = false;
    private ConfigParams config; // NOPMD it can't see into lambdas
    protected String exportFile;
    private NodeFactory nodeFactory;  // NOPMD it can't see into lambdas

    static {

        // HELP

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "h", false, "print help.",
                (m,o)->System.setProperty(PRINT_HELP, "true"),
                STARTUP));

        // EASY OPTIONS

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "available", false, "show available tasks.",
                (m,o)->m.ofexport.addTaskExpression("available", true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "remaining", false, "show remaining tasks.",
                (m,o)->m.ofexport.addTaskExpression("remaining", true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "flagged", false, "show flagged tasks.",
                (m,o)->m.ofexport.addTaskExpression("flagged", true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "unflagged", false, "show flagged tasks.",
                (m,o)->m.ofexport.addTaskExpression("unflagged", true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "duesoon", false, "show remaining tasks.",
                (m,o)->m.ofexport.addTaskExpression("remaining && due.soon", true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "completed", false, "show remaining tasks.",
                (m,o)->m.ofexport.addTaskExpression("completed", true, false),
                AFTER_LOAD));

        // PROJECT

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pc", true, "include and cascade: projects and all beneath where expression is true.",
                (m,o)->m.ofexport.addProjectExpression(o.nextValue(), true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pi", true, "include: projects where expression is true.",
                (m,o)->m.ofexport.addProjectExpression(o.nextValue(), true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "px", true, "exclude: projects where expression is true.",
                (m,o)->m.ofexport.addProjectExpression(o.nextValue(), false, true),
                AFTER_LOAD));


        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "pn", true, "include project specified by name.",
                (m,o)->m.ofexport.addProjectExpression("name==\"" + escape(o.nextValue()) + "\"", true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ps", true, "sort projects by field.",
                (m,o)->m.ofexport.addProjectComparator(o.nextValue()),
                AFTER_LOAD));

        // FOLDER

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fc", true, "include and cascade: folders and all beneath where expression is true.",
                (m,o)->m.ofexport.addFolderExpression (o.nextValue(), true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fi", true, "include: folders where expression is true.",
                (m,o)->m.ofexport.addFolderExpression (o.nextValue(), true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fx", true, "exclude: folders where expression is true.",
                (m,o)->m.ofexport.addFolderExpression (o.nextValue(), false, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fn", true, "include folder specified by name.",
                (m,o)->m.ofexport.addFolderExpression("name==\"" + escape(o.nextValue()) + "\"", true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "fs", true, "sort folders by field.",
                (m,o)->m.ofexport.addFolderComparator(o.nextValue()),
                AFTER_LOAD));

        // TASK

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "tc", true, "include and cascade: tasks and all beneath where expression is true.",
                (m,o)->m.ofexport.addTaskExpression(o.nextValue(), true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ti", true, "include: tasks where expression is true.",
                (m,o)->m.ofexport.addTaskExpression(o.nextValue(), true, false),
                AFTER_LOAD));


        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "tx", true, "exclude: tasks where expression is true.",
                (m,o)->m.ofexport.addTaskExpression(o.nextValue(), false, true),
                AFTER_LOAD));


        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "tn", true, "include tasks specified by name.",
                (m,o)->m.ofexport.addTaskExpression("name==\"" + escape(o.nextValue()) + "\"" , true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ts", true, "sort tasks by field",
                (m,o)->m.ofexport.addTaskComparator(o.nextValue()),
                AFTER_LOAD));

        // CONTEXT

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cc", true, "include and cascade: contexts and all beneath where expression is true.",
                (m,o)->m.ofexport.addContextExpression(o.nextValue(), true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ci", true, "include: contexts where expression is true.",
                (m,o)->m.ofexport.addContextExpression(o.nextValue(), true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cx", true, "exclude: contexts where expression is true.",
                (m,o)->m.ofexport.addContextExpression("name==\"" + escape(o.nextValue()) + "\"", true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cn", true, "include contexts specified by name.",
                (m,o)->m.ofexport.addContextExpression("name==\"" + escape(o.nextValue()) + "\"" , true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "cs", true, "sort contexts by field.",
                (m,o)->m.ofexport.addContextComparator(o.nextValue()),
                AFTER_LOAD));

        // GENERAL

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ac", true, "include and cascade: items and all beneath where expression is true.",
                (m,o)->m.ofexport.addExpression(o.nextValue(), true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ai", true, "include: items where expression is true.",
                (m,o)->m.ofexport.addExpression(o.nextValue(), true, false),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "ax", true, "exclude: items where expression is true.",
                (m,o)->m.ofexport.addExpression(o.nextValue(), false, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "an", true, "include items specified by name.",
                (m,o)->m.ofexport.addExpression("name==\"" + escape(o.nextValue()) + "\"" , true, true),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "m", true, "modify a node value.",
                (m,o)->m.ofexport.addModifyExpression(o.nextValue()),
                AFTER_LOAD));

        // MODES

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "p", false, "prune empty folders, projects and contexts.",
                (m,o)->m.ofexport.addPruneFilter (),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "S", false, "Simplify hierarchies.",
                (m,o)->m.ofexport.addFilter (new SimplifyFilter()),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine>(
                "F", false, "Flatten hierarchies.",
                (m,o)->m.ofexport.addFilter (new FlattenFilter(m.nodeFactory, m.config)),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "c", false, "context mode: filter and display context hierarchy instead of project hierarchy.",
                (m,o)->m.ofexport.setProjectMode(false),
                BEFORE_LOAD));

        // OUTPUT

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "f", true, "output in this format.",
                (m,o)->m.format = o.nextValue(),
                AFTER_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "o", true, "write output to the file.",
                (m,o)->m.outputFile = o.nextValue(),
                BEFORE_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "O", true, "write output to the file and open it.",
                (m,o)->{m.outputFile = o.nextValue(); m.open=true;},
                BEFORE_LOAD));

        // DEBUG/DEV/CONFIG

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "i", false, "print additional help information.",
                (m,o)->System.setProperty(PRINT_INFO, "true"),
                STARTUP));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "D", true, "set property: name=value.",
                (m,o)->setSystemProperty (o.nextValue()),
                STARTUP));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "export", true, "export data to JSON file (for testing).",
                (m,o)->m.exportFile = o.nextValue (),
                BEFORE_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "import", true, "import data from JSON file instead of database (for testing).",
                (m,o)->m.jsonInputFile = o.nextValue(),
                BEFORE_LOAD));

        OPTIONS.addOption(new ActiveOption<CommandLine> (
                "loglevel", true, "set log level [debug,info,warn,error].",
                (m,o)->m.setLogLevel (o.nextValue()),
                BEFORE_LOAD));
    }

    private static void setSystemProperty(String attribValue) {
        int index = attribValue.indexOf('=');
        if (index == -1) {
            throw new IllegalArgumentException("Expected attrib=value, got:" + attribValue);
        }
        String attrib = attribValue.substring(0, index);
        String value = attribValue.substring(index + 1);
        System.setProperty(attrib, value);
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

    protected void printHelp() throws IOException {
        processor.printHelp();
    }

    protected void printAdditionalInfo() {
        new ExprAttributePrinter (Folder.class, Project.class, Task.class, Context.class).print();
    }

    private static String escape(String val) {
        return val.replaceAll("'", "\'");
    }

    public void setOfexport(OFExport ofexport) {
        this.ofexport = ofexport;
    }

    public void setProcessor (ActiveOptionProcessor<CommandLine> processor) {
        this.processor = processor;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setNodeFactory (NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public void setConfigParams (ConfigParams config) {
        this.config = config;
    }
}
