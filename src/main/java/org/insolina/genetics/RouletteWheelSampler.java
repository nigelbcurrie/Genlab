/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author nigel_2
 * 
 * Note: another option would be Tournament Selection
 */
public class RouletteWheelSampler implements Sampler {
    private List<? extends Chromosome> population;
    private double[] slots;

    @Override
    public void setSampleData(List<? extends Chromosome> data, final boolean highFitness) {
        if (data == null) {
            return;
        }
        
        // My first implementation created as many slots for each chromosome as the fitness value.
        // It worked fine, but had the potential for creating very large arrays. I found this 
        // better way of doing it in the Watchmaker GA library.
        
        // This algorithm uses cumulative fitnesses. You create an array of length populationSize, 
        // but make it an array of sorted cumulative fitnesses. So say we have A with fitness 13,
        // B, fitness 8, and C, fitness 10, we would end up with an array [13, 21, 31]. Then to do 
        // the selection, multiply a random double between 0 and 1 with the final element in the 
        // array of cumulative fitnesses, and find where that fitness would be inserted by doing 
        // Arrays.binarySearch. This is much better than the simple solution because you have a 
        // fixed array size.
        
        population = data;
        int populationSize = population.size();
        slots = new double[populationSize];
        
        slots[0] = normalizeFitness(population.get(0).getFitness(), highFitness);       
        for (int i = 1; i < populationSize; i++) {
            slots[i] = slots[i - 1] + normalizeFitness(population.get(i).getFitness(), highFitness);
        }
    }
    
    private double normalizeFitness(final double fitness, final boolean highFitness) {
        if (highFitness) {
            return fitness;
        }
        
        return (fitness == 0) ? Double.POSITIVE_INFINITY : 1 / fitness;
    }

    @Override
    public ChromosomePair selectParents() {
        List<Chromosome> selections = select(2);
        
        return new ChromosomePair(selections.get(0), selections.get(1));
    }
    
    public List<Chromosome> select(final int howMany) {
        List<Chromosome> selections = new ArrayList<>();
        
        for (int i = 0; i < howMany; i++) {
            double fitness = ThreadLocalRandom.current().nextDouble(0, 1) * slots[slots.length - 1];
            int index = Arrays.binarySearch(slots, fitness);
            index = (index < 0) ? Math.abs(index + 1) : index;
            Chromosome parent = population.get(index);
            parent.incrementSelectionCount();
            
            selections.add(parent);
        }

        return selections;
    }
}
