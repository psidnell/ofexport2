package org.psidnell.omnifocus.cli;

import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class ActiveOptionProcessor<P> {
    
    private Options options;
    private String progName;

    public ActiveOptionProcessor (String progName, Options options) {
        this.options = options;
        this.progName = progName;
    }
    
    public boolean processOptions (P program, String[] args) throws Exception {
        CommandLineParser parser = new BasicParser();
        CommandLine cl = parser.parse(options, args);
        
        // If there are unrecognised options then stop
        @SuppressWarnings("unchecked")
        List<String> unrecognized = cl.getArgList();
        if (!unrecognized.isEmpty()) {
            System.out.println ("Unrecognised command line arguments: " + unrecognized);
            printHelp();
            return false;
        }

        // Set the option values on the options
        for (Object o : options.getOptions()) {
            @SuppressWarnings("unchecked")
            ActiveOption<P> opt = (ActiveOption<P>) o;
            opt.setValues (cl.getOptionValues(opt.getOpt()));
        }
        
        // Walk through the options processing in order 
        for (Object o : cl.getOptions()) {
            @SuppressWarnings("unchecked")
            ActiveOption<P> opt = (ActiveOption<P>) o;
            opt.process(program);
        }
        
        return true;
    }
    
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        // Output in the order I specify
        formatter.setOptionComparator((x,y)->0);
        formatter.printHelp(progName,options);
    }
}
