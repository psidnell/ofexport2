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
import java.util.TreeSet;

import org.psidnell.omnifocus.model.Node;

public class AttribPrinter {

    public static void print(Class<? extends Node> clazz) {
        TreeSet<String> info = new TreeSet<>();
        for (Method m : clazz.getMethods()) {

            ExprAttribute attrib = m.getAnnotation(ExprAttribute.class);
            if (attrib != null) {
                String name = null;
                if (m.getName().startsWith("is")) {
                    name = m.getName().replaceAll("^is", "");
                } else if (m.getName().startsWith("get")) {
                    name = m.getName().replaceAll("^get", "");
                }
                if (name != null) {
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                    String type = m.getReturnType().getSimpleName().toLowerCase();
                    info.add("    " + name + " (" + type + "): " + attrib.help());
                }
            }
        }
        System.out.println(clazz.getSimpleName() + ":");
        for (String line : info) {
            System.out.println(line);
        }
    }
}
