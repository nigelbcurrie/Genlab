/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.genetics;

import org.insolina.genetics.Chromosome;
import org.insolina.genetics.Mutator;

/**
 *
 * @author Nigel Currie
 */
public class NullMutator implements Mutator {

    @Override
    public void mutate(Chromosome chrmsm) {
        // Do nothing
    }

}
