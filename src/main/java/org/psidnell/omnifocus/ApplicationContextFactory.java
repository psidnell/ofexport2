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
package org.psidnell.omnifocus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author psidnell
 *
 *         Utility factory class for building the default Spring application context.
 */
public class ApplicationContextFactory {

    public static final String CONFIG_XML = "/config.xml";
    private static final String CONFIG_PROPERTIES = "/config.properties";

    public static ApplicationContext getContext() {
        return new ClassPathXmlApplicationContext(CONFIG_XML);
    }

    public static Properties getConfigProperties() throws IOException {
        try (
            InputStream in = ApplicationContextFactory.class.getResourceAsStream(CONFIG_PROPERTIES)) {
            if (in == null) {
                throw new IOException("config not found");
            }
            Properties config = new Properties();
            config.load(in);
            return config;
        }
    }
}
