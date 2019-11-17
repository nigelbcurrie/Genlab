/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author nigel_2
 */
public class BitStringChromosome implements Chromosome {
    private BitString bitString;
    private int fitness;
    private int selectionCount;
    private int length;
    private boolean crossover;
    private boolean mutated;

    static BitStringChromosome random(final int length) {
        BitString bitString = new BitString(length);
        for (int i = 0; i < length; i++) {
            bitString.set(i, ThreadLocalRandom.current().nextBoolean());
        }
        
        return new BitStringChromosome(bitString, length, false);
    }

    public BitStringChromosome(final BitString bitString, final int length, final boolean crossover) {
        this.bitString = bitString;
        this.length = length;
        this.crossover = crossover;
    }

    @Override
    public String toString() {
        if (bitString != null) {
            return "value: " + bitString.toString() + "; fitness: " + fitness + "; selectionCount: " + selectionCount 
                    + "; crossover: " + crossover + "; mutated: " + mutated;
        }
        
        return null;
    }

    @Override
    public void setFitness(final int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int cumulativeValue() {
        return bitString.countOnes();
    }

    @Override
    public int getFitness() {
        return fitness;
    }
    @Override 
    public void incrementSelectionCount() {
        selectionCount++;
    }

    private int getLength() {
        return length;
    }

    @Override
    public ChromosomePair crossover(final Chromosome other, final int crossoverPoint) {
        if (!(other instanceof BitStringChromosome)) {
            return new ChromosomePair(this, other);
        }
              
        byte[] thisNewData = new byte[length];
        byte[] otherNewData = new byte[length];
        
        byte[] thisData = bitString.getData();
        byte[] otherData = bitString.getData();
        
        int i = 0;
        for (; i <= crossoverPoint; i++) {
            thisNewData[i] = thisData[i];
            otherNewData[i] = otherData[i];
        }
        
        for (; i < length; i++) {
            thisNewData[i] = otherData[i];
            otherNewData[i] = thisData[i];
        }
        
        return new ChromosomePair(new BitStringChromosome(new BitString(thisNewData), length, true),
            new BitStringChromosome(new BitString(otherNewData), length, true));
    }

    @Override
    public void mutateAt(final int mutationPosition) {
        bitString.flipBit(mutationPosition);
        mutated = true;
    }

    @Override
    public int intValue() {
        return bitString.intValue();
    }

    @Override
    public int getCrossoverLength() {
        return getLength();
    }

    @Override
    public int getMutationLength() {
        return getLength();
    }
}
