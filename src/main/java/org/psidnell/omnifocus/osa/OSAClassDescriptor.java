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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class OSAClassDescriptor {

    private static final String STD_ADAPTATION = "o.%s()";
    private final Map<String, OSAPropertyDescriptor> properties = new TreeMap<>();
    private final String objectName;
    
    public OSAClassDescriptor (String objectName) throws ClassNotFoundException {
        this (Class.forName("org.psidnell.omnifocus.model." + objectName));
    }
    public OSAClassDescriptor (Class<?> clazz) throws ClassNotFoundException {
        this.objectName = clazz.getSimpleName();
        for (Method m : clazz.getMethods()) {
            String methodName = m.getName();
            
            if (m.getAnnotation(OSAIgnore.class) == null &&
                methodName.startsWith("get") && methodName.length() > 3 &&
                !methodName.equals("getClass") &&
                m.getParameters().length == 0) {
                
                char firstChar = Character.toLowerCase(methodName.charAt(3));
                String remainder = methodName.length() == 4 ? "" : methodName.substring(4);
                String propName = firstChar + remainder;                
                String adaptation;
                
                OSACollection collAnnot = m.getAnnotation(OSACollection.class);
                if (collAnnot != null) {
                    adaptation = "map" + collAnnot.type().getSimpleName() + "Array(o.%s)";
                }
                else {
                    adaptation = STD_ADAPTATION;
                }
                
                OSAAdaptation wrapAnnot = m.getAnnotation(OSAAdaptation.class);
                if (wrapAnnot != null) {
                    adaptation = wrapAnnot.pattern();
                }
                
                OSAPropertyDescriptor pd = new OSAPropertyDescriptor(propName, adaptation);
                properties.put(propName, pd);
                
                OSADefaultValue defValAnnot = m.getAnnotation(OSADefaultValue.class);
                if (defValAnnot != null) {
                    String value = defValAnnot.value();
                    override (propName, value);
                }
            }
        }
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
        if (pd != null) {
            pd.setGetter (val);
        }
        else {
            throw new IllegalArgumentException (objectName + " has no such property: " + attrib);
        }
    }
    public String getObjectName() {
        return objectName;
    }
    public OSAPropertyDescriptor getProperty(String name) {
        return properties.get(name);
    }
}
