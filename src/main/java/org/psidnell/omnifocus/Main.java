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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.psidnell.omnifocus.util.IOUtils;
import org.springframework.context.ApplicationContext;

/**
 * @author psidnell
 *
 *         The main(...) of the program. There had to be one somewhere. Well here it is.
 */
public class Main extends CommandLine {

    private DataCache data;

    private SQLiteDAO sqliteDAO;

    private void loadData() throws IllegalAccessException, InvocationTargetException, InstantiationException, SQLException, IOException {
        if (jsonInputFile != null) {
            data = DataCache.importData(new File(jsonInputFile));
        } else {
            data = sqliteDAO.load();
        }

        data.build();

        if (ofexport.isProjectMode()) {
            Folder projectRoot = ofexport.getProjectRoot();
            // Add root projects/folders to the fabricated root folder
            for (Folder child : data.getFolders().values()) {
                if (child.getParent() == null) {
                    projectRoot.getFolders().add(child);
                }
            }

            for (Project child : data.getProjects().values()) {
                if (child.getFolder() == null) {
                    projectRoot.getProjects().add(child);
                }
            }
        } else {
            Context contextRoot = ofexport.getContextRoot();
            // Add root contexts to the fabricated root context
            for (Context child : data.getContexts().values()) {
                if (child.getParent() == null) {
                    contextRoot.getContexts().add(child);
                }
            }
        }
    }

    private void run() throws Exception {
        Writer out;
        if (outputFile != null) {
            out = new BufferedWriter(new FileWriter(outputFile));
        } else {
            out = new BufferedWriter(IOUtils.systemOutWriter());
        }

        if (format != null) {
            ofexport.setFormat(format.toLowerCase());
        } else if (outputFile != null && outputFile.contains(".")) {
            int dot = outputFile.indexOf('.');
            ofexport.setFormat(outputFile.substring(dot + 1).toLowerCase());
        }

        ofexport.process();
        ofexport.write(out);

        out.flush();
        out.close();
    }

    private boolean procesPreLoadOptions(String[] args) throws Exception {
        // Load initial switches, help etc
        return processor.processOptions(this, args, BEFORE_LOAD) && !exitBeforeLoad;
    }

    private void processPostLoadOptions(String[] args) throws Exception {
        // Load filters etc.
        processor.processOptions(this, args, AFTER_LOAD);
    }

    public static void main(String[] args) throws Exception {

        ApplicationContext appContext = ApplicationContextFactory.getContext();

        Main main = appContext.getBean("main", Main.class);

        if (!main.procesPreLoadOptions(args)) {
            LOGGER.debug("Exiting");
            return;
        }

        main.loadData();

        main.processPostLoadOptions(args);

        main.run();

        LOGGER.debug("Exiting");
    }

    public void setSqliteDAO(SQLiteDAO sqliteDAO) {
        this.sqliteDAO = sqliteDAO;
    }
}
