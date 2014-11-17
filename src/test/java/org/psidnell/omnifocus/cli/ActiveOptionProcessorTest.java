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

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class ActiveOptionProcessorTest {

    private LinkedList<String> list = new LinkedList<>();
    
    @Before
    public void before () {
        list.clear();
    }
    
    @Test
    public void testOrderOfProcessing () throws Exception {
        Options options = new Options();
        options.addOption(new ActiveOption<ActiveOptionProcessorTest>("x", true, "i'm an opt", (p,o)->gotX(o)));
        options.addOption(new ActiveOption<ActiveOptionProcessorTest>("y", true, "i'm an opt", (p,o)->gotY(o)));
        
        ActiveOptionProcessor<ActiveOptionProcessorTest> p = new ActiveOptionProcessor<ActiveOptionProcessorTest>("test", options);
        p.processOptions(this, new String[]{"-x", "1", "-y", "2", "-x", "3", "-y", "4"});
        
        // Show that options processed in order
        assertEquals ("[X:1, Y:2, X:3, Y:4]", list.toString());
    }

    private void gotX(ActiveOption<ActiveOptionProcessorTest> o) {
        list.add("X:" + o.nextValue());
    }
    
    private void gotY(ActiveOption<ActiveOptionProcessorTest> o) {
        list.add("Y:" + o.nextValue());
    }
}
