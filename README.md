# GenlabJ
A Java framework for genetic algorithms

This initial version of GenlabJ was written to complete programming exercise 1 in Melanie Mitchell's _An Introduction to Genetic Algorithms_. But by the time I'd finished I realised I'd written a general framework for solving any number of exercises, so I thought I'd publish it here.

My eventual plan is to develop this framework so that I can use it to generate human-quality haiku :-). In the meantime instructions on how to use the current framework are provided below.

_A flock of seagulls \
On a ploughed field; white petals \
Ruffled by the wind._

## Running the algorithm

```java
GeneticAlgorithm alg = new GeneticAlgorithm();
alg.start();
```

By default the algorithm will perform the task described here:

>Implement a simple GA with fitness-proportionate selection, roulette-wheel sampling, population size 100, single-point crossover >rate pc = 0.7, and bitwise mutation rate pm = 0.001. Try it on the following fitness function: f(x) = number of ones in x, where x >is a chromosome of length 20.
>        
>Perform 20 runs, and measure the average generation at which the string of all ones is discovered.

To print out the results, and to plot the gathered data (using the Plotter utility from my MatplotJ library):

```java
RunStatistics runStats = alg.getRunStatistics();
int count = runStats.getRunDataCount();
for (int i = 0; i < count; i++) {
    RunData runData = runStats.getRunData(i);
    System.out.println("Best chromosome (Run " + (i + 1) + "): " + runData.bestChromosome);
}

PlotData<double[], double[]> plotData1 = alg.getRunStatistics().getAvgGenAtBestFitnessData();
Plotter plt1 = new Plotter("Generation at which best fitness was found", "Run", "Generation");
plt1.plot(plotData1.xData, plotData1.yData, "Best fitness", "bo-");
plt1.show();
```
## Changing the parameters of the algorithm

There are builder methods to set the algorithm parameters explicitly. The following code runs the same algorithm as above, but with the parameters set explicitly:

```java
GeneticAlgorithm alg = new GeneticAlgorithm()
   .withPopulation(100)
   .withBitStringChromosome(20)
   .withSinglePointCrossover(0.7)
   .withBitwiseMutation(0.001)
   .withFitnessFunction(chromo -> 20 - chromo.cumulativeValue())
   .withRuns(20);
alg.start();
```
