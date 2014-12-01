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
package org.psidnell.omnifocus.cli;

import java.util.LinkedList;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;

/**
 * @author psidnell
 *
 *         For some unexplained reason the Parser.class strips leading and trailing quotes off the
 *         arguments in a fairly dumb way so if we use quoted arguments, they get broken. Adding an
 *         additional layer of quotes is a workaround.
 */
public class FixedParser extends BasicParser {

    @Override
    protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {

        LinkedList<String> result = new LinkedList<>();
        for (String arg : arguments) {
            if (!arg.startsWith("-")) {
                result.add('"' + arg + '"');
            } else {
                result.add(arg);
            }
        }

        return result.toArray(new String[result.size()]);
    }

}
