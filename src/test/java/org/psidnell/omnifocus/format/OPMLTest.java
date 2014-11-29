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

public class OPMLTest extends FormatTest {
    
    @Test
    public void testProjectMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.getProjectRoot().add(f1);
        ofExport.setFormat("OPML");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);
                
        Diff.diff (new String[]
            {
                "<opml version=\"1.0\">",
                "  <head>",
                "    <title>OmniFocus</title>",
                "  </head>",
                "  <body>",
                "<outline text=\"f1\">",
                "  <outline text=\"p1\">",
                "    <outline text=\"t1\" flagged=\"flagged\">",
                "    </outline>",
                "    <outline text=\"t2\" context=\"c1\" _note=\"line1&#10;line2\">",
                "    </outline>",
                "    <outline text=\"t3\" completed=\"2014-11-27\" context=\"c2\" _note=\"line1&#10;line2\">",
                "      <outline text=\"t4\" completed=\"2014-11-27\" context=\"c2\" _note=\"line1&#10;line2\">",
                "      </outline>",
                "    </outline>",
                "  </outline>",
                "  <outline text=\"p2\" completed=\"2014-11-27\" flagged=\"flagged\" _note=\"line1&#10;line2\">",
                "  </outline>",
                "  <outline text=\"p3\">",
                "  </outline>",
                "</outline>",
                "  </body>",
                "</opml>",
            }, out.toString().split("\n"));
    }
    
    @Test
    public void testContextMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.setProjectMode(false);
        ofExport.getContextRoot().add(c1);
        ofExport.setFormat("OPML");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);
                
        Diff.diff (new String[]
            {
                "<opml version=\"1.0\">",
                "  <head>",
                "    <title>OmniFocus</title>",
                "  </head>",
                "  <body>",
                "<outline text=\"c1\">",
                "  <outline text=\"c2\">",
                "    <outline text=\"t3\" completed=\"2014-11-27\" context=\"c2\" _note=\"line1&#10;line2\">",
                "    </outline>",
                "    <outline text=\"t4\" completed=\"2014-11-27\" context=\"c2\" _note=\"line1&#10;line2\">",
                "    </outline>",
                "  </outline>",
                "  <outline text=\"t2\" context=\"c1\" _note=\"line1&#10;line2\">",
                "  </outline>",
                "</outline>",
                "  </body>",
                "</opml>",
            }, out.toString().split("\n"));
    }
}
