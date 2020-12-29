/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nigel Currie
 */
public class Instantiator<T> {
    private Class<T> type;
    
    public Instantiator(final Class<T> typ) {
        this.type = typ;
    }
    
    public T newObject(final String className) {
        if (className == null) {
            return null;
        }
        
        try {
            Class c = Class.forName(className);
            Object o = c.newInstance();
            return type.cast(o);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException ex ) {
            Logger.getLogger(Instantiator.class.getName()).log(Level.WARNING, null, ex);
        }
        
        return null;
    }
}
