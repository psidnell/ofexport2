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

import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.integrationtest.Diff;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.NodeFactory;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.FlattenFilter;
import org.psidnell.omnifocus.visitor.SimplifyFilter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

public class OFExportTest {

    private BeanFactory appContext;
    private ConfigParams config;
    private NodeFactory nodeFactory;

    @Before
    public void setup () {
        appContext = ApplicationContextFactory.getContext();
        config = appContext.getBean("configparams", ConfigParams.class);
        nodeFactory = appContext.getBean("nodefactory", NodeFactory.class);
    }

    @Test
    public void testEverythingIncludedByDefault () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");

        Task t = nodeFactory.createTask("t1");
        p1.add(t);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "  [ ] t1",
                "p2"
            }, out.toString().split("\n"));
    }

    @Test
    public void testImplicitIncludeProjectSubtreeWhenMatch () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");

        Task t = nodeFactory.createTask("t1");
        p1.add(t);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addProjectExpression("name=='p1'", true, true);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "  [ ] t1",
            }, out.toString().split("\n"));
    }

    @Test
    public void testExplicitIncludeProjectSubtreeWhenMatch () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");

        Task t = nodeFactory.createTask("t1");
        p1.add(t);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addProjectExpression("name=='p1'", true, true);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "  [ ] t1",
            }, out.toString().split("\n"));
    }

    @Test
    public void testIncludeFolderSubtreeWhenMatch () throws Exception {
        OFExport ofExport = newOFExport();

        Folder f1 = nodeFactory.createFolder("f1");

        Project p = nodeFactory.createProject("p");
        f1.add(p);

        Folder f2 = nodeFactory.createFolder("f2");

        ofExport.getProjectRoot().add(f1);
        ofExport.getProjectRoot().add(f2);

        ofExport.addFolderExpression("name=='f1'", true, true);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "f1",
                "  p",
            }, out.toString().split("\n"));
    }

    @Test
    public void testIncludeContextSubtreeWhenMatch () throws Exception {
        OFExport ofExport = newOFExport();
        ofExport.setProjectMode(false);

        Context c1 = nodeFactory.createContext("c1");

        Task t = nodeFactory.createTask("t1");
        c1.add(t);

        Context c2 = nodeFactory.createContext("c2");

        ofExport.getContextRoot().add(c1);
        ofExport.getContextRoot().add(c2);

        ofExport.addContextExpression("name=='c1'", true, true);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "c1",
                "  [ ] t1",
            }, out.toString().split("\n"));
    }

    @Test
    public void testIncludeTaskWhenMatch () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");

        Task t1 = nodeFactory.createTask("t1");
        p1.add(t1);

        Task t2 = nodeFactory.createTask("t2");
        t1.add (t2);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addTaskExpression("name=='t1'", true, true);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "  [ ] t1",
                "    [ ] t2"
            }, out.toString().split("\n"));
    }

    @Test
    public void testExcludeTaskWhenMatch () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");

        Task t1 = nodeFactory.createTask("t1");
        p1.add(t1);

        Task t2 = nodeFactory.createTask("t2");
        t1.add (t2);

        Task t3 = nodeFactory.createTask("t3");
        t2.add (t3);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addTaskExpression("name=='t2'", false, true);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "  [ ] t1",
                "p2"
            }, out.toString().split("\n"));
    }

    @Test
    public void testIncludeTaskSubTreeWhenMatch () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");

        Task t1 = nodeFactory.createTask("t1");
        p1.add(t1);

        Task t2 = nodeFactory.createTask("t2");
        t1.add (t2);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addTaskExpression("name=='t1'", true, true);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "  [ ] t1",
                "    [ ] t2",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortFoldersDefault () throws Exception {
        OFExport ofExport = newOFExport();

        Folder f1 = nodeFactory.createFolder("f1");
        f1.setRank(2);
        Folder f2 = nodeFactory.createFolder("f2");
        f2.setRank(1);
        Folder f3 = nodeFactory.createFolder("f3");
        f3.setRank(3);

        ofExport.getProjectRoot().add(f1);
        ofExport.getProjectRoot().add(f2);
        ofExport.getProjectRoot().add(f3);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "f2",
                "f1",
                "f3",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortFoldersByName () throws Exception {
        OFExport ofExport = newOFExport();

        Folder f1 = nodeFactory.createFolder("f1");
        f1.setRank(2);
        Folder f2 = nodeFactory.createFolder("f2");
        f2.setRank(1);
        Folder f3 = nodeFactory.createFolder("f3");
        f3.setRank(3);

        ofExport.getProjectRoot().add(f1);
        ofExport.getProjectRoot().add(f2);
        ofExport.getProjectRoot().add(f3);

        ofExport.addFolderComparator("name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "f1",
                "f2",
                "f3",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortFoldersByNameReversed () throws Exception {
        OFExport ofExport = newOFExport();

        Folder f1 = nodeFactory.createFolder("f1");
        f1.setRank(2);
        Folder f2 = nodeFactory.createFolder("f2");
        f2.setRank(1);
        Folder f3 = nodeFactory.createFolder("f3");
        f3.setRank(3);

        ofExport.getProjectRoot().add(f1);
        ofExport.getProjectRoot().add(f2);
        ofExport.getProjectRoot().add(f3);

        ofExport.addFolderComparator("r:name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "f3",
                "f2",
                "f1",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortProjectsDefault () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");
        p1.setRank(2);
        Project p2 = nodeFactory.createProject("p2");
        p2.setRank(1);
        Project p3 = nodeFactory.createProject("p3");
        p3.setRank(3);

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);
        ofExport.getProjectRoot().add(p3);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p2",
                "p1",
                "p3",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortProjectsByName () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");
        p1.setRank(2);
        Project p2 = nodeFactory.createProject("p2");
        p2.setRank(1);
        Project p3 = nodeFactory.createProject("p3");
        p3.setRank(3);

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);
        ofExport.getProjectRoot().add(p3);

        ofExport.addProjectComparator("name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "p2",
                "p3",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortProjectsByNameReversed () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");
        p1.setRank(2);
        Project p2 = nodeFactory.createProject("p2");
        p2.setRank(1);
        Project p3 = nodeFactory.createProject("p3");
        p3.setRank(3);

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);
        ofExport.getProjectRoot().add(p3);

        ofExport.addProjectComparator("r:name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p3",
                "p2",
                "p1",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortTasksDefault () throws Exception {
        OFExport ofExport = newOFExport();

        Task t1 = nodeFactory.createTask("t1");
        t1.setRank(2);
        Task t2 = nodeFactory.createTask("t2");
        t2.setRank(1);
        Task t3 = nodeFactory.createTask("t3");
        t3.setRank(3);

        Project p = nodeFactory.createProject("p");
        p.add(t1);
        p.add(t2);
        p.add(t3);

        ofExport.getProjectRoot().add(p);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p",
                "  [ ] t2",
                "  [ ] t1",
                "  [ ] t3"
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortTasksByName () throws Exception {
        OFExport ofExport = newOFExport();

        Task t1 = nodeFactory.createTask("t1");
        t1.setRank(2);
        Task t2 = nodeFactory.createTask("t2");
        t2.setRank(1);
        Task t3 = nodeFactory.createTask("t3");
        t3.setRank(3);

        Project p = nodeFactory.createProject("p");
        p.add(t1);
        p.add(t2);
        p.add(t3);

        ofExport.getProjectRoot().add(p);
        ofExport.addTaskComparator("name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
                {
                    "p",
                    "  [ ] t1",
                    "  [ ] t2",
                    "  [ ] t3"
                }, out.toString().split("\n"));
    }

    @Test
    public void testSortTasksByNameReversed () throws Exception {
        OFExport ofExport = newOFExport();

        Task t1 = nodeFactory.createTask("t1");
        t1.setRank(2);
        Task t2 = nodeFactory.createTask("t2");
        t2.setRank(1);
        Task t3 = nodeFactory.createTask("t3");
        t3.setRank(3);

        Project p = nodeFactory.createProject("p");
        p.add(t1);
        p.add(t2);
        p.add(t3);

        ofExport.getProjectRoot().add(p);

        ofExport.addTaskComparator("r:name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p",
                "  [ ] t3",
                "  [ ] t2",
                "  [ ] t1"
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortContextsDefault () throws Exception {
        OFExport ofExport = newOFExport();
        ofExport.setProjectMode(false);

        Context c1 = nodeFactory.createContext("c1");
        c1.setRank(2);
        Context c2 = nodeFactory.createContext("c2");
        c2.setRank(1);
        Context c3 = nodeFactory.createContext("c3");
        c3.setRank(3);

        Context c = nodeFactory.createContext("c");
        c.add(c1);
        c.add(c2);
        c.add(c3);

        ofExport.getContextRoot().add(c);

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "c",
                "  c2",
                "  c1",
                "  c3"
            }, out.toString().split("\n"));
    }

    @Test
    public void testSortContetsByName () throws Exception {
        OFExport ofExport = newOFExport();
        ofExport.setProjectMode(false);

        Context c1 = nodeFactory.createContext("c1");
        c1.setRank(2);
        Context c2 = nodeFactory.createContext("c2");
        c2.setRank(1);
        Context c3 = nodeFactory.createContext("c3");
        c3.setRank(3);

        Context c = nodeFactory.createContext("c");
        c.add(c1);
        c.add(c2);
        c.add(c3);

        ofExport.getContextRoot().add(c);
        ofExport.addContextComparator("name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
                {
                    "c",
                    "  c1",
                    "  c2",
                    "  c3"
                }, out.toString().split("\n"));
    }

    @Test
    public void testSortContextsByNameReversed () throws Exception {
        OFExport ofExport = newOFExport();
        ofExport.setProjectMode(false);

        Context c1 = nodeFactory.createContext("c1");
        c1.setRank(2);
        Context c2 = nodeFactory.createContext("c2");
        c2.setRank(1);
        Context c3 = nodeFactory.createContext("c3");
        c3.setRank(3);

        Context c = nodeFactory.createContext("c");
        c.add(c1);
        c.add(c2);
        c.add(c3);

        ofExport.getContextRoot().add(c);

        ofExport.addContextComparator("r:name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "c",
                "  c3",
                "  c2",
                "  c1"
            }, out.toString().split("\n"));
    }

    @Test
    public void testModifyNode () throws Exception {
        OFExport ofExport = newOFExport();

        Project p1 = nodeFactory.createProject("p1");

        Task t = nodeFactory.createTask("t1");
        p1.add(t);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(p1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addProjectExpression("name=='p1'", true, true);
        ofExport.addModifyExpression("type=='Project' && name='foo'+type");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "fooProject",
                "  [ ] t1",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSimplifyFolders () throws Exception {
        OFExport ofExport = newOFExport();

        Folder f1 = nodeFactory.createFolder("f1");

        Project p1 = nodeFactory.createProject("p1");
        f1.add(p1);

        Task t1 = nodeFactory.createTask("t1");
        p1.add(t1);

        Task t2 = nodeFactory.createTask("t2");
        t1.add (t2);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(f1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addFilter(new SimplifyFilter());
        ofExport.addProjectComparator("name");
        ofExport.addTaskComparator("name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "p1",
                "  [ ] t1",
                "  [ ] t2",
                "p2",
            }, out.toString().split("\n"));
    }

    @Test
    public void testSimplifyContexts () throws Exception {
        OFExport ofExport = newOFExport();
        ofExport.setProjectMode(false);

        Context c1 = nodeFactory.createContext("c1");
        c1.setRank(2);
        Context c2 = nodeFactory.createContext("c2");
        c2.setRank(1);
        Context c3 = nodeFactory.createContext("c3");
        c3.setRank(3);

        Context c = nodeFactory.createContext("c");
        c.add(c1);
        c.add(c2);
        c.add(c3);

        Task t1 = nodeFactory.createTask("t1");
        c2.add(t1);
        Task t2 = nodeFactory.createTask("t2");
        c2.add(t2);


        ofExport.getContextRoot().add(c);
        ofExport.addContextComparator("name");
        ofExport.addTaskComparator("name");
        ofExport.addFilter(new SimplifyFilter());

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
                {
                    "c",
                    "c1",
                    "c2",
                    "  [ ] t1",
                    "  [ ] t2",
                    "c3"
                }, out.toString().split("\n"));
    }

    @Test
    public void testPruneFolders () throws Exception {
        OFExport ofExport = newOFExport();

        Folder f1 = nodeFactory.createFolder("f1");

        Folder f2 = nodeFactory.createFolder("f2");

        Folder f3 = nodeFactory.createFolder("f3");

        Folder f4 = nodeFactory.createFolder("f4");

        Project p1 = nodeFactory.createProject("p1");
        f1.add(p1);

        Task t1 = nodeFactory.createTask("t1");
        p1.add(t1);

        Task t2 = nodeFactory.createTask("t2");
        t1.add (t2);

        Project p2 = nodeFactory.createProject("p2");

        Project p3 = nodeFactory.createProject("p3");
        f3.add(p3);
        f3.add(f4);

        ofExport.getProjectRoot().add(f1);
        ofExport.getProjectRoot().add(f2);
        ofExport.getProjectRoot().add(f3);
        ofExport.getProjectRoot().add(p2);

        ofExport.addPruneFilter();
        ofExport.addProjectComparator("name");
        ofExport.addTaskComparator("name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "f1",
                "  p1",
                "    [ ] t1",
                "      [ ] t2",
            }, out.toString().split("\n"));
    }

    @Test
    public void testPruneContexts () throws Exception {
        OFExport ofExport = newOFExport();
        ofExport.setProjectMode(false);

        Context c1 = nodeFactory.createContext("c1");
        c1.setRank(2);
        Context c2 = nodeFactory.createContext("c2");
        c2.setRank(1);
        Context c3 = nodeFactory.createContext("c3");
        c3.setRank(3);

        Context c = nodeFactory.createContext("c");
        c.add(c1);
        c.add(c2);
        c.add(c3);

        Task t1 = nodeFactory.createTask("t1");
        c2.add(t1);
        Task t2 = nodeFactory.createTask("t2");
        c2.add(t2);


        ofExport.getContextRoot().add(c);
        ofExport.addContextComparator("name");
        ofExport.addTaskComparator("name");
        ofExport.addPruneFilter();

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
                {
                    "c",
                    "  c2",
                    "    [ ] t1",
                    "    [ ] t2",
                }, out.toString().split("\n"));
    }

    @Test
    public void testFlattenFolders () throws Exception {

        OFExport ofExport = newOFExport();

        Folder f1 = nodeFactory.createFolder("f1");

        Project p1 = nodeFactory.createProject("p1");
        f1.add(p1);

        Task t1 = nodeFactory.createTask("t1");
        p1.add(t1);

        Task t2 = nodeFactory.createTask("t2");
        t1.add (t2);

        Project p2 = nodeFactory.createProject("p2");

        ofExport.getProjectRoot().add(f1);
        ofExport.getProjectRoot().add(p2);

        ofExport.addFilter(new FlattenFilter(nodeFactory, config));
        ofExport.addProjectComparator("name");
        ofExport.addTaskComparator("name");

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
            {
                "Tasks",
                "  [ ] t1",
                "  [ ] t2",
            }, out.toString().split("\n"));
    }

    @Test
    public void testFlattenContexts () throws Exception {

        OFExport ofExport = newOFExport();
        ofExport.setProjectMode(false);

        Context c1 = nodeFactory.createContext("c1");
        c1.setRank(2);
        Context c2 = nodeFactory.createContext("c2");
        c2.setRank(1);
        Context c3 = nodeFactory.createContext("c3");
        c3.setRank(3);

        Context c = nodeFactory.createContext("c");
        c.add(c1);
        c.add(c2);
        c.add(c3);

        Task t1 = nodeFactory.createTask("t1");
        c2.add(t1);
        Task t2 = nodeFactory.createTask("t2");
        c2.add(t2);

        ofExport.getContextRoot().add(c);
        ofExport.addContextComparator("name");
        ofExport.addTaskComparator("name");
        ofExport.addFilter(new FlattenFilter(nodeFactory, config));

        ofExport.process();
        StringWriter out = new StringWriter();
        ofExport.write(out);

        Diff.diff (new String[]
                {
                    "Tasks",
                    "  [ ] t1",
                    "  [ ] t2",
                }, out.toString().split("\n"));
    }

    private OFExport newOFExport() {
        OFExport ofExport = new OFExport();
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        NodeFactory nodeFactory = appContext.getBean("nodefactory", NodeFactory.class);
        ofExport.setNodeFactory(nodeFactory);
        ofExport.init();
        return ofExport;
    }
}
