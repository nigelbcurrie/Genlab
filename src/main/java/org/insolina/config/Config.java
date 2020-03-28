/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for all insolina classes.
 * 
 * @author Nigel Currie
 */
public class Config {
    // TODO: Replace this with proper configuration from TOML file
    private static final Map<String, Object> PROPERTIES = new HashMap<>();
    
    static {
        PROPERTIES.put("cmudict.jdbc.url", "jdbc:sqlite:C:/Apps/JavaProjects/Basho/basho.db");
        PROPERTIES.put("brown.corpus.location", "C:/Apps/JavaProjects/Basho/Corpora/brown");
        PROPERTIES.put("nlp.parsing.model", "C:/Apps/Apache OpenNLP/apache-opennlp-1.8.4/bin/en-parser-chunking.bin");
    }
    
    /**
     * Get a string config attribute
     * 
     * @param name the name of the attribute
     * @return the property value
     */
    public static String getString(final String name) {
        return (String) PROPERTIES.get(name);
    }
}
