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
package org.psidnell.omnifocus.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * @author psidnell
 *
 * @param <P>
 *
 * A convenience wrapper around CommandLineParser: because everyone always wants to write their own.
 *
 * Provides:
 * - Lambda methods to be associated with options to simplify integration.
 * - Phased execution. Options can be grouped and executed by phase.
 *
 */
public class ActiveOptionProcessor<P> {

    private Options options;
    private String progName;

    public ActiveOptionProcessor(String progName, Options options) {
        this.options = options;
        this.progName = progName;
    }

    public boolean processOptions(P program, String[] args, int phase) throws Exception {

        CommandLineParser parser = new FixedParser();
        CommandLine cl = parser.parse(options, args);

        // If there are unrecognised options then stop
        @SuppressWarnings("unchecked")
        List<String> unrecognized = cl.getArgList();
        if (!unrecognized.isEmpty()) {
            if (!unrecognized.isEmpty()) {
                System.out.println("Unrecognised command line arguments: " + unrecognized);
            }
            printHelp();
            return false;
        }

        // Set the option values on the options
        for (Object o : options.getOptions()) {
            @SuppressWarnings("unchecked")
            ActiveOption<P> opt = (ActiveOption<P>) o;
            opt.setValues(cl.getOptionValues(opt.getOpt()));
        }

        // Walk through the options processing in order
        for (Object o : cl.getOptions()) {
            @SuppressWarnings("unchecked")
            ActiveOption<P> opt = (ActiveOption<P>) o;
            if (opt.getPhase() == phase) {
                opt.process(program);
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public void printHelp() throws IOException {

        System.out.println();
        System.out.println (progName.toUpperCase());
        System.out.println();

        try (
            InputStream in = this.getClass().getResourceAsStream("/version.properties")) {
            Properties p = new Properties();
            p.load(in);
            System.out.println ("Version: " + p.getProperty("version"));
            System.out.println ("Build Date: " + p.getProperty("date"));

        }

        System.out.println();

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(200);
        // Output in the order I specify
        formatter.setOptionComparator((x, y) -> ((ActiveOption<P>) x).getOrder() - ((ActiveOption<P>) y).getOrder());
        formatter.printHelp(progName, options);
    }
}
