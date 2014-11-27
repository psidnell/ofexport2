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

public class HTMLTest extends FormatTest {
    
    @Test
    public void testProjectMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.getProjectRoot().add(f1);
        ofExport.setFormat("HTML");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);
        
        System.out.println(out);
        
        Diff.diff (new String[]
            {
                "<html>",
                "  <head>",
                "    <title>OmniFocus</title>",
                "    <style type=\"text/css\">",
                "        ",
                "        body",
                "        {",
                "            background-color: #fdf6e3;",
                "            font-family: Verdana, Arial, Helvetica, sans-serif;",
                "            margin-left:2cm;",
                "        }",
                "        ",
                "        a:link",
                "        {",
                "           color:inherit;",
                "           text-decoration: none;",
                "        }",
                "        ",
                "        a:hover",
                "        {",
                "          text-decoration: underline;",
                "        }",
                "        ",
                "        .Project",
                "        {",
                "            color: #b58900;",
                "        }",
                "        ",
                "        .Folder",
                "        {",
                "            color: #b58900;",
                "        }",
                "",
                "        .Context",
                "        {",
                "            color: #6c71c4;",
                "        }",
                "        ",
                "        .Task",
                "        {",
                "            color: #586e75;",
                "            font-size: 1.0em;",
                "        }",
                "        ",
                "        .TaskGroup",
                "        {",
                "            color: #586e75;",
                "            font-weight: bold;",
                "            font-size: 1.0em;",
                "        }",
                "        ",
                "        .Attrib",
                "        {",
                "            font-size: 0.8em;",
                "        }",
                "        ",
                "        .AStart",
                "        {",
                "            color: #2aa198;",
                "        }",
                "        ",
                "        .ADue",
                "        {",
                "            color: #dc322f;",
                "        }",
                "        ",
                "        .AComplete",
                "        {",
                "            color: #839496;",
                "        }",
                "        ",
                "        .AFlagged",
                "        {",
                "            color: #d33682;",
                "        }",
                "        ",
                "        .AProject",
                "        {",
                "            color: #b58900;",
                "        }",
                "        ",
                "        .AContext",
                "        {",
                "            color: #6c71c4;",
                "        }",
                "    </style>",
                "  </head>",
                "  <body>",
                "    <H1 class=\"Folder\"><a href=\"omnifocus:///folder/%%%%f1\">f1</a></H1><ul>",
                "      <H2 class=\"Project\"><a href=\"omnifocus:///task/%%%%p1\">p1</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span></span></H2><ul>",
                "        <li class=\"Task\"><a href=\"omnifocus:///task/%%%%t1\">t1</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"AStart\">2014-11-27</span></span></li><ul>",
                "        </ul>",
                "        <li class=\"Task\"><a href=\"omnifocus:///task/%%%%t2\">t2</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"ADue\">2014-11-27</span></span></li><ul>",
                "          line1<br>",
                "          line2<br>",
                "        </ul>",
                "        <li class=\"Task\"><a href=\"omnifocus:///task/%%%%t3\">t3</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"AComplete\">2014-11-27</span></span></li><ul>",
                "          line1<br>",
                "          line2<br>",
                "          <li class=\"Task\"><a href=\"omnifocus:///task/%%%%t4\">t4</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"AComplete\">2014-11-27</span></span></li><ul>",
                "            line1<br>",
                "            line2<br>",
                "          </ul>",
                "        </ul>",
                "      </ul>",
                "      <H2 class=\"Project\"><a href=\"omnifocus:///task/%%%%p2\">p2</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"ADue\">2014-11-27</span> <span class=\"AComplete\">2014-11-27</span></span></H2><ul>",
                "        line1<br>",
                "        line2<br>",
                "      </ul>",
                "      <H2 class=\"Project\"><a href=\"omnifocus:///task/%%%%p3\">p3</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span></span></H2><ul>",
                "      </ul>",
                "    </ul>",
                "  </body>",
                "<html>",
            }, out.toString().split("\n"));
    }
    
    @Test
    public void testContextMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.setProjectMode(false);
        ofExport.getContextRoot().add(c1);
        ofExport.setFormat("HTML");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);
        
        System.out.println(out);
        
        Diff.diff (new String[]
            {
                "<html>",
                "  <head>",
                "    <title>OmniFocus</title>",
                "    <style type=\"text/css\">",
                "        ",
                "        body",
                "        {",
                "            background-color: #fdf6e3;",
                "            font-family: Verdana, Arial, Helvetica, sans-serif;",
                "            margin-left:2cm;",
                "        }",
                "        ",
                "        a:link",
                "        {",
                "           color:inherit;",
                "           text-decoration: none;",
                "        }",
                "        ",
                "        a:hover",
                "        {",
                "          text-decoration: underline;",
                "        }",
                "        ",
                "        .Project",
                "        {",
                "            color: #b58900;",
                "        }",
                "        ",
                "        .Folder",
                "        {",
                "            color: #b58900;",
                "        }",
                "",
                "        .Context",
                "        {",
                "            color: #6c71c4;",
                "        }",
                "        ",
                "        .Task",
                "        {",
                "            color: #586e75;",
                "            font-size: 1.0em;",
                "        }",
                "        ",
                "        .TaskGroup",
                "        {",
                "            color: #586e75;",
                "            font-weight: bold;",
                "            font-size: 1.0em;",
                "        }",
                "        ",
                "        .Attrib",
                "        {",
                "            font-size: 0.8em;",
                "        }",
                "        ",
                "        .AStart",
                "        {",
                "            color: #2aa198;",
                "        }",
                "        ",
                "        .ADue",
                "        {",
                "            color: #dc322f;",
                "        }",
                "        ",
                "        .AComplete",
                "        {",
                "            color: #839496;",
                "        }",
                "        ",
                "        .AFlagged",
                "        {",
                "            color: #d33682;",
                "        }",
                "        ",
                "        .AProject",
                "        {",
                "            color: #b58900;",
                "        }",
                "        ",
                "        .AContext",
                "        {",
                "            color: #6c71c4;",
                "        }",
                "    </style>",
                "  </head>",
                "  <body>",
                "    <H1 class=\"Context\"><a href=\"omnifocus:///context/%%%%c1\">c1</a><span class=\"Attrib\"></span></H1>",
                "      <H2 class=\"Context\"><a href=\"omnifocus:///context/%%%%c2\">c2</a><span class=\"Attrib\"></span></H2>",
                "        <li class=\"Task\"><a href=\"omnifocus:///task/%%%%t3\">t3</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"AComplete\">2014-11-27</span></span></li><ul>",
                "          line1<br>",
                "          line2<br>",
                "        </ul>",
                "        <li class=\"Task\"><a href=\"omnifocus:///task/%%%%t4\">t4</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"AComplete\">2014-11-27</span></span></li><ul>",
                "          line1<br>",
                "          line2<br>",
                "        </ul>",
                "      </ul>",
                "      <li class=\"Task\"><a href=\"omnifocus:///task/%%%%t2\">t2</a><span class=\"Attrib\"> <span class=\"AFlagged\">FLAGGED</span> <span class=\"ADue\">2014-11-27</span></span></li><ul>",
                "        line1<br>",
                "        line2<br>",
                "      </ul>",
                "    </ul>",
                "  </body>",
                "<html>",
            }, out.toString().split("\n"));
    }
}
