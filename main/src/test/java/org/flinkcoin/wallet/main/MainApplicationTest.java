package org.flinkcoin.wallet.main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainApplicationTest {
    
    public MainApplicationTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of main method, of class MainApplication.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        MainApplication.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of configure method, of class MainApplication.
     */
    @Test
    public void testConfigure() {
        System.out.println("configure");
        MainApplication instance = null;
        instance.configure();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of init method, of class MainApplication.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        MainApplication instance = null;
        instance.init();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of shutdownHook method, of class MainApplication.
     */
    @Test
    public void testShutdownHook() {
        System.out.println("shutdownHook");
        MainApplication instance = null;
        instance.shutdownHook();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class MainApplication.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        MainApplication instance = null;
        instance.start();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class MainApplication.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        MainApplication instance = null;
        instance.stop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
