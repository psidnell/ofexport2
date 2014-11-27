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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.psidnell.omnifocus.format.Formatter;
import org.psidnell.omnifocus.format.FreeMarkerFormatter;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.psidnell.omnifocus.util.IOUtils;
import org.psidnell.omnifocus.visitor.IncludeVisitor;
import org.psidnell.omnifocus.visitor.Traverser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main extends CommandLine {
        
    private DataCache data;

    private Folder projectRoot;

    private Context contextRoot;
    
    private void load () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, SQLException, FileNotFoundException, IOException {
        if (jsonInputFile != null) {
            data = DataCache.importData(new File (jsonInputFile));
        }
        else {
            data = SQLiteDAO.load();
        }
        
        data.build();
        
        projectRoot = new Folder();
        projectRoot.setName("RootFolder");
        projectRoot.setId("__%%RootFolder"); // to give deterministic JSON/XML output
        
        contextRoot = new Context();
        contextRoot.setName("RootContext");
        contextRoot.setId("__%%RootContext"); // to give deterministic JSON/XML output
        
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
        
        filters.add(sortingFilter);
        
        if (projectMode) {
            filters.stream().forEachOrdered((f)->Traverser.traverse(f, projectRoot));
        }
        else {
            filters.stream().forEachOrdered((f)->Traverser.traverse(f, contextRoot));
        }

        Formatter formatter = loadFormatter();
                
        Writer out;
        if (outputFile != null) {
            out = new BufferedWriter(new FileWriter(outputFile));
        }
        else {
            out = new BufferedWriter (IOUtils.systemOutWriter());
        }
        
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
        
        @SuppressWarnings("resource")
        ApplicationContext appContext = new ClassPathXmlApplicationContext("config.xml");

        Main main = appContext.getBean("main", Main.class);
        
        // Load initial switches, help etc
        if (!main.processor.processOptions(main, args, BEFORE_LOAD) || main.exit) {
            LOGGER.debug("Exiting");
            return;
        }
                
        main.load ();
        
        // Load filters etc.
        main.processor.processOptions(main, args, AFTER_LOAD);

        main.run ();
        
        LOGGER.debug("Exiting");        
    }
    
    private Formatter loadFormatter() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        // Formats are loaded by name.
        
        // Start by looking for a freemarker template:
        try {
            String templateName = format + ".ftl";
            return new FreeMarkerFormatter(templateName);
        }
        catch (IOException e) {
            LOGGER.debug("unable to load fremarker template for: " + format, e);
        }
        
        // Then try and load it by class name:
        String formatterClassName = "org.psidnell.omnifocus.format." + format + "Formatter";
        return (Formatter) Class.forName(formatterClassName).newInstance();
    }
}
