package org.psidnell.omnifocus.osa;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class OSAClassDescriptor {

    private Map<String, OSAPropertyDescriptor> properties;
    private String objectName;
    
    public OSAClassDescriptor(String objectName, TreeMap<String, OSAPropertyDescriptor> properties) {
        this.objectName = objectName;
        this.properties = properties;
    }

    public String createMapperFunctions() {
        StringBuilder result = new StringBuilder ();
        result.append("function map");
        result.append(objectName);
        result.append("(o) {\n");
        result.append("  return {\n");
        for (Entry<String, OSAPropertyDescriptor> e : properties.entrySet()) {
            OSAPropertyDescriptor pd = e.getValue();
            String propName = e.getKey();
            result.append ("    ");
            result.append (propName);
            result.append(": ");
            result.append(pd.getAdaptedGetter ());
            result.append(",\n");
        }
        result.append("  };\n");
        result.append("}\n");
        result.append("function map");
        result.append(objectName);
        result.append("Array (a) {var m=[];for (i in a){m.push(map");
        result.append(objectName);
        result.append("(a[i]));}return m;}\n\n");
        return result.toString();
    }

    public void override(String attrib, String val) {
        OSAPropertyDescriptor pd = properties.get(attrib);
        pd.setGetter (val);
    }
}
