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
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.psidnell.omnifocus.Main;
import org.psidnell.omnifocus.model.DataCache;
import org.psidnell.omnifocus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
    public void testRewriteInputFiles () throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, SQLException, IOException {
        
        if (Mode.REWRITE_INPUT_FILES) {
            // Regenerate the exported json from raw OmniFocus data
            DataCache.exportData(new File("src/test/data/input/simple.json"), (n)->n.getName().startsWith("%Test"));
        }
        
        // So I don't forget
        assertFalse ("TURN THIS OFF", Mode.REWRITE_INPUT_FILES);
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
                testFormat(inputFile, comparisonFile, params.format, args, Mode.REWRITE_INPUT_FILES);
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
                testFormat(inputFile, comparisonFile, params.format, args, Mode.REWRITE_INPUT_FILES);
            }
        }
    }

    private void testFormat(File inputFile, File comparisonFile, String formatNamez, String extraArgs[], boolean fix) throws Exception {
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
        
        if (fix) {
            LOGGER.error("WARNING overwriting " + inputFile);
            // Going to (over)write the comparison file with the actual results
            // Only doing this when writing new tests or after updating the input
            // file. The comparison will obviously succeed!
            FileUtils.copyFile(tmpFile, comparisonFile);
        }

        Diff.diff(comparisonFile, tmpFile);
    }
}
