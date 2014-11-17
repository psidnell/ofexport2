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
    private static int count = 0;
    private int order;

    public ActiveOption(String opt, String description, ActiveOptionProcess<P> processor) throws IllegalArgumentException {
        super(opt, description);
        this.processor = processor;
        setOrder();
    }
    
    public ActiveOption(String opt, boolean hasArg, String description, ActiveOptionProcess<P> processor) throws IllegalArgumentException {
        super(opt, hasArg, description);
        this.processor = processor;
        setOrder();
    }
    
    public ActiveOption(String opt, String longOpt, boolean hasArg, String description, ActiveOptionProcess<P> processor) throws IllegalArgumentException {
        super(opt, longOpt, hasArg, description);
        this.processor = processor;
        setOrder();
    }

    private final void setOrder() {
        synchronized (ActiveOption.class) {
            order = count++;
        }
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
    
    int getOrder () {
        return order;
    }
}