/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

/**
 *
 * @author nigel_2
 */
public class BitString {
    private byte[] data;
    
    public BitString(final int length) {
        data = new byte[length];
    }

    public BitString(final byte[] indata) {
        this.data = indata;
    }
    
    public BitString(final String strData) {
        if (strData != null) {
            int len = strData.length();
            data = new byte[len]; 
            for (int i = 0; i < len; i++) {
                if (strData.charAt(i) == '0') {
                    data[i] = 0;
                } else {
                    data[i] = 1;
                }
            }
        }
    }
    
    public void set(final int index, final boolean value) {
        if (data == null || (data.length - 1) < index) {
            return;
        }
        
        data[index] = (value) ? (byte) 1 : (byte) 0;
    }
    
    @Override
    public String toString() {
        if (data != null) {
            return toBitString();
        }
        
        return null;
    }

    public int countOnes() {
        if (data == null) {
            return 0;
        }
        
        int len = data.length;
        int count = 0;
        for (int i = 0; i < len; i++) {
            count += data[i] ;
        }
        
        return count;
    }

    byte[] getData() {
        return data;
    }

    void flipBit(final int index) {
        if (data == null || index < 0 || index >= data.length) {
            return;
        }
        
        data[index] = (data[index] == 1) ? (byte) 0 : (byte) 1;
    }

    int intValue() {
        // Try reversing the array, then iterating through it to calculate the 
        // value. The value = sum(data[i] * 2^i)
        if (data == null) {
            return 0;
        }
        
        // TODO: What if the value overflows an int?
        int value = 0;
        int maxIndex = data.length - 1;
        // Iterate through the array from the end to calculate the value. 
        // The value = sum(data[i] * 2^(maxIndex - i))
        for (int i = maxIndex; i >= 0; i--) {
            value += (data[i] * Math.pow(2.0, (double) (maxIndex - i)));
        }
                
        return value;
    }
    
    public String toBitString() {
        if (data == null) {
            return null;
        }
        
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            buf.append(b);
        }
        
        return buf.toString();
     }
}
