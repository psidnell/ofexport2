package org.psidnell.omnifocus.filter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    
    public static String and (String... exprs) {
        List<String> nonNullExprs = Arrays.asList(exprs).stream().filter((x)->x!=null).collect (Collectors.toList());
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
        result.append ("]}");
        return result.toString();
    }
}
