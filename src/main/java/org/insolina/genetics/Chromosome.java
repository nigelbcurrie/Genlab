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
public interface Chromosome {  
    int cumulativeValue();   
    void setFitness(int fitness);
    int getFitness();
    void incrementSelectionCount();
    int getCrossoverLength();
    int getMutationLength();
    ChromosomePair crossover(Chromosome other, int crossoverPoint);
    void mutateAt(int mutationPosition);
    int intValue();
}
