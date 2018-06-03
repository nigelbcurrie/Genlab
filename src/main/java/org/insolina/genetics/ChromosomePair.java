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
public class ChromosomePair {
    public Chromosome c1;
    public Chromosome c2;
    public int length;

    ChromosomePair(final Chromosome parentOne, final Chromosome parentTwo) {
        this.c1 = parentOne;
        this.c2 = parentTwo;
        
        this.length = Math.min(parentOne.getLength(), parentTwo.getLength());
    }

    ChromosomePair crossover(final int crossoverPoint) {
        return c1.crossover(c2, crossoverPoint);
    }
    
    @Override
    public String toString() {
        return "C1: " + c1.toString() + System.lineSeparator()
                + "C2: " + c2.toString();
    }
}
