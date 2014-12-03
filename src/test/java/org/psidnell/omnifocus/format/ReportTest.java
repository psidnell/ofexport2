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
package org.psidnell.omnifocus.format;

import java.io.StringWriter;

import org.junit.Test;
import org.psidnell.omnifocus.OFExport;
import org.psidnell.omnifocus.integrationtest.Diff;

public class ReportTest extends FormatTest {

    @Test
    public void testProjectMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.getProjectRoot().add(f1);
        ofExport.setFormat("report");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "Weekly Report XXXX-XX-XX-XXX:",
                "\tp1:",
                "\t\t- t1",
                "\t\t- t2",
                "\t\t- t3 @2014-11-27-Thu",
                "\t\t\t- t4 @2014-11-27-Thu",
                "\tp2:",
                "\tp3:",
            }, out.toString().replaceFirst("[0-9]+-[0-9]+-[0-9]+-[A-Z][a-z]+:", "XXXX-XX-XX-XXX:"). split("\n"));
    }

    @Test
    public void testContextMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.setProjectMode(false);
        ofExport.setFormat("report");
        ofExport.getContextRoot().add(c1);
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "Weekly Report XXXX-XX-XX-XXX:",
                "\tc1:",
                "\t\tc2:",
                "\t\t\t- t3 @2014-11-27-Thu",
                "\t\t\t- t4 @2014-11-27-Thu",
                "\t\t- t2",
            }, out.toString().replaceFirst("[0-9]+-[0-9]+-[0-9]+-[A-Z][a-z]+:", "XXXX-XX-XX-XXX:"). split("\n"));
    }
}
