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

import org.junit.Test;

public class MainTest {
    @Test
    public void test() throws Exception {
       // Main.main(new String[] {"-e", "{name: 'Hello'}", "-p", "Home", "-c", "Home", "-i"});
    }
    
    @Test
    public void test2() throws Exception {
        Main.main(new String[] {"-a", "All", "-T", "{name: 'README'}", "-p", "ofexport2", "-flagged", "false", "-format", "SimpleTextList"});
    }
    
    @Test
    public void test3() throws Exception {
        Main.main(new String[] {"-a", "All", "-p", "ofexport2", "-format", "TaskPaper"});
    }
    
    @Test
    public void test4() throws Exception {
        Main.main(new String[] {"-a", "All", "-f", "TestFolder", "-format", "TaskPaper"});
    }

    @Test
    public void testHelp() throws Exception {
        Main.main(new String[] { "-h" });
    }
}
