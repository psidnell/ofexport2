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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class OSA {

    public static String parse(Map<String, OSAClassDescriptor> descriptors, List<String> expression) throws ClassNotFoundException {
        StringBuilder mapFunctions = new StringBuilder ();
        
        for (String node : expression) {
            int colon = node.indexOf(':');
            String nodeName = node.substring(0, colon);
            
            OSAClassDescriptor desc = descriptors.get(nodeName);
            
            String remainder = node.substring(colon + 1);
            for (String attribVal : splitOn (remainder, ',')) {
                String bits[] = attribVal.split("=");
                String attrib = bits[0];
                String val = bits[1];
                
                if (val.contains("{")) {
                    val = val.replaceFirst("\\{", ".whose({");
                    val = val + ")";
                }
                
                desc.override (attrib, val);
            }
            
            mapFunctions.append(desc.createMapperFunctions());
        }
        return mapFunctions.toString();
    }
    
    protected static List<String> splitOn (String str, char splitChar) {
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
            else if (c == splitChar) {
                result.add(value.toString());
                value = new StringBuilder();
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
