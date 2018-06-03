/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.probability;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Nigel Currie
 * 
 * Utility class to check occurrence of an event given the defined
 * probability. If you instantiate this class with a probability of 0.7, 
 * and call checkOccurrence 100 times, it will return true around 70 times.
 */
public class ProbabilityEvent {
    private final double probability;
    
    /**
     * Construct with a given probability
     * 
     * @param probability - the probability to set
     */
    public ProbabilityEvent(final double probability) {
        this.probability = probability;
    }
   
    /**
     * Check the occurrence of an event, given the probability
     * we've set.
     * 
     * @return true if the event occurred; false otherwise
     */
    public boolean checkOccurrence() {
        double rand = ThreadLocalRandom.current().nextDouble(0, 1);
        if (rand <= probability) {
            return true;
        }
        
        return false;
    }
}
