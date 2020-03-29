/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.config;

import com.github.jezza.Toml;
import com.github.jezza.TomlTable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configuration for all insolina classes. Currently using Toml for the config file, just to 
 * try out something new. Toml definition can be found here:
 * 
 * https://github.com/toml-lang/toml
 * 
 * The actual implementation I'm currently using is this, just because it doesn't introduce any extra 
 * dependencies:
 * 
 * https://github.com/Jezza/toml
 * 
 * The main problem with using toml rather than xml or a simple properties file is that I'm adding 
 * in a dependency that's not really very essential.
 * 
 * @author Nigel Currie
 */
public class Config {
    private final static Config SINGLETON = new Config();
    private TomlTable toml;
    
    /**
     * Private constructor to stop this being instantiated by anyone else
     * 
     * @throws RuntimeException 
     */
    private Config() throws RuntimeException {
        String configFile = System.getProperty("genlab.config");
        if (configFile == null) {
            String usage = "-Dgenlab.config=\"<toml file>\"";
            throw new RuntimeException("The genlab config file has not been defined. Define it on the command line like this : " + usage);
        }
        
        try {
            InputStream inStream = new FileInputStream(configFile);
            toml = Toml.from(inStream);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Get the single Config object
     * 
     * @return the single Config object
     */
    private static Config get() {
        return SINGLETON;
    }
     
    /**
     * Get a string config attribute
     * 
     * @param name the name of the attribute
     * @return the property value
     */
    public static String getString(final String name) {
        Config config = Config.get();
        Object property = config.toml.get(name);
        if (property != null) {
            return property.toString();
        }
        
        return null;
    }
}
