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
 * @author Nigel Currie
 */
public class GenerationStatistics {
    private List<GenData> genDataList = new ArrayList<>();
    
    public void addGenData(final GenData genData) {
        genDataList.add(genData);
    }
    
    public GenData getGenData(final int index) {
        if (index >= 0 && index < genDataList.size()) {
            return genDataList.get(index);
        }
        
        return new GenData();
    }
    
    public int getGenDataCount() {
        return genDataList.size();
    }
    
    public static class GenData {
        public double bestFitness;
        public double avgFitness;
        public Chromosome bestChromosome;
        
        public GenData() {
            
        }
        
        public GenData(final double bestFitness, final double avgFitness, final Chromosome bestChromosome) {
            this.bestFitness = bestFitness;
            this.avgFitness = avgFitness;
            this.bestChromosome = bestChromosome;
        }
    }
    
    public PlotData<double[], double[]> getAvgFitnessData(final int ignoreFirstN) {
        int len = genDataList.size();
        
        double[] indices = new double[len];
        double[] fitness = new double[len];
        for (int i = ignoreFirstN; i < len; i++) {
            indices[i] = i + 1;
            fitness[i] = genDataList.get(i).avgFitness;
        }
        
        return new PlotData<>(indices, fitness);
    }
    
    public PlotData<double[], double[]> getBestFitnessData(final int ignoreFirstN) {
        int len = genDataList.size();
        
        double[] indices = new double[len];
        double[] fitness = new double[len];
        for (int i = ignoreFirstN; i < len; i++) {
            indices[i] = i + 1;
            fitness[i] = genDataList.get(i).bestFitness;
        }
        
        return new PlotData<>(indices, fitness);
    }
}
