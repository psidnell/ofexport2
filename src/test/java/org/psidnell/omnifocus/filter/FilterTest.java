package org.psidnell.omnifocus.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class FilterTest {

    @Test
    public void testAnd () {
        assertEquals ("{_and:[a,b]}", Filter.and("a", "b"));
        assertEquals ("{_and:[a,b,c]}", Filter.and("a", "b", "c"));
        assertEquals ("a", Filter.and("a", null));
    }
}
