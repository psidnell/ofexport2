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

public class MarkdownTest extends FormatTest {
    
    @Test
    public void testProjectMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.getProjectRoot().add(f1);
        ofExport.setFormat("md");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);
                
        Diff.diff (new String[]
            {
                "# f1",
                "",
                "## p1",
                "",
                "- t1",
                "- t2",
                "",
                "> line1",
                "> line2",
                "",
                "- t3",
                "",
                "> line1",
                "> line2",
                "",
                "  - t4",
                "",
                "> > line1",
                "> > line2",
                "",
                "## p2",
                "",
                "> line1",
                "> line2",
                "",
                "## p3",
            }, out.toString().split("\n"));
    }
    
    @Test
    public void testContextMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.setProjectMode(false);
        ofExport.getContextRoot().add(c1);
        ofExport.setFormat("md");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);
                
        Diff.diff (new String[]
            {
                "# c1",
                "",
                "## c2",
                "",
                "- t3",
                "",
                "> line1",
                "> line2",
                "",
                "- t4",
                "",
                "> line1",
                "> line2",
                "",
                "- t2",
                "",
                "> line1",
                "> line2",
            }, out.toString().split("\n"));
    }
}
