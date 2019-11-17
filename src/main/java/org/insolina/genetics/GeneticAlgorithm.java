/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Nigel Currie
 * 
 * 
 */
public class GeneticAlgorithm {
    private final static int DEFAULT_POPULATION_SIZE = 100;
    private final static int DEFAULT_CHROMOSOME_LENGTH = 20;
    private final static double DEFAULT_CROSSOVER_RATE = 0.7;
    private final static double DEFAULT_MUTATION_RATE = 0.001;
    private final static int DEFAULT_NUMBER_OF_RUNS = 20;
    private final static int MAX_GENERATIONS_WITH_NO_CHANGE = 10000;
    
    private int populationSize;
    private int chromosomeLength;
    private double crossoverRate;
    private double mutationRate;
    private int numberOfRuns;
    
    private ChromosomeBuilder chromosomeBuilder;
    private Sampler sampler;
    private Crossover crossover;
    private Mutator mutator;
    private Function<Chromosome, Integer> fitnessFunc = GeneticAlgorithm::calculateFitness;
        
    private List<? extends Chromosome> currentPopulation;
    private int generationCount;
    private int generationWhereBestFitnessLastChanged;
    private int currentBestFitness;
    private int maxGenerations;
    private boolean gatherGenStats;
    private boolean highFitness;
    
    private RunStatistics runStats = new RunStatistics();
    
    /**
     * Default constructor
     */
    public GeneticAlgorithm() {
        populationSize = DEFAULT_POPULATION_SIZE;
        chromosomeLength = DEFAULT_CHROMOSOME_LENGTH;
        crossoverRate = DEFAULT_CROSSOVER_RATE;
        mutationRate = DEFAULT_MUTATION_RATE;
        numberOfRuns = DEFAULT_NUMBER_OF_RUNS;
                
        sampler = new RouletteWheelSampler();
        chromosomeBuilder = new BitStringChromosomeBuilder(DEFAULT_CHROMOSOME_LENGTH);
        crossover = new SinglePointCrossover(DEFAULT_CROSSOVER_RATE);
        mutator = new BitWiseMutator(DEFAULT_MUTATION_RATE); 
        maxGenerations = Integer.MAX_VALUE;
    }

    /**
     * Set the initial size of the population
     * 
     * @param population the population size
     * @return this object
     */
    public GeneticAlgorithm withPopulation(final int population) {
        this.populationSize = population;
        return this;
    }

    /**
     * Set the chromosome type to be a bit string
     * 
     * @param chromosomeLength the length of the chromosome
     * @return this object
     */
    public GeneticAlgorithm withBitStringChromosome(final int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
        chromosomeBuilder = new BitStringChromosomeBuilder(chromosomeLength);
        return this;
    }

    /**
     * Set the crossover type to be single-point crossover
     * 
     * @param crossoverRate the crossover rate
     * @return this object
     */
    public GeneticAlgorithm withSinglePointCrossover(final double crossoverRate) {
        this.crossoverRate = crossoverRate;
        this.crossover = new SinglePointCrossover(crossoverRate);
        return this;
    }

    /**
     * Set the mutation type to be bitwise mutation
     * 
     * @param mutationRate the mutation rate
     * @return this object
     */
    public GeneticAlgorithm withBitwiseMutation(final double mutationRate) {
        this.mutationRate = mutationRate;
        this.mutator = new BitWiseMutator(mutationRate);
        return this;
    }

    /**
     * Set the fitness function to use
     * 
     * @param fitnessFunc the fitness function
     * @return this object
     */
    public GeneticAlgorithm withFitnessFunction(final Function<Chromosome, Integer> fitnessFunc) {
        this.fitnessFunc = fitnessFunc;
        return this;
    }

    /**
     * Set the number of runs, i.e. how many times to repeat the experiment
     * 
     * @param numberOfRuns the number of runs
     * @return this object
     */
    public GeneticAlgorithm withRuns(final int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
        return this;
    }
    
    /**
     * Set how many generations the algorithm will evolve for. If you don't limit the 
     * number of generations, the algorithm will run until there's been no change in best fitness 
     * for 10,000 generations.
     * 
     * @param maxGenerations the number of generations
     * @param gatherStats whether the gather statistics for each generation
     * @return this object
     */
    public GeneticAlgorithm withGenerations(final int maxGenerations, final boolean gatherStats) {
        this.maxGenerations = maxGenerations;
        this.gatherGenStats = gatherStats;
        return this;
    }
    
    /**
     * By default the algorithm assumes that best fitness tends to zero. This will switch that 
     * logic so that higher fitness values are better.
     * 
     * @return this object
     */
    public GeneticAlgorithm withHighFitness() {
        highFitness = true;
        return this;
    }
    
    /**
     * Set the chromosome builder for this algorithm
     * 
     * @param chromosomeBuilder the chromosome builder
     * @return this object
     */
    public GeneticAlgorithm withChromosomeBuilder(final ChromosomeBuilder chromosomeBuilder) {
        this.chromosomeBuilder = chromosomeBuilder;
        return this;
    }
    
    /**
     * Set the crossover for this algorithm
     * 
     * @param crossover the crossover
     * @return this object
     */
    public GeneticAlgorithm withCrossover(final Crossover crossover) {
        this.crossover = crossover;
        return this;
    }
    
