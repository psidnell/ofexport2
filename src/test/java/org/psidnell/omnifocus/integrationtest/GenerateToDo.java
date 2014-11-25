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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.psidnell.omnifocus.Main;
import org.psidnell.omnifocus.expr.ExpressionFunctions;

public class GenerateToDo {

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-MMM");

    @Test
    public void generateToDo () throws Exception {
        Main.main(new String[]{
                "-pn", "ofexport2", "-te", "!completed", "-f", "Markdown", "-o", "TODO.txt"
        });
        
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        String dateStr = FORMAT.format(cal.getTime());
        
        
        Main.main(new String[]{
                "-pn", "ofexport2", "-te", "completed", "-f", "Markdown", "-o", "DONE-" + dateStr + ".txt"
        });
    }
}
