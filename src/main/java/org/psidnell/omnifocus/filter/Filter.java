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
package org.psidnell.omnifocus.filter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {

    public static String and(String... exprs) {
        List<String> nonNullExprs = Arrays.asList(exprs)
                .stream()
                .filter((x) -> x != null)
                .collect(Collectors.toList());
        
        if (nonNullExprs.size() == 1) {
            return nonNullExprs.get(0);
        }

        StringBuilder result = new StringBuilder();
        result.append("{_and:[");
        boolean first = true;
        for (String expr : nonNullExprs) {
            result.append(first ? "" : ',');
            result.append(expr);
            first = false;
        }
        result.append("]}");
        return result.toString();
    }
}
