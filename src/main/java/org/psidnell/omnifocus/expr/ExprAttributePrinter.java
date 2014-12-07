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
package org.psidnell.omnifocus.expr;

import java.lang.reflect.Method;
import java.util.TreeMap;

import org.psidnell.omnifocus.model.NodeImpl;
import org.psidnell.omnifocus.util.StringUtils;

/**
 * @author psidnell
 *
 * Prints help from ExprAttribute annotations in a class.
 */
public class ExprAttributePrinter {

    private static class Line {
        private String type;
        private String nameAndArgs;
        private String help;

        Line (String type, String nameAndArgs, String help) {
            this.type = type;
            this.nameAndArgs = nameAndArgs;
            this.help = help;
        }
    }

    public static void print(Class<? extends NodeImpl> clazz) {
        TreeMap<String,Line> info = new TreeMap<>();
        for (Method m : clazz.getMethods()) {

            ExprAttribute attrib = m.getAnnotation(ExprAttribute.class);
            if (attrib != null) {
                String name = null;
                if (m.getName().startsWith("is")) {
                    name = m.getName().replaceAll("^is", "");
                } else if (m.getName().startsWith("get")) {
                    name = m.getName().replaceAll("^get", "");
                }
                else {
                    name = m.getName();
                }
                name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                String type = m.getReturnType().getSimpleName().toLowerCase();

                String nameAndArgs = name + processArgs(attrib);
                info.put(nameAndArgs, new Line(type, nameAndArgs, attrib.help()));
            }
        }

        int maxTypeLen = 0;
        int maxNameAndArgsLen = 0;

        System.out.println(clazz.getSimpleName() + ":");
        for (Line line : info.values()) {
            maxTypeLen = Math.max(maxTypeLen, line.type.length());
            maxNameAndArgsLen = Math.max(maxNameAndArgsLen, line.nameAndArgs.length());
        }
        for (Line line : info.values()) {
            System.out.println ("    " + padL(line.type, maxTypeLen) + " " + padR(line.nameAndArgs, 1 + maxNameAndArgsLen) + ": " + line.help);
        }
    }

    private static String padL(String val, int pad) {
        String result = val;
        while (result.length() < pad) {
            result = " " + result;
        }
        return result;
    }

    private static String padR(String val, int pad) {
        String result = val;
        while (result.length() < pad) {
            result = result + " ";
        }
        return result;
    }

    private static String processArgs(ExprAttribute attrib) {
        if (attrib.args().length == 0) {
            return "";
        }
        else {
            return StringUtils.join(attrib.args(), ",", " (",")");
        }
    }
}
