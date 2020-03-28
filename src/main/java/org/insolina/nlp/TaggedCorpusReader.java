/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

import org.insolina.nlp.Sentence;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 *
 * @author Nigel Currie
 */
public class TaggedCorpusReader {
    private final String corpusLocation;
    Map<String, List<String>> categoryFileMap = new HashMap<>();

    TaggedCorpusReader(final String corpusLocation, final String catFileName) {
        this.corpusLocation = corpusLocation;
        try {
            List<String> lines = Files.readAllLines(Paths.get(corpusLocation, catFileName),
                    Charset.defaultCharset());
            
            for (String line : lines) {
                String[] cols = line.split(" ");
                if (cols.length >= 2) {
                    List<String> files = categoryFileMap.get(cols[1]);
                    if (files == null) {
                        files = new ArrayList<>();
                    }
                    
                    files.add(cols[0]);
                    categoryFileMap.put(cols[1], files);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> categories() {
        return categoryFileMap.keySet().stream().sorted().collect(Collectors.toList());
    }
    
    public List<String> fileids() {
        List<String> fileids = new ArrayList<>();
        for (List<String> files : categoryFileMap.values()) {
            fileids.addAll(files);
        }
        
        return fileids.stream().sorted().collect(Collectors.toList());
    }
    
    public List<String> fileids(final String category) {
        if (category == null) {
            return fileids();
        }
        
        List<String> fileids = categoryFileMap.get(category);
        if (fileids != null) {
            return fileids.stream().sorted().collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
    
    public List<Sentence> sample(final int sizeOfSample) {
        return sample(sizeOfSample, null);
    }
    
    public List<Sentence> sample(final int sizeOfSample, final String category) {
        List<Sentence> sentences = new ArrayList<>();
        
        List<String> filenames = fileids(category);
        int lenFileList = filenames.size();
        if (lenFileList == 0) {
            return sentences;
        }
        
        int i = 0;
        while (i < sizeOfSample) {
            int index = ThreadLocalRandom.current().nextInt(0, lenFileList);
            String filename = filenames.get(index);
            String randomSentence = getRandomSentenceFromFile(filename);
            if (randomSentence != null) {
                sentences.add(new Sentence(randomSentence));
                i++;
            }
        }
        
        return sentences;
    }

    private String getRandomSentenceFromFile(final String filename) {
        String sentence = null;
        
        try {
            Path path = Paths.get(corpusLocation, filename);
            //System.out.println("Reading from file: " + filename + "...");
            try (RandomAccessFile file = new RandomAccessFile(path.toString(), "r")) {
                long fileLength = file.length();
                long randomPosition = ThreadLocalRandom.current().nextLong(0, fileLength);
                
                file.seek(randomPosition);
                // Get to the end of the current line
                String line = file.readLine();
                while ((line = file.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0) {
                        sentence = line;
                        break;
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return sentence;
    }

}
