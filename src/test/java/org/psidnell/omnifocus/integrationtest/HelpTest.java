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

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class HelpTest {

    static Logger LOGGER = LoggerFactory.getLogger(HelpTest.class);
    private PrintStream stdout;

    @Before
    public void setUp() {
        stdout = System.out;
    }

    @After
    public void tearDown() {
        System.setErr(stdout);
    }

    @Test
    public void testRewriteInputFiles() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            InstantiationException, SQLException, IOException {
        // So I don't forget
        assertFalse("TURN THIS OFF", Mode.REWRITE_INPUT_FILES);
    }

    @Test
    public void testHelp() throws Exception {
        File tmpDir = new File("target/data");
        tmpDir.mkdirs();
        File tmpFile = new File(tmpDir, "help.txt");
        PrintStream out = new PrintStream(new FileOutputStream(tmpFile));

        System.setOut(out);
        Main.main(new String[] {"-h"});
        System.setOut(stdout);

        out.flush();
        out.close();

        File expectedFile = new File("src/test/data/help/help.txt");

        if (Mode.REWRITE_INPUT_FILES) {
            LOGGER.error("WARNING overwriting " + expectedFile);
            // Going to (over)write the comparison file with the actual
            // results
            // Only doing this when writing new tests or after updating the
            // input
            // file. The comparison will obviously succeed!
            FileUtils.copyFile(tmpFile, expectedFile);
        }

        Diff.diff(expectedFile, tmpFile);
    }

    @Test
    public void testHelpWhenNoOptions() throws Exception {
        File tmpDir = new File("target/data");
        tmpDir.mkdirs();
        File tmpFile = new File(tmpDir, "help.txt");
        PrintStream out = new PrintStream(new FileOutputStream(tmpFile));

        System.setOut(out);
        Main.main(new String[] {});
        System.setOut(stdout);

        out.flush();
        out.close();

        File expectedFile = new File("src/test/data/help/help.txt");

        if (Mode.REWRITE_INPUT_FILES) {
            LOGGER.error("WARNING overwriting " + expectedFile);
            // Going to (over)write the comparison file with the actual
            // results
            // Only doing this when writing new tests or after updating the
            // input
            // file. The comparison will obviously succeed!
            FileUtils.copyFile(tmpFile, expectedFile);
        }

        Diff.diff(expectedFile, tmpFile);
    }

    @Test
    public void testInfo() throws Exception {
        File tmpDir = new File("target/data");
        tmpDir.mkdirs();
        File tmpFile = new File(tmpDir, "info.txt");
        PrintStream out = new PrintStream(new FileOutputStream(tmpFile));

        System.setOut(out);

        Main.main(new String[] {"-i"});
        out.flush();
        out.close();

        System.setOut(stdout);

        File expectedFile = new File("src/test/data/help/info.txt");

        if (Mode.REWRITE_INPUT_FILES) {
            LOGGER.error("WARNING overwriting " + expectedFile);
            // Going to (over)write the comparison file with the actual
            // results
            // Only doing this when writing new tests or after updating the
            // input
            // file. The comparison will obviously succeed!
            FileUtils.copyFile(tmpFile, expectedFile);
        }

        Diff.diff(expectedFile, tmpFile);
    }

}
