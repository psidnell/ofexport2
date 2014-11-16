package org.psidnell.omnifocus.osa;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.psidnell.omnifocus.model.Folder;


public class OSATest {

    @Test
    public void testAnalyse () {
        OSAClassDescriptor desc = OSA.analyse (Dummy.class);
        String fn = desc.createMapperFunctions ();
        
        assertEquals (
            "function mapDummy(o) {\n" +
            "  return {\n" +
            "    foo: o.foo(),\n" +
            "    x: o.x(),\n" +
            "  };\n" +
            "}\n" +
            "function mapDummyArray (a) {var m=[];for (i in a){m.push(mapDummy(a[i]));}return m;}\n\n",
            fn);
    }
    
    @Test
    public void testParseWithOverrides () throws ClassNotFoundException {
        String expressions[] = {"Folder:projects=foo,folders=bar.whose({x:'y,\\'z'})"};
        Map<String, OSAClassDescriptor> descriptors = new HashMap<>();
        descriptors.put("Folder", OSA.analyse(Folder.class));
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
    public void testSplitOnCommas () {
        assertEquals ("[a]", OSA.splitOnCommas("a").toString());
        assertEquals ("[a, b, c]", OSA.splitOnCommas("a,b,c").toString());
        assertEquals ("[a','b, c]", OSA.splitOnCommas("a','b,c").toString());
        assertEquals ("[a\\', b, c]", OSA.splitOnCommas("a\\',b,c").toString());
    }

    public static class Dummy {
        public String getX() {
            return "";
        }
        public int getFoo () {
            return 0;
        }
    }
}
