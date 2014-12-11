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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.psidnell.omnifocus.Main;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.model.NodeImpl;
import org.psidnell.omnifocus.sqlite.SQLiteDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class IntegrationTest {

    static Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);

    private static File tmpDataDir = new File ("target/data");
    private static File srcDataDir = new File ("src/test/data/");

    private static final File EXPORTED_DATA_FILE = new File (tmpDataDir, "raw-example-data.json");
    private static final File PREVIOUSLY_EXPORTED_DATA_FILE = new File (srcDataDir, "raw-example-data.json");

    @Before
    public void doExport () throws Exception {
        tmpDataDir.mkdirs();
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        SQLiteDAO dao = appContext.getBean("sqlitedao", SQLiteDAO.class);
        // Export nodes that start with '##' and remove the '##'
        Predicate<NodeImpl> filterFn = (n)->{
            boolean isTestData = n.getName().startsWith("##");
            if (isTestData) {
                n.setName(n.getName().substring(2));
            }
            return isTestData;
        };
        DataCache.exportData(EXPORTED_DATA_FILE, filterFn, dao, appContext);
    }

    @Test
    public void testExportedData () throws IOException {
        Diff.diff(PREVIOUSLY_EXPORTED_DATA_FILE, EXPORTED_DATA_FILE);
    }

    @Test
    public void testFormats () throws Exception {

        // IF THE TEST FAILS because you've changed a template, set doDiff=false
        // and copy the generated files over the test files.
        boolean doDiff = true;

        String[] suffixes = {
            "csv",
            "debug",
            "html",
            "md",
            "opml",
            "report",
            "taskpaper",
            "txt",
            "json",
            "xml"
        };

        for (String suffix : suffixes) {
            LOGGER.warn("Suffix:" + suffix);
            runMainAndDiff("example-p." + suffix, new String[0], doDiff);
            runMainAndDiff("example-c." + suffix, new String[]{"-c"}, doDiff);
        }

        assertTrue ("Turn this back on", doDiff);
    }

    private void runMainAndDiff(final String name, String[] extraArgs, boolean doDiff) throws Exception, IOException {
        File tmp = new File (tmpDataDir, name);
        String[] args = {"-import", PREVIOUSLY_EXPORTED_DATA_FILE.getPath(), "-o", tmp.getPath()};
        LinkedList<String> combinedArgs = new LinkedList<>();
        combinedArgs.addAll(Arrays.asList(args));
        combinedArgs.addAll(Arrays.asList(extraArgs));
        Main.main(combinedArgs.toArray(new String[combinedArgs.size()]));
        if (doDiff) {
            Diff.diff(new File("src/test/data/" + name), tmp);
        }
    }
}
