package org.psidnell.omnifocus;

import java.io.IOException;

import javax.script.ScriptException;

import org.junit.Test;

public class MainTest {
    @Test
    public void test () throws IOException, ScriptException {
        Main.main(new String[]{"p:Home","c:Home"});
    }
}
