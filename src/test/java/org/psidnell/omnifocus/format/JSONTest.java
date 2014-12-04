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

public class JSONTest extends FormatTest {

    @Test
    public void testProjectMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.getProjectRoot().add(f1);
        ofExport.setFormat("JSON");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "{",
                "  \"name\" : \"RootFolder\",",
                "  \"id\" : \"__%%RootFolder\",",
                "  \"rank\" : 0,",
                "  \"projects\" : [ ],",
                "  \"folders\" : [ {",
                "    \"name\" : \"f1\",",
                "    \"id\" : \"%%%%f1\",",
                "    \"rank\" : 0,",
                "    \"projects\" : [ {",
                "      \"name\" : \"p1\",",
                "      \"id\" : \"%%%%p1\",",
                "      \"rank\" : 0,",
                "      \"note\" : null,",
                "      \"deferDate\" : null,",
                "      \"dueDate\" : null,",
                "      \"completionDate\" : null,",
                "      \"sequential\" : false,",
                "      \"flagged\" : false,",
                "      \"tasks\" : [ {",
                "        \"name\" : \"t1\",",
                "        \"id\" : \"%%%%t1\",",
                "        \"rank\" : 0,",
                "        \"note\" : null,",
                "        \"deferDate\" : 1417046400000,",
                "        \"dueDate\" : null,",
                "        \"completionDate\" : null,",
                "        \"sequential\" : false,",
                "        \"flagged\" : true,",
                "        \"tasks\" : [ ],",
                "        \"parentTaskId\" : null,",
                "        \"contextId\" : null,",
                "        \"blocked\" : false,",
                "        \"available\" : true,",
                "        \"remaining\" : true",
                "      }, {",
                "        \"name\" : \"t2\",",
                "        \"id\" : \"%%%%t2\",",
                "        \"rank\" : 0,",
                "        \"note\" : \"line1\\nline2\",",
                "        \"deferDate\" : null,",
                "        \"dueDate\" : 1417046400000,",
                "        \"completionDate\" : null,",
                "        \"sequential\" : false,",
                "        \"flagged\" : false,",
                "        \"tasks\" : [ ],",
                "        \"parentTaskId\" : null,",
                "        \"contextId\" : null,",
                "        \"blocked\" : false,",
                "        \"available\" : true,",
                "        \"remaining\" : true",
                "      }, {",
                "        \"name\" : \"t3\",",
                "        \"id\" : \"%%%%t3\",",
                "        \"rank\" : 0,",
                "        \"note\" : \"line1\\nline2\",",
                "        \"deferDate\" : null,",
                "        \"dueDate\" : null,",
                "        \"completionDate\" : 1417046400000,",
                "        \"sequential\" : false,",
                "        \"flagged\" : false,",
                "        \"tasks\" : [ {",
                "          \"name\" : \"t4\",",
                "          \"id\" : \"%%%%t4\",",
                "          \"rank\" : 0,",
                "          \"note\" : \"line1\\nline2\",",
                "          \"deferDate\" : null,",
                "          \"dueDate\" : null,",
                "          \"completionDate\" : 1417046400000,",
                "          \"sequential\" : false,",
                "          \"flagged\" : false,",
                "          \"tasks\" : [ ],",
                "          \"parentTaskId\" : null,",
                "          \"contextId\" : null,",
                "          \"blocked\" : false,",
                "          \"available\" : false,",
                "          \"remaining\" : false",
                "        } ],",
                "        \"parentTaskId\" : null,",
                "        \"contextId\" : null,",
                "        \"blocked\" : false,",
                "        \"available\" : false,",
                "        \"remaining\" : false",
                "      } ],",
                "      \"status\" : \"status\",",
                "      \"available\" : false,",
                "      \"remaining\" : true",
                "    }, {",
                "      \"name\" : \"p2\",",
                "      \"id\" : \"%%%%p2\",",
                "      \"rank\" : 0,",
                "      \"note\" : \"line1\\nline2\",",
                "      \"deferDate\" : null,",
                "      \"dueDate\" : 1417046400000,",
                "      \"completionDate\" : 1417046400000,",
                "      \"sequential\" : false,",
                "      \"flagged\" : true,",
                "      \"tasks\" : [ ],",
                "      \"status\" : \"status\",",
                "      \"available\" : false,",
                "      \"remaining\" : false",
                "    }, {",
                "      \"name\" : \"p3\",",
                "      \"id\" : \"%%%%p3\",",
                "      \"rank\" : 0,",
                "      \"note\" : null,",
                "      \"deferDate\" : null,",
                "      \"dueDate\" : null,",
                "      \"completionDate\" : null,",
                "      \"sequential\" : false,",
                "      \"flagged\" : false,",
                "      \"tasks\" : [ ],",
                "      \"status\" : \"status\",",
                "      \"available\" : false,",
                "      \"remaining\" : true",
                "    } ],",
                "    \"folders\" : [ ],",
                "    \"parentFolderId\" : null",
                "  } ],",
                "  \"parentFolderId\" : null",
                "}",
            }, out.toString().split("\n"));
    }

    @Test
    public void testContextMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.setProjectMode(false);
        ofExport.getContextRoot().add(c1);
        ofExport.setFormat("JSON");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "{",
                "  \"name\" : \"RootContext\",",
                "  \"id\" : \"__%%RootContext\",",
                "  \"rank\" : 0,",
                "  \"tasks\" : [ ],",
                "  \"contexts\" : [ {",
                "    \"name\" : \"c1\",",
                "    \"id\" : \"%%%%c1\",",
                "    \"rank\" : 0,",
                "    \"tasks\" : [ {",
                "      \"name\" : \"t2\",",
                "      \"id\" : \"%%%%t2\",",
                "      \"rank\" : 0,",
                "      \"note\" : \"line1\\nline2\",",
                "      \"deferDate\" : null,",
                "      \"dueDate\" : 1417046400000,",
                "      \"completionDate\" : null,",
                "      \"sequential\" : false,",
                "      \"flagged\" : false,",
                "      \"tasks\" : [ ],",
                "      \"parentTaskId\" : null,",
                "      \"contextId\" : null,",
                "      \"blocked\" : false,",
                "      \"available\" : true,",
                "      \"remaining\" : true",
                "    } ],",
                "    \"contexts\" : [ {",
                "      \"name\" : \"c2\",",
                "      \"id\" : \"%%%%c2\",",
                "      \"rank\" : 0,",
                "      \"tasks\" : [ {",
                "        \"name\" : \"t3\",",
                "        \"id\" : \"%%%%t3\",",
                "        \"rank\" : 0,",
                "        \"note\" : \"line1\\nline2\",",
                "        \"deferDate\" : null,",
                "        \"dueDate\" : null,",
                "        \"completionDate\" : 1417046400000,",
                "        \"sequential\" : false,",
                "        \"flagged\" : false,",
                "        \"tasks\" : [ {",
                "          \"name\" : \"t4\",",
                "          \"id\" : \"%%%%t4\",",
                "          \"rank\" : 0,",
                "          \"note\" : \"line1\\nline2\",",
                "          \"deferDate\" : null,",
                "          \"dueDate\" : null,",
                "          \"completionDate\" : 1417046400000,",
                "          \"sequential\" : false,",
                "          \"flagged\" : false,",
                "          \"tasks\" : [ ],",
                "          \"parentTaskId\" : null,",
                "          \"contextId\" : null,",
                "          \"blocked\" : false,",
                "          \"available\" : false,",
                "          \"remaining\" : false",
                "        } ],",
                "        \"parentTaskId\" : null,",
                "        \"contextId\" : null,",
                "        \"blocked\" : false,",
                "        \"available\" : false,",
                "        \"remaining\" : false",
                "      }, {",
                "        \"name\" : \"t4\",",
                "        \"id\" : \"%%%%t4\",",
                "        \"rank\" : 0,",
                "        \"note\" : \"line1\\nline2\",",
                "        \"deferDate\" : null,",
                "        \"dueDate\" : null,",
                "        \"completionDate\" : 1417046400000,",
                "        \"sequential\" : false,",
                "        \"flagged\" : false,",
                "        \"tasks\" : [ ],",
                "        \"parentTaskId\" : null,",
                "        \"contextId\" : null,",
                "        \"blocked\" : false,",
                "        \"available\" : false,",
                "        \"remaining\" : false",
                "      } ],",
                "      \"contexts\" : [ ],",
                "      \"parentContextId\" : null,",
                "      \"active\" : false,",
                "      \"allowsNextAction\" : false",
                "    } ],",
                "    \"parentContextId\" : null,",
                "    \"active\" : false,",
                "    \"allowsNextAction\" : false",
                "  } ],",
                "  \"parentContextId\" : null,",
                "  \"active\" : false,",
                "  \"allowsNextAction\" : false",
                "}",
            }, out.toString().split("\n"));
    }
}
