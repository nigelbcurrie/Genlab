/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

/**
 *
 * @author Nigel Currie
 */
public class Word {
    private String text;
    private String tag;

    public Word(final String word) {
        String[] elems = word.split("/");
        text = elems[0];
        if (elems.length >= 2) {
            tag = elems[1];
        }
    }
    
    public String text() {
        return text;
    }
    
    public String tag() {
        return tag;
    }
}
