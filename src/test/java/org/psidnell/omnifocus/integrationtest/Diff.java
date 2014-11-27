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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Diff {

    static Logger LOGGER = LoggerFactory.getLogger(Diff.class);

    public static void diff(File expectedFile, File actualFile) throws IOException {
    
        try (
            BufferedReader expectedIn = new BufferedReader(new FileReader(expectedFile));
            BufferedReader actualIn = new BufferedReader(new FileReader(actualFile));) {
            
            int line = 1;
            
            String expected = expectedIn.readLine();
            String actual = actualIn.readLine();
            
            while (Diff.diff (expected, actual, line)) {
                expected = expectedIn.readLine();
                actual = actualIn.readLine();
                line++;
            }
        }
    }

    public static void diff(String[] expected, String[] actual) {

        int i;
        for (i = 0; i < expected.length && i < actual.length; i++) {
            diff (expected[i], actual[i], i+1);
        }
        
        if (expected.length > actual.length) {
            diff (expected[i], null, i+1);
        }
        else if (expected.length < actual.length) {
            diff (null, actual[i], i+1);
        }
    }
    
    public static boolean diff(String expected, String actual, int line) {
        
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
