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

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;
import org.psidnell.omnifocus.Main;
import org.psidnell.omnifocus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegrationTest {

    static Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);

    private class TestParams {
        String format;
        String suffix;
        TestParams(String format, String suffix) {
            this.format = format;
            this.suffix = suffix;
        }
    }
    
    @Test
    public void testFormatsInProjectMode() throws Exception {
        // These formats are "<FormatName>.<suffix>"
        TestParams testParams[] = {
                new TestParams("SimpleTextList", ".txt"),
                new TestParams("TaskPaper",".taskpaper"),
                new TestParams("JSON",".json"),
                new TestParams("XML", ".xml")
            };
        
        for (TestParams params : testParams) {
            for (File inputFile : new File("src/test/data/input").listFiles((f) -> f.getName().endsWith(".json"))) {
                                
                String outputFileName = inputFile.getName().replaceAll("\\..*", params.suffix);
                File comparisonFile = new File("src/test/data/output/formats/" + params.format + "/" + outputFileName);

                String args[] = new String[]{"-f", params.format};
                testFormat(inputFile, comparisonFile, params.format, args);
            }
        }
    }
    
    @Test
    public void testFormatsInContextMode() throws Exception {
        // These formats are "<FormatName>.<suffix>"
        TestParams testParams[] = {
                new TestParams("SimpleTextList", ".txt"),
                new TestParams("TaskPaper",".taskpaper"),
                new TestParams("JSON",".json"),
                new TestParams("XML", ".xml")
            };
        
        String testSuffix = "-c";
        
        for (TestParams params : testParams) {
            for (File inputFile : new File("src/test/data/input").listFiles((f) -> f.getName().endsWith(".json"))) {
                                
                String outputFileName = inputFile.getName().replaceAll("\\..*", testSuffix + params.suffix);
                File comparisonFile = new File("src/test/data/output/formats/" + params.format + "/" + outputFileName);

                String args[] = new String[]{"-c", "-f", params.format};
                testFormat(inputFile, comparisonFile, params.format, args);
            }
        }
    }

    private void testFormat(File inputFile, File comparisonFile, String formatNamez, String extraArgs[]) throws Exception {
        LOGGER.info("TESTING: " + inputFile);

        File tmpDir = new File("target/data");
        tmpDir.mkdirs();
        File tmpFile = new File(tmpDir, comparisonFile.getName());

        LOGGER.info("  Output file: " + tmpFile);

        String baseArgs[] = {"-load", inputFile.getPath(), "-o", tmpFile.getPath()};
        
        LinkedList<String> argList = new LinkedList<>();
        argList.addAll(Arrays.asList(baseArgs));
        argList.addAll(Arrays.asList(extraArgs));
        
        String args[] = argList.toArray(new String[argList.size()]);
        
        LOGGER.info("  Running: cmdline: " + StringUtils.join(args, " "));

        Main.main(args);

        diff(comparisonFile, tmpFile);
    }

    private static void diff(File expectedFile, File actualFile) throws IOException {

        try (
            BufferedReader expectedIn = new BufferedReader(new FileReader(expectedFile));
            BufferedReader actualIn = new BufferedReader(new FileReader(actualFile));) {
            
            int line = 1;
            
            String expected = expectedIn.readLine();
            String actual = actualIn.readLine();
            
            while (diff (expected, actual, line)) {
                expected = expectedIn.readLine();
                actual = actualIn.readLine();
                line++;
            }
        }
    }

    private static boolean diff(String expected, String actual, int line) {
        
        if (expected != null && actual != null && !expected.equals(actual)) {
            LOGGER.error("Error on line " + line);
            LOGGER.error("Expected: " + expected);
            LOGGER.error("Actual:   " + actual);
        }
        String message = "Error on line " + line + " " + expected + " != " + actual;
        assertEquals (message, expected, actual);
        return expected != null;
    }
}
