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
package org.psidnell.omnifocus.osa;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Task;


public class OSATest {

    @Test
    public void testAnalyse () throws ClassNotFoundException {
        OSAClassDescriptor desc = new OSAClassDescriptor (Dummy.class);
        String fn = desc.createMapperFunctions ();
        
        assertEquals (
            "function mapDummy(o) {\n" +
            "  return {\n" +
            "    foo: doThing(foo),\n" +
            "    x: o.x(),\n" +
            "    z: mapTaskArray(o.z),\n" +
            "  };\n" +
            "}\n" +
            "function mapDummyArray (a) {var m=[];for (i in a){m.push(mapDummy(a[i]));}return m;}\n\n",
            fn);
    }
    
    @Test
    public void testParseWithOverrides () throws ClassNotFoundException {
        String expressions[] = {"Folder:projects=foo,folders=bar{x:'y,\\'z'}"};
        Map<String, OSAClassDescriptor> descriptors = new HashMap<>();
        descriptors.put("Folder", new OSAClassDescriptor(Folder.class.getSimpleName()));
        String fn = OSA.parse(descriptors , Arrays.asList(expressions));
        assertEquals (
            "function mapFolder(o) {\n" +
            "  return {\n" +
            "    folders: mapFolderArray(o.bar.whose({x:'y,\\'z'})),\n" +
            "    id: o.id(),\n" +
            "    name: o.name(),\n" +
            "    projects: mapProjectArray(o.foo),\n" +
            "  };\n" +
            "}\n" +
            "function mapFolderArray (a) {var m=[];for (i in a){m.push(mapFolder(a[i]));}return m;}\n" +
            "\n",
            fn);
    }
    
    @Test
    public void  TestSplitOn() {
        assertEquals ("[a]", OSA.splitOn("a", ',').toString());
        assertEquals ("[a, b, c]", OSA.splitOn("a,b,c", ',').toString());
        assertEquals ("[a','b, c]", OSA.splitOn("a','b,c", ',').toString());
        assertEquals ("[a\\', b, c]", OSA.splitOn("a\\',b,c", ',').toString());
    }

    public static class Dummy {
        public String getX() {
            return "";
        }
        @OSAAdaptation(pattern="doThing(%s)")
        public int getFoo () {
            return 0;
        }
        @OSACollection(type=Task.class)
        public List<Task> getZ() {
            return null;
        }
    }
}
