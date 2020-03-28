/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Nigel Currie
 */
public class Sentence {
    private final String rawText;
    private List<Word> words;

    public Sentence(final String rawText) {
        this.rawText = rawText;
    }

    public String raw() {
        return rawText;
    }
    
    public String text() {
        if (words == null) {
            parseSentence();
        }
        
        StringBuilder buf = new StringBuilder();
        Iterator<Word> iter = words.iterator();
        while (iter.hasNext()) {
            buf.append(iter.next().text());
            if (iter.hasNext()) {
                buf.append(" ");
            }
        }
        
        return buf.toString();
    }
    
    public int wordCount() {
        if (words == null) {
            parseSentence();
        }
        
        if (words != null) {
            return words.size();
        }
        
        return 0;
    }
    
    private void parseSentence() {
        words = new ArrayList<>();
        String[] wordsText = rawText.split(" ");
        for (String word : wordsText) {
            words.add(new Word(word));
        }
    }
}