    /**
     * Set the mutator for this algorithm 
     * 
     * @param mutator the mutator
     * @return this object
     */
    public GeneticAlgorithm withMutator(final Mutator mutator) {
        this.mutator = mutator;
        return this;
    }
    
    /**
     * Start the algorithm
     */
    public void start() { 
        System.out.println("Starting genetic algorithm...");     
        for (int i = 0; i < numberOfRuns; i++) {
            System.out.println("Run " + (i + 1) + "...");
            // 1. Generate the initial population of chromosomes
            GenerationStatistics genStats = new GenerationStatistics();
            currentPopulation = chromosomeBuilder.createInitialPopulation(populationSize);
            generationCount = 0;
            generationWhereBestFitnessLastChanged = 0;
            currentBestFitness = 999;
            
            while (true) { 
                generationCount++;
            
                // 2. Calculate the fitness of each chromosome
                currentPopulation.stream().forEach((chromosome) -> {
                    chromosome.setFitness(fitnessFunc.apply(chromosome));
                }); 

                int bestFitness = (highFitness) 
                        ? currentPopulation.stream().mapToInt(Chromosome::getFitness).max().getAsInt() 
                        : currentPopulation.stream().mapToInt(Chromosome::getFitness).min().getAsInt();
                
                boolean fitnessHasChanged = (highFitness) ? bestFitness > currentBestFitness : bestFitness < currentBestFitness;
                if (fitnessHasChanged) {
                    generationWhereBestFitnessLastChanged = generationCount;
                    currentBestFitness = bestFitness;
                }
                
                if (gatherGenStats) {
                    double avgFitness = currentPopulation.stream().mapToInt(Chromosome::getFitness).average().getAsDouble();
                    genStats.addGenData(new GenerationStatistics.GenData((double) bestFitness, avgFitness, 
                            getBestChromosome(bestFitness)));
                }

                if (generationCount > maxGenerations 
                        || (!highFitness && bestFitness == 0) 
                        || (generationCount - generationWhereBestFitnessLastChanged) >= MAX_GENERATIONS_WITH_NO_CHANGE) {
                    //printCurrentPopulation();
                    runStats.addRunData(new RunStatistics.RunData(generationWhereBestFitnessLastChanged, currentBestFitness, 
                            getBestChromosome(bestFitness), genStats));
                    break;
                }

                // 3. Generate the next generation
                //    (a) select parents
                sampler.setSampleData(currentPopulation, highFitness);
                List<ChromosomePair> selectionPairs = new ArrayList<>();
                for (int k = 0; k < populationSize; k += 2) {
                    selectionPairs.add(sampler.selectParents());
                }

                //    (b) generate offspring (with crossover)
                List<Chromosome> offspring = new ArrayList<>();
                selectionPairs.stream().forEach((selectionPair) -> {
                    ChromosomePair pair = crossover.generateOffspring(selectionPair);
                    offspring.add(pair.c1);
                    offspring.add(pair.c2);
                });
                //print("** Parents **", selectionPairs);
                //print(offspring, "** Offspring before mutation **");

                //    (c) mutate offspring
                offspring.stream().forEach((child) -> {
                    mutator.mutate(child);
                });
                //print(offspring, "** Offspring after mutation **");

                //    (d) replace old population with offspring
                currentPopulation = offspring;

                // 4. Repeat 2-3 until the fitness function is maximized
            }
            
            // 5. For this run store (a) what generation the fitness function was first maximized;
            //    (b) the time taken to get to that point
            // 6. Repeat 1-5 until we've got to the end of our runs
        }
        
        /*System.out.println("** Results **");
        results.stream().forEach(result -> {
            System.out.println("Result: " + result.generation + "; " + result.maxFitness);
        });*/
    }
    
    /**
     * Get the statistics for each run
     * 
     * @return the run statistics
     */
    public RunStatistics getRunStatistics() {
        return runStats;
    }
    
    private void printCurrentPopulation() {
        System.out.println("Current Population");
        if (currentPopulation != null) {
            currentPopulation.stream().forEach((chromosome) -> {
                System.out.println(chromosome.toString());
            });
        }
        System.out.println();
    }
    
    private void print(final String label, final List<ChromosomePair> pairs) {
        System.out.println(label);
        pairs.stream().forEach((pair) -> {
            System.out.println(pair.toString());
        });
        System.out.println();
    }
    
    private void print(final List<Chromosome> chromosomes, final String label) {
        System.out.println(label);
        chromosomes.stream().forEach((c) -> {
            System.out.println(c.toString());
        });
        System.out.println();
    }
    
    private static Integer calculateFitness(final Chromosome chromosome) {
        return 20 - chromosome.cumulativeValue();
    }

    private Chromosome getBestChromosome(final int bestFitness) {
        Chromosome best = null;
        int currentMinDifference = Integer.MAX_VALUE;
        for (Chromosome c : currentPopulation) {
            int diff = Math.abs(c.getFitness() - bestFitness);
            if (diff == 0) {
               best = c;
               break;
            } else if (diff < currentMinDifference) {
                currentMinDifference = diff;
                best = c;
            }
        }
        
        return best;
    }
}

