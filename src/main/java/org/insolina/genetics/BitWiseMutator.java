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
 * @author Nigel Currie
 */
class BitWiseMutator implements Mutator {
    ProbabilityEvent pEvent;
    
    public BitWiseMutator(final double mutationRate) {
        pEvent = new ProbabilityEvent(mutationRate);
    }

    @Override
    public void mutate(final Chromosome child) {
        if (!pEvent.checkOccurrence()) {
            return;
        }
        
        int mutationPosition = ThreadLocalRandom.current().nextInt(0, child.getMutationLength());
        child.mutateAt(mutationPosition);
    }

}
