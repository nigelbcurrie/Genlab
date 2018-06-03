/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

import org.insolina.genetics.BitString;
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
public class BitStringTest {
    
    public BitStringTest() {
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
     * Test of set method, of class BitString.
     */
    /*@Test
    public void testSet() {
        System.out.println("set");
        int index = 0;
        boolean value = false;
        BitString instance = null;
        instance.set(index, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of toString method, of class BitString.
     */
    /*@Test
    public void testToString() {
        System.out.println("toString");
        BitString instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of countOnes method, of class BitString.
     */
    /*@Test
    public void testCountOnes() {
        System.out.println("countOnes");
        BitString instance = null;
        int expResult = 0;
        int result = instance.countOnes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getData method, of class BitString.
     */
    /*@Test
    public void testGetData() {
        System.out.println("getData");
        BitString instance = null;
        byte[] expResult = null;
        byte[] result = instance.getData();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of flipBit method, of class BitString.
     */
    /*@Test
    public void testFlipBit() {
        System.out.println("flipBit");
        int index = 0;
        BitString instance = null;
        instance.flipBit(index);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of intValue method, of class BitString.
     */
    @Test
    public void testIntValue() {
        System.out.println("intValue");
        BitString instance = new BitString("10100");
        int expResult = 20;
        int result = instance.intValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of toBitString method, of class BitString.
     */
    /*@Test
    public void testToBitString() {
        System.out.println("toBitString");
        BitString instance = null;
        String expResult = "";
        String result = instance.toBitString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
}
