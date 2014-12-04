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

import java.text.ParseException;

import org.junit.Before;
import org.psidnell.omnifocus.expr.ExpressionFunctions;
import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Folder;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;

public abstract class FormatTest {

    protected Folder f1;
    protected Context c1;

    @Before
    public void setUp () throws ParseException {
        c1 = new Context ("c1");
        setId (c1);

        Context c2 = new Context ("c2");
        setId(c2);
        c1.add(c2);

        f1 = new Folder ("f1");
        setId(f1);

        Project p1 = new Project ("p1");
        p1.setStatus("status");
        setId(p1);
        f1.add(p1);

        Task t1 = new Task("t1");
        setId(t1);
        t1.setDeferDate(new ExpressionFunctions().date("2014-11-27"));
        t1.setFlagged(true);
        p1.add(t1);

        Task t2 = new Task("t2");
        setId(t2);
        t2.setDueDate(new ExpressionFunctions().date("2014-11-27"));
        t2.setNote("line1\nline2");
        c1.add(t2);
        p1.add(t2);

        Task t3 = new Task("t3");
        setId(t3);
        t3.setCompletionDate(new ExpressionFunctions().date("2014-11-27"));
        t3.setNote("line1\nline2");
        c2.add(t3);
        p1.add(t3);

        Task t4 = new Task("t4");
        setId(t4);
        t4.setCompletionDate(new ExpressionFunctions().date("2014-11-27"));
        t4.setNote("line1\nline2");
        c2.add(t4);
        t3.add(t4);

        Project p2 = new Project ("p2");
        p2.setStatus("status");
        setId(p2);
        p2.setFlagged(true);
        p2.setDueDate(new ExpressionFunctions().date("2014-11-27"));
        f1.add(p2);
        p2.setNote("line1\nline2");

        Project p3 = new Project ("p3");
        p3.setStatus("status");
        setId(p3);
        p2.setCompletionDate(new ExpressionFunctions().date("2014-11-27"));
        f1.add(p3);
    }

    private void setId(Node n) {
        n.setId("%%%%" + n.getName());
    }
}
