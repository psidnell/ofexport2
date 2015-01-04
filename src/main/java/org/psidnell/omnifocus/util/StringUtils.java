/*
 * Copyright 2015 Paul Sidnell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.psidnell.omnifocus.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * @author psidnell
 *
 * Things to do with Strings.
 *
 */
public class StringUtils {

    public static String join(Collection<String> data, String delimiter) {
        return join(data, new StringJoiner(delimiter));
    }

    public static String join(Collection<String> data, String delimiter, String prefix, String suffix) {
        return join(data, new StringJoiner(delimiter, prefix, suffix));
    }

    public static String join(String data[], String delimiter) {
        return join(Arrays.asList(data), new StringJoiner(delimiter));
    }

    public static String join(String data[], String delimiter, String prefix, String suffix) {
        return join(Arrays.asList(data), new StringJoiner(delimiter, prefix, suffix));
    }

    public static String join(Collection<String> data, StringJoiner sj) {
        data.stream().forEachOrdered((s) -> sj.add(s));
        return sj.toString();
    }

    public static String times(String str, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            result.append(str);
        }
        return result.toString();
    }
}
