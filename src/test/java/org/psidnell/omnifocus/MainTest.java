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
package org.psidnell.omnifocus;

import java.util.Date;

import org.junit.Test;
import org.psidnell.omnifocus.expr.ExpressionFunctions;

public class MainTest {
    @Test
    public void testWhatsDueToday() throws Exception {
        Main.main(new String[] {
                "-te", "dueDate == date('today')",
                "-format", "SimpleTextList"});
    }
    
    @Test
    public void testWhatsDueTomorrow() throws Exception {
        Main.main(new String[] {
                "-te", "dueDate == date('tomorrow')",
                "-format", "SimpleTextList"});
    }
    
    @Test
    public void testWhatsCompleteToday() throws Exception {
        Main.main(new String[] {
                "-te", "completionDate == date('" + ExpressionFunctions.YYYYMMDD.format(new Date()) + "')",
                "-format", "SimpleTextList"});
    }
    
    @Test
    public void testWhatsCompleteThisWorkWeek() throws Exception {
        Main.main(new String[] {
                "-te", "within(completionDate, 'Mon', 'Fri')",
                "-format", "SimpleTextList"});
    }
    
    @Test
    public void testWhatsCompleteLastWorkWeek() throws Exception {
        Main.main(new String[] {
                "-te", "within(completionDate, '-Mon', '-Fri')",
                "-format", "SimpleTextList"});
    }
    
    @Test
    public void testHelp() throws Exception {
        Main.main(new String[] { "-h" });
        Main.main(new String[] {});
   }
}
