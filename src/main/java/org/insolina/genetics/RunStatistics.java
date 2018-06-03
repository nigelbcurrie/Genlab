/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.genetics;

import org.insolina.genetics.PlotData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nigel Currie
 */
public class RunStatistics {
    private List<RunData> runDataList = new ArrayList<>();
    
    public void addRunData(final RunData runData) {
        runDataList.add(runData);
    }
    
    public RunData getRunData(final int index) {
        if (index >= 0 && index < runDataList.size()) {
            return runDataList.get(index);
        }
        
        return new RunData();
    }
    
    public int getRunDateCount() {
        return runDataList.size();
    }

    public static class RunData {
        public int avgGenAtBestFitness;
        public double bestFitness;
        public Chromosome bestChromosome;
        public GenerationStatistics generationStats;
        
        public RunData() {
            
        }
        
        public RunData(final int avgGenAtBestFitness, final double bestFitness, 
                final Chromosome bestChromosome, final GenerationStatistics genStats) {
            this.avgGenAtBestFitness = avgGenAtBestFitness;
            this.bestFitness = bestFitness;
            this.bestChromosome = bestChromosome;
            this.generationStats = genStats;
        }
        
        public GenerationStatistics getGenerationStats() {
            return generationStats;
        }
    }
    
    public PlotData<double[], double[]> getAvgGenAtBestFitnessData() {
        int len = runDataList.size();
        
        double[] indices = new double[len];
        double[] avgs = new double[len];
        for (int i = 0; i < len; i++) {
            indices[i] = i + 1;
            avgs[i] = runDataList.get(i).avgGenAtBestFitness;
        }
        
        return new PlotData<>(indices, avgs);
    }
    
    public PlotData<double[], double[]> getBestFitnessData() {
        int len = runDataList.size();
        
        double[] indices = new double[len];
        double[] fitness = new double[len];
        for (int i = 0; i < len; i++) {
            indices[i] = i + 1;
            fitness[i] = runDataList.get(i).bestFitness;
        }
        
        return new PlotData<>(indices, fitness);
    }
}
