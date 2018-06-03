/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

import org.insolina.probability.ProbabilityEvent;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author nigel_2
 */
public class SinglePointCrossover implements Crossover {
    ProbabilityEvent pEvent;
    
    public SinglePointCrossover(final double crossoverRate) {
        pEvent = new ProbabilityEvent(crossoverRate);
    }

    @Override
    public ChromosomePair generateOffspring(ChromosomePair parents) {
        if (!pEvent.checkOccurrence()) {
            // No crossover. Offspring are a copy of the parents.
            return new ChromosomePair(parents.c1, parents.c2);
        }
        
        int maxIndex = parents.length - 1;
        int crossoverPoint = ThreadLocalRandom.current().nextInt(0, maxIndex);
        
        ChromosomePair offspring = parents.crossover(crossoverPoint);
        return offspring;
    }
}
