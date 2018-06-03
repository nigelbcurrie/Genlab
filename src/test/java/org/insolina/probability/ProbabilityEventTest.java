/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.probability;

import org.insolina.probability.ProbabilityEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nigel_2
 */
public class ProbabilityEventTest {
    
    public ProbabilityEventTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of checkProbability method, of class ProbabilityEvent.
     */
    @Test
    public void testCheckProbability() {
        System.out.println("checkProbability");
        ProbabilityEvent instance = new ProbabilityEvent(0.7);
        int trues = 0;
        for (int i = 0; i < 100; i++) {
            if (instance.checkOccurrence()) {
                trues++;
            }
        }
        
        int lowerBound = 60;
        int upperBound = 80;
        
        System.out.println("How many occurrences: " + trues);
        assertTrue(trues > lowerBound);
        assertTrue(trues < upperBound);
    }
    
}
