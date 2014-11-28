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
package org.psidnell.omnifocus.integrationtest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.OFApplicationContext;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.psidnell.omnifocus.util.IOUtils;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataExportImportTest {

    private static final ApplicationContext appContext = OFApplicationContext.create();
    
    private SQLiteDAO sqliteDAO;
    
    @Before
    public void setUp () {
        sqliteDAO = appContext.getBean("sqlitedao", SQLiteDAO.class);
    }
    
    @Test
    public void testExport () throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, SQLException, IOException {
        
        // Export
        File file = new File("target/test1.json");
        DataCache.exportData(file, (n)->n.getName().startsWith("%Test"), sqliteDAO);
        
        // Import
        DataCache dataCache = DataCache.importData (file);
        ObjectMapper mapper = new ObjectMapper();
        
        // Just check we've loaded something, other tests will
        // verify the structure
        assertFalse (dataCache.getContexts().isEmpty());
        assertFalse (dataCache.getProjectInfos().isEmpty());
        assertFalse (dataCache.getFolders().isEmpty());
        assertFalse (dataCache.getTasks().isEmpty());
        assertTrue (dataCache.getProjects().isEmpty());
        
        // Dump to console
        Writer out = IOUtils.systemOutWriter();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, dataCache);
        out.flush();
        out.close();
    }
}
