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
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.psidnell.omnifocus.expr.ExprIncludeVisitor;
import org.psidnell.omnifocus.expr.ExpressionComparator;
import org.psidnell.omnifocus.expr.ExprVisitor;
import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.format.FreeMarkerFormatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.IncludeVisitor;
import org.psidnell.omnifocus.visitor.IncludedFilter;
import org.psidnell.omnifocus.visitor.SortingFilter;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;
import org.psidnell.omnifocus.visitor.VisitorDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.TemplateException;

/**
 * @author psidnell
 *
 * The main processing class.
 *
 * 1. Model objects are added to the root project/context.
 *
 * 2. Filter expressions are added to reduce the tree.
 *
 * 3. Sorting comparators are added to control the output ordering
 *
 * 4. Finally the tree is processed to produce the output.
 *
 */
public class OFExport {

    private static Logger LOGGER = LoggerFactory.getLogger(OFExport.class);

    protected String format = "txt";
    protected boolean projectMode = true;
    protected List<Visitor> filters = new LinkedList<>();
    protected SortingFilter sortingFilter = new SortingFilter();
    private Folder projectRoot;
    private Context contextRoot;

    public OFExport() {
        projectRoot = new Folder();
        projectRoot.setName("RootFolder");
        projectRoot.setIsRoot(true);
        projectRoot.setId("__%%RootFolder"); // to give deterministic JSON/XML output

        contextRoot = new Context();
        contextRoot.setName("RootContext");
        contextRoot.setIsRoot(true);
        contextRoot.setId("__%%RootContext"); // to give deterministic JSON/XML output
    }

    public void process() throws Exception {

        if (projectMode) {
            Traverser.traverse(new IncludeVisitor(false), projectRoot);
            filters.stream().forEachOrdered((f) -> Traverser.traverse(f, projectRoot));
            Traverser.traverse(sortingFilter, projectRoot);
        } else {
            Traverser.traverse(new IncludeVisitor(false), contextRoot);
            filters.stream().forEachOrdered((f) -> Traverser.traverse(f, contextRoot));
            Traverser.traverse(sortingFilter, contextRoot);
        }
    }

    public void write(Writer out) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, TemplateException {
        Formatter formatter = loadFormatter();

        if (projectMode) {
            formatter.format(projectRoot, out);
        } else {
            formatter.format(contextRoot, out);
        }
        out.flush();
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private Formatter loadFormatter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        // Formats are loaded by name.

        // Start by looking for a freemarker template:
        try {
            String templateName = format + ".ftl";
            return new FreeMarkerFormatter(templateName);
        } catch (IOException e) {
            LOGGER.debug("unable to load fremarker template for: " + format, e);
        }

        // Then try and load it by class name:
        String formatterClassName = "org.psidnell.omnifocus.format." + format.toUpperCase() + "Formatter";
        return (Formatter) Class.forName(formatterClassName).newInstance();
    }

    public void addProjectExpression(String expression) {
        if (!projectMode) {
            throw new IllegalArgumentException("project filters only valid in project mode");
        }
        VisitorDescriptor visitwhat = new VisitorDescriptor().visit(Folder.class, Project.class);
        VisitorDescriptor applyToWhat = new VisitorDescriptor().visit(Project.class);
        addFilter(new ExprIncludeVisitor(expression, projectMode, visitwhat, applyToWhat));
        addFilter(new IncludedFilter());
    }

    public void addFolderExpression(String expression) {
        if (!projectMode) {
            throw new IllegalArgumentException("project filters only valid in project mode");
        }
        VisitorDescriptor visitWhat = new VisitorDescriptor().visit(Folder.class);
        VisitorDescriptor applyToWhat = new VisitorDescriptor().visit(Folder.class);
        addFilter(new ExprIncludeVisitor(expression, projectMode, visitWhat, applyToWhat));
        addFilter(new IncludedFilter());
    }

    public void addTaskExpression(String expression) {
        if (projectMode) {
            VisitorDescriptor visitWhat = new VisitorDescriptor().visit(Folder.class, Project.class, Task.class);
            VisitorDescriptor applyToWhat = new VisitorDescriptor().visit(Task.class);
            addFilter(new ExprIncludeVisitor(expression, projectMode, visitWhat, applyToWhat));
        } else {
            VisitorDescriptor visitWhat = new VisitorDescriptor().visit(Context.class, Task.class);
            VisitorDescriptor applyToWhat = new VisitorDescriptor().visit(Task.class);
            addFilter(new ExprIncludeVisitor(expression, projectMode, visitWhat, applyToWhat));
        }
        addFilter(new IncludedFilter());
    }

    public void addContextExpression(String expression) {
        if (projectMode) {
            throw new IllegalArgumentException("context filters only valid in context mode");
        }
        VisitorDescriptor visitWhat = new VisitorDescriptor().visit(Context.class);
        VisitorDescriptor applyToWhat = new VisitorDescriptor().visit(Context.class);
        addFilter(new ExprIncludeVisitor(expression, projectMode, visitWhat, applyToWhat));
        addFilter(new IncludedFilter());
    }

    public void addExpression(String expression) {
        VisitorDescriptor visitWhat = new VisitorDescriptor().visitAll();
        VisitorDescriptor applyToWhat = new VisitorDescriptor().visitAll();
        addFilter(new ExprIncludeVisitor(expression, projectMode, visitWhat, applyToWhat));
        addFilter(new IncludedFilter());
    }

    public void addModifyExpression(String expression) {
        VisitorDescriptor visitWhat = new VisitorDescriptor().visitAll();
        VisitorDescriptor applyToWhat = new VisitorDescriptor().visitAll();
        addFilter(new ExprVisitor(expression, visitWhat, applyToWhat));
    }

    public void addPruneFilter() {
        // Go bottom up
        if (projectMode) {
            addProjectExpression("taskCount > 0");
            addFolderExpression("folderCount > 0 || projectCount > 0");
        } else {
            addContextExpression("contextCount > 0 || taskCount > 0");
        }
    }

    public boolean isProjectMode() {
        return projectMode;
    }

    public void addFilter(Visitor filter) {
        filters.add(filter);
    }

    public void setProjectMode(boolean b) {
        this.projectMode = false;
    }

    public Folder getProjectRoot() {
        return projectRoot;
    }

    public Context getContextRoot() {
        return contextRoot;
    }

    public void addProjectComparator(String expr) {
        sortingFilter.addProjectComparator(new ExpressionComparator<>(expr, Project.class));
    }

    public void addFolderComparator(String expr) {
        sortingFilter.addFolderComparator(new ExpressionComparator<>(expr, Folder.class));
    }

    public void addTaskComparator(String expr) {
        sortingFilter.addTaskComparator(new ExpressionComparator<>(expr, Task.class));
    }

    public void addContextComparator(String expr) {
        sortingFilter.addContextComparator(new ExpressionComparator<>(expr, Context.class));
    }
}
