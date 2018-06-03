/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insolina.genetics;

import java.util.List;

/**
 *
 * @author nigel_2
 */
public interface ChromosomeBuilder {
    List<? extends Chromosome> createInitialPopulation(final int size);   
}
