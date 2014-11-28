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
package org.psidnell.omnifocus.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class IOUtils {

    public static Writer systemOutWriter() {
        return closeProofWriter(new OutputStreamWriter(System.out));
    }

    public static Writer closeProofWriter(Writer out) {
        return new Writer(out) {
            @Override
            public void close() throws IOException {
                // Don't want to close underlying writer
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                out.write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
                out.flush();
            }
        };
    }
}
