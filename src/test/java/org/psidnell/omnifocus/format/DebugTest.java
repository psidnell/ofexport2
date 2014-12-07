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

public class DebugTest extends FormatTest {

    @Test
    public void testProjectMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.getProjectRoot().add(f1);
        ofExport.setFormat("debug");
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        System.out.println (out);

        Diff.diff (new String[]
            {
                "Folder id:__%%RootFolder depth:0",
                ". -name:RootFolder",
                ". -folderCount:1",
                ". -active:true",
                ". -projectCount:0",
                ". -type:Folder",
                ". Folder id:%%%%f1 depth:1",
                ". . -name:f1",
                ". . -folderCount:0",
                ". . -active:true",
                ". . -projectCount:3",
                ". . -type:Folder",
                ". . Project id:%%%%p1 depth:2",
                ". . . -name:p1",
                ". . . -completedfalse",
                ". . . -completionDate:null",
                ". . . -contextName:null>",
                ". . . -deferDate:null",
                ". . . -dueDate:null",
                ". . . -estimatedMinutes:-1",
                ". . . -flagged:false",
                ". . . -remaining:true",
                ". . . -sequential:false",
                ". . . -status:status",
                ". . . -taskCount:3",
                ". . . -type:Project",
                ". . . -note:null",
                ". . . Task id:%%%%t1 depth:3",
                ". . . . -name:t1",
                ". . . . -available:false",
                ". . . . -blocked:false",
                ". . . . -completed:false",
                ". . . . -completionDate:null",
                ". . . . -contextName:null>",
                ". . . . -deferDate:2014-11-27",
                ". . . . -dueDate:null",
                ". . . . -estimatedMinutes:-1",
                ". . . . -flagged:true",
                ". . . . -available:false",
                ". . . . -projectTask:false",
                ". . . . -remaining:true",
                ". . . . -sequential:false",
                ". . . . -taskCount:0",
                ". . . . -type:Task",
                ". . . . -note:null",
                ". . . Task id:%%%%t2 depth:3",
                ". . . . -name:t2",
                ". . . . -available:false",
                ". . . . -blocked:false",
                ". . . . -completed:false",
                ". . . . -completionDate:null",
                ". . . . -contextName:c1",
                ". . . . -deferDate:null",
                ". . . . -dueDate:2014-11-27",
                ". . . . -estimatedMinutes:-1",
                ". . . . -flagged:false",
                ". . . . -available:false",
                ". . . . -projectTask:false",
                ". . . . -remaining:true",
                ". . . . -sequential:false",
                ". . . . -taskCount:0",
                ". . . . -type:Task",
                ". . . . -note:",
                "      line1",
                "      line2",
                "",
                ". . . Task id:%%%%t3 depth:3",
                ". . . . -name:t3",
                ". . . . -available:false",
                ". . . . -blocked:false",
                ". . . . -completed:true",
                ". . . . -completionDate:2014-11-27",
                ". . . . -contextName:c2",
                ". . . . -deferDate:null",
                ". . . . -dueDate:null",
                ". . . . -estimatedMinutes:-1",
                ". . . . -flagged:false",
                ". . . . -available:false",
                ". . . . -projectTask:false",
                ". . . . -remaining:false",
                ". . . . -sequential:false",
                ". . . . -taskCount:1",
                ". . . . -type:Task",
                ". . . . -note:",
                "      line1",
                "      line2",
                "",
                ". . . . Task id:%%%%t4 depth:4",
                ". . . . . -name:t4",
                ". . . . . -available:false",
                ". . . . . -blocked:false",
                ". . . . . -completed:true",
                ". . . . . -completionDate:2014-11-27",
                ". . . . . -contextName:c2",
                ". . . . . -deferDate:null",
                ". . . . . -dueDate:null",
                ". . . . . -estimatedMinutes:-1",
                ". . . . . -flagged:false",
                ". . . . . -available:false",
                ". . . . . -projectTask:false",
                ". . . . . -remaining:false",
                ". . . . . -sequential:false",
                ". . . . . -taskCount:0",
                ". . . . . -type:Task",
                ". . . . . -note:",
                "        line1",
                "        line2",
                "",
                ". . Project id:%%%%p2 depth:2",
                ". . . -name:p2",
                ". . . -completedfalse",
                ". . . -completionDate:2014-11-27",
                ". . . -contextName:null>",
                ". . . -deferDate:null",
                ". . . -dueDate:2014-11-27",
                ". . . -estimatedMinutes:-1",
                ". . . -flagged:true",
                ". . . -remaining:true",
                ". . . -sequential:false",
                ". . . -status:status",
                ". . . -taskCount:0",
                ". . . -type:Project",
                ". . . -note:",
                "    line1",
                "    line2",
                "",
                ". . Project id:%%%%p3 depth:2",
                ". . . -name:p3",
                ". . . -completedfalse",
                ". . . -completionDate:null",
                ". . . -contextName:null>",
                ". . . -deferDate:null",
                ". . . -dueDate:null",
                ". . . -estimatedMinutes:-1",
                ". . . -flagged:false",
                ". . . -remaining:true",
                ". . . -sequential:false",
                ". . . -status:status",
                ". . . -taskCount:0",
                ". . . -type:Project",
                ". . . -note:null",
            }, out.toString().split("\n"));
    }

    @Test
    public void testContextMode () throws Exception {
        OFExport ofExport = new OFExport();
        ofExport.setProjectMode(false);
        ofExport.setFormat("debug");
        ofExport.getContextRoot().add(c1);
        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        System.out.println (out);

        Diff.diff (new String[]
            {
                "Task id:__%%RootContext depth:0",
                ". -name:RootContext",
                ". -contextCount:0",
                ". -active:true",
                ". -onHold:false",
                ". -dropped:false",
                ". -taskCount:0",
                ". -type:Context",
                ". Task id:%%%%c1 depth:1",
                ". . -name:c1",
                ". . -contextCount:1",
                ". . -active:true",
                ". . -onHold:false",
                ". . -dropped:false",
                ". . -taskCount:1",
                ". . -type:Context",
                ". . Task id:%%%%c2 depth:2",
                ". . . -name:c2",
                ". . . -contextCount:2",
                ". . . -active:true",
                ". . . -onHold:false",
                ". . . -dropped:false",
                ". . . -taskCount:2",
                ". . . -type:Context",
                ". . . Task id:%%%%t3 depth:3",
                ". . . . -name:t3",
                ". . . . -available:false",
                ". . . . -blocked:false",
                ". . . . -completed:true",
                ". . . . -completionDate:2014-11-27",
                ". . . . -contextName:c2",
                ". . . . -deferDate:null",
                ". . . . -dueDate:null",
                ". . . . -estimatedMinutes:-1",
                ". . . . -flagged:false",
                ". . . . -available:false",
                ". . . . -projectTask:false",
                ". . . . -remaining:false",
                ". . . . -sequential:false",
                ". . . . -taskCount:1",
                ". . . . -type:Task",
                ". . . . -note:",
                "      line1",
                "      line2",
                "",
                ". . . Task id:%%%%t4 depth:3",
                ". . . . -name:t4",
                ". . . . -available:false",
                ". . . . -blocked:false",
                ". . . . -completed:true",
                ". . . . -completionDate:2014-11-27",
                ". . . . -contextName:c2",
                ". . . . -deferDate:null",
                ". . . . -dueDate:null",
                ". . . . -estimatedMinutes:-1",
                ". . . . -flagged:false",
                ". . . . -available:false",
                ". . . . -projectTask:false",
                ". . . . -remaining:false",
                ". . . . -sequential:false",
                ". . . . -taskCount:0",
                ". . . . -type:Task",
                ". . . . -note:",
                "      line1",
                "      line2",
                "",
                ". . Task id:%%%%t2 depth:2",
                ". . . -name:t2",
                ". . . -available:false",
                ". . . -blocked:false",
                ". . . -completed:false",
                ". . . -completionDate:null",
                ". . . -contextName:c1",
                ". . . -deferDate:null",
                ". . . -dueDate:2014-11-27",
                ". . . -estimatedMinutes:-1",
                ". . . -flagged:false",
                ". . . -available:false",
                ". . . -projectTask:false",
                ". . . -remaining:true",
                ". . . -sequential:false",
                ". . . -taskCount:0",
                ". . . -type:Task",
                ". . . -note:",
                "    line1",
                "    line2",
            }, out.toString().split("\n"));
    }
}
