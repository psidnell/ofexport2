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
package org.psidnell.omnifocus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class ApplicationContextFactoryTest {

    @Test
    public void testPropertyOverrideFromCommandLine () {

        ApplicationContext beanFactory = ApplicationContextFactory.getContext();

        // The default value
        assertEquals ("7d", beanFactory.getBean("configparams", ConfigParams.class).getDueSoon());

        // Now override
        System.getProperties().put("dueSoon", "1d");

        beanFactory = ApplicationContextFactory.getContext();
        assertEquals ("1d", beanFactory.getBean("configparams", ConfigParams.class).getDueSoon());
    }
}
