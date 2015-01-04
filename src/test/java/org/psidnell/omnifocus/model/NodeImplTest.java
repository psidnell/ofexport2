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
package org.psidnell.omnifocus.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.springframework.context.ApplicationContext;

public class NodeImplTest {

    private NodeFactory nodeFactory;

    @Before
    public void setup () {
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        nodeFactory = appContext.getBean("nodefactory", NodeFactory.class);
    }

    @Test
    public void testIsRootFolder () {

        Folder f1 = nodeFactory.createFolder("f1");
        assertTrue (f1.isRoot());

        Folder f2 = nodeFactory.createFolder("f2");
        assertTrue (f2.isRoot());
        f1.add(f2);
        assertFalse (f2.isRoot());
    }

    @Test
    public void testIsRootContext () {

        Context c1 = nodeFactory.createContext("c1");
        assertTrue (c1.isRoot());

        Context c2 = nodeFactory.createContext("c2");
        assertTrue (c2.isRoot());
        c1.add(c2);
        assertFalse (c2.isRoot());
    }
}
