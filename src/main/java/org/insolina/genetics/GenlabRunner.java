/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.genetics;

import java.util.function.Function;
import org.insolina.config.Config;
import org.insolina.util.Instantiator;

/**
 * A utility class to run a Genetic Algorithm based on config. 
 * 
 * @author Nigel Currie
 */
public class GenlabRunner {
    public static GeneticAlgorithm run() {
        GeneticAlgorithm alg = new GeneticAlgorithm();
        
        long population = Config.getLong("genlab.runner.alg.population", 0);
        if (population > 0) {
            alg = alg.withPopulation((int) population);
        }

        long runs = Config.getLong("genlab.runner.alg.runs", 0);
        if (runs > 0) {
            alg = alg.withRuns((int) runs);
        }
        
        double singlePointCrossover = Config.getDouble("genlab.runner.alg.singlePointCrossover", 0.0);
        if (singlePointCrossover > 0.0) {
            alg.withSinglePointCrossover(singlePointCrossover);
        }
        
        long generations = Config.getLong("genlab.runner.alg.generations", 0);
        boolean withStats = Config.getBool("genlab.runner.alg.stats", false);
        if (generations > 0) {
            alg = alg.withGenerations((int) generations, withStats);
        }
        
        Instantiator<Mutator> mutInst = new Instantiator(Mutator.class);
        Mutator mutator = mutInst.newObject(Config.getString("genlab.runner.alg.mutator"));
        if (mutator != null) {
            alg = alg.withMutator(mutator);
        }
        
        Instantiator<Function<Chromosome, Integer>> functInst = new Instantiator(Function.class);
        Function<Chromosome, Integer> funct = functInst.newObject(Config.getString("genlab.runner.alg.fitnessFunction"));
        if (funct != null) {
            alg = alg.withFitnessFunction(funct);
        }
        
        Instantiator<ChromosomeBuilder> chromInst = new Instantiator(ChromosomeBuilder.class);
        ChromosomeBuilder builder = chromInst.newObject(Config.getString("genlab.runner.alg.chromosomeBuilder"));
        if (builder != null) {
            alg = alg.withChromosomeBuilder(builder);
        }
                                                   
        alg.start();
        
        return alg;
    }

}
