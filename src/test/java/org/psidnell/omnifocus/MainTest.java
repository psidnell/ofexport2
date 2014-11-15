package org.psidnell.omnifocus;

import org.junit.Test;

public class MainTest {
    @Test
    public void test() throws Exception {
        Main.main(new String[] {"-e", "{name: 'Hello'}", "-p", "Home", "-c", "Home", "-i"});
    }
    
    
    @Test
    public void test2() throws Exception {
        Main.main(new String[] {"-a", "All", "-p", "ofexport2", "-f", "SimpleTextList"});
    }
    
    @Test
    public void test3() throws Exception {
        Main.main(new String[] {"-a", "All", "-p", "ofexport2", "-f", "TaskPaper"});
    }

    @Test
    public void testHelp() throws Exception {
        Main.main(new String[] { "-h" });
    }
}
