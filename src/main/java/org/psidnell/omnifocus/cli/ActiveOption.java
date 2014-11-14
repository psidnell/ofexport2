package org.psidnell.omnifocus.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.script.ScriptException;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class ActiveOption<P> extends Option {

    private final ActiveOptionProcess<P> processor;
    private Stack<String> valStack = new Stack<>();

    public ActiveOption(String opt, boolean hasArg, String description, ActiveOptionProcess<P> processor) throws IllegalArgumentException {
        super(opt, hasArg, description);
        this.processor = processor;
    }
    
    public String nextValue() {
        return valStack.pop();
    }

    public void setValues(String[] optionValues) {
        if (optionValues != null) {
            List<String> valList = Arrays.asList(optionValues);
            Collections.reverse(valList);
            valStack.addAll(valList);
        }
    }

    void process (P program) throws IOException, ScriptException, ParseException {
        processor.process(program, this);
    }
}