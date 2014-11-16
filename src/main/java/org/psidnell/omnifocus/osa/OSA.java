package org.psidnell.omnifocus.osa;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class OSA {

    public static String parse(Map<String, OSAClassDescriptor> descriptors, List<String> expression) throws ClassNotFoundException {
        StringBuilder mapFunctions = new StringBuilder ();
        
        for (String node : expression) {
            int colon = node.indexOf(':');
            String nodeName = node.substring(0, colon);
            
            OSAClassDescriptor desc = descriptors.get(nodeName);
            
            String remainder = node.substring(colon + 1);
            for (String attribVal : splitOnCommas (remainder)) {
                String bits[] = attribVal.split("=");
                String attrib = bits[0];
                String val = bits[1];
                
                desc.override (attrib, val);
            }
            
            mapFunctions.append(desc.createMapperFunctions());
        }
        return mapFunctions.toString();
    }
    
    public static OSAClassDescriptor analyse(Class<?> clazz) {
        TreeMap<String, OSAPropertyDescriptor> properties = new TreeMap<>();
        for (Method m : clazz.getMethods()) {
            String methodName = m.getName();
            
            if (m.getAnnotation(OSAIgnore.class) == null &&
                methodName.startsWith("get") && methodName.length() > 3 &&
                !methodName.equals("getClass") &&
                m.getParameters().length == 0) {
                
                char firstChar = Character.toLowerCase(methodName.charAt(3));
                String remainder = methodName.length() == 4 ? "" : methodName.substring(4);
                String propName = firstChar + remainder;
                
                String nullGetter;
                String adaptation;
                
                OSACollection collAnnot = m.getAnnotation(OSACollection.class);
                if (collAnnot != null) {
                    nullGetter = "[]";
                    adaptation = "map" + collAnnot.type().getSimpleName() + "Array(o.%s)";
                }
                else {
                    nullGetter = "null";
                    adaptation = "o.%s()";
                }
                
                OSAAdaptation wrapAnnot = m.getAnnotation(OSAAdaptation.class);
                if (wrapAnnot != null) {
                    adaptation = wrapAnnot.pattern();
                }
                
                OSAPropertyDescriptor pd = new OSAPropertyDescriptor(propName, nullGetter, adaptation);
                properties.put(propName, pd);
            }
        }
        return new OSAClassDescriptor(clazz.getSimpleName(), properties);
    }
    
    protected static List<String> splitOnCommas (String str) {
        boolean inString = false;
        boolean escaped = false;
        StringBuilder value = new StringBuilder();
        LinkedList<String> result = new LinkedList<>();
        for (int i = 0; i < str.length(); i++) {
            char c=str.charAt(i);
            if (escaped) {
                escaped = false;
                value.append(c);
            }
            else if (inString) {
                value.append(c);
                if (c == '\'') {
                    inString = false;
                }
            }
            else {
                switch (c) {
                    case '\\':
                        escaped = true;
                        value.append(c);
                        break;
                    case '\'' :
                        inString = true;
                        value.append(c);
                        break;
                    case ',':
                        result.add(value.toString());
                        value = new StringBuilder();
                        break;
                    default:
                       value.append(c);
                       break;
                }
            }
        }
        if (value.length() > 0) {
            result.add(value.toString());
        }
        return result;
        
    }

}
