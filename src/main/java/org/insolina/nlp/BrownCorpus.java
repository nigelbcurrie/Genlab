/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

import org.insolina.config.Config;
import java.util.List;

/**
 *
 * In the Brown Corpus there is a file called cats.txt that lists the files by category. To sample
 * the sentences, find a file name in cats.txt, then sample that file.
 * 
 * In NLTK the brown corpus is something you import, then you have methods like categories(), words(),
 * sents(), paras(), tagged_words(), tagged_sents(), tagged_paras() and raw(). Behind the scenes, NLTK 
 * uses different readers for different types of corpora: TaggedCorpusReader for the Brown Corpus,
 * PlaintextCorpusReader for state_union and other plain text corpora. Method fileids() gives a list of 
 * the files in the corpus. In the BrownCorpus, fileids("<category>") gives the files in a particular category.
 * 
 * TODO: Implement other NLTK methods?
 * 
 * @author Nigel Currie
 */
public class BrownCorpus {
    private final String corpusLocation = Config.getString("nlp.corpora.locations.brown");
    private final TaggedCorpusReader reader;
    
    /**
     * Construct the BrownCorpus instance
     */
    public BrownCorpus() {
        reader = new TaggedCorpusReader(corpusLocation, "cats.txt");
    }
    
    /**
     * Get the list of categories
     * 
     * @return the list of categories
     */
    public List<String> categories() {
        return reader.categories();
    }
    
    /**
     * Get the list of file names. The method name is taken from the 
     * Python nltk module.
     * 
     * @return 
     */
    public List<String> fileids() {
        return reader.fileids();
    }
    
    /**
     * Get the list of file names for a category.
     * 
     * @param category the category name
     * @return the list of file names
     */
    public List<String> fileids(final String category) {
        return reader.fileids(category);
    }
    
    /**
     * Return a random sample of sentences from the corpus.
     * 
     * @param sizeOfSample the size of sample we want
     * @return a random list of sentences
     */
    public List<Sentence> sample(final int sizeOfSample) {
        return reader.sample(sizeOfSample);
    }
    
    /**
     * Return a random sample of sentences from a particular category of the corpus.
     * 
     * @param sizeOfSample the size of sample we want
     * @param category the category
     * @return a random list of sentences
     */
    public List<Sentence> sample(final int sizeOfSample, final String category) {
        return reader.sample(sizeOfSample);
    }
}
