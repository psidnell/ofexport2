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
package org.psidnell.omnifocus.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.psidnell.omnifocus.model.Node;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author psidnell
 * 
 *  Formats the node structure using a FreeMarker template.
 *
 */
@SuppressWarnings("unused")
public class FreeMarkerFormatter implements Formatter {

    private static final String TEMPLATES = "/templates";
    private Template template;

    public FreeMarkerFormatter(String templateName) throws IOException {
        // If the resource doesn't exist abort so we can look elsewhere
        try (
            InputStream in = this.getClass().getResourceAsStream(TEMPLATES + "/" + templateName)) {
            if (in == null) {
                throw new IOException("Resource not found:" + templateName);
            }
        }
        
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), TEMPLATES);
        cfg.setTemplateLoader(templateLoader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        
        // This is fatal - bomb out of application
        try {
            template = cfg.getTemplate(templateName);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void format(Node root, Writer out) throws IOException, TemplateException {
        template.process(root, out);
    }
}
