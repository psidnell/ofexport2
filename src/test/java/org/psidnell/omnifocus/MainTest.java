package org.psidnell.omnifocus;

import org.junit.Test;

public class MainTest {
    @Test
    public void test() throws Exception {
        Main.main(new String[] {"-e", "{name: 'Hello'}", "-p", "Home", "-c", "Home", "-i"});
    }
    
    @Test
    public void test2() throws Exception {
        Main.main(new String[] {"-p", "ofexport2"});
    }

    @Test
    public void testHelp() throws Exception {
        Main.main(new String[] { "-h" });
    }
}
