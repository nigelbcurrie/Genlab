/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nigel_2
 */
public class BitStringChromosomeBuilder implements ChromosomeBuilder {
    private int chromosomeLength;

    BitStringChromosomeBuilder(final int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
    }

    @Override
    public List<? extends Chromosome> createInitialPopulation(final int size) {
        List<BitStringChromosome> initialPopulation = new ArrayList<>();
        
        for (int i = 0; i < size; i++) {
            initialPopulation.add(BitStringChromosome.random(chromosomeLength));
        }
        
        return initialPopulation;
    }
    
}
