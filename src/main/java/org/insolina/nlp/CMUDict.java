/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

import org.insolina.config.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.sqlite.SQLiteDataSource;

/**
 * A class for accessing the Carnegie Mellon University pronunciation dictionary (cmudict). I've loaded 
 * the normal text file into a sqlite database to make it faster to access.
 * 
 * For more information about cmudict, see the home page here:
 * 
 * <a>http://www.speech.cs.cmu.edu/cgi-bin/cmudict</a>
 * 
 * @author Nigel Currie
 */
public class CMUDict {
    private final static CMUDict SINGLETON = new CMUDict();
    private final static String SQL_PRO = "select pronunciation from cmudict where word = ? order by pronunciation_index;";
    private final static String SQL_SYL = "select syllable_count from cmudict where word = ? and pronunciation_index = 0;";
    
    private SQLiteDataSource ds;
    
    /**
     * Private constructor because we don't want anyone else to create instances
     */
    private CMUDict() {
        ds = new SQLiteDataSource();
        ds.setUrl(Config.getString("cmudict.jdbc.url"));
    }
    
    /**
     * Static method to get the single instance.
     * 
     * @return the single instance of the CMUDict class
     */
    public static CMUDict get() {
        return SINGLETON;
    }
    
    /**
     * Return all the pronunciations of a word. A pronunciation is a space-separated list of  
     * phonemes.
     * 
     * @param word the word
     * @return all the pronunciations
     */
    public List<String> getPronunciations(final String word) {
        List<String> pros = new ArrayList<>();
        if (word == null) {
            return pros;
        }
        
        try (Connection conn = ds.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(SQL_PRO);
            pstmt.setString(1, word.toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pros.add(rs.getString("pronunciation"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CMUDict.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pros;
    }
    
    /**
     * Get all the phoneme lists for all word, i.e. get all the pronunciations, and
     * break those down into lists of phonemes.
     * 
     * @param word the word
     * @return all the phoneme lists
     */
    public List<String[]> getPhonemesLists(final String word) {
        List<String[]> phonemeList = new ArrayList<>();
        
        List<String> pronunciations = getPronunciations(word);
        for (String pronunciation : pronunciations) {
            phonemeList.add(pronunciation.split(" "));
        }
        
        return phonemeList;
    }
    
    /**
     * Get the first pronunciation for the word, i.e. the pronunciation with pronunciation_index = 0.
     * 
     * @param word the word
     * @return the first pronunciation
     */
    public String getPronunciation(final String word) {
        List<String> pronunciations = getPronunciations(word);
        if (pronunciations.size() > 0) {
            return pronunciations.get(0);
        }
        
        return null;
    }
    
    /**
     * Get the phoneme list for the first pronunciation, i.e. the pronunciation with pronunciation_index = 0.
     * 
     * @param word the word
     * @return the first phoneme list
     */
    public String[] getPhonemes(final String word) {
        List<String[]> phonemeLists = getPhonemesLists(word);
        if (phonemeLists.size() > 0) {
            return phonemeLists.get(0);
        }
        
        return null;
    }
    
    /**
     * Return the syllable count for the word.
     * 
     * @param word the word
     * @return the syllable count
     */
    public int countSyllables(final String word) {
        if (word == null) {
            return 0;
        }
        
        int count = 0;
        try (Connection conn = ds.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(SQL_SYL);
            pstmt.setString(1, word.toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("syllable_count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CMUDict.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Return the number of syllables in a list of words. If there are duplicate words in the list, 
     * this method will only count the syllables for one instance of the word, e.g. given the sentence,
     * 'That was the week, that was.', this method will return a value of 4, rather than 6. So if you need to 
     * treat each duplicate as a separate word, then use the overridden method below. If you're sure 
     * your list doesn't contain duplicates, or you don't care, use this method.
     * 
     * @param words
     * @return the number of syllables in all the words
     */
    public int countSyllables(final List<String> words) {
        if (words == null || words.isEmpty()) {
            return 0;
        }
        
        String inList = words.stream()
                .map(w -> w.toUpperCase().replaceAll("'", "''"))
                .collect(Collectors.joining("','", "'", "'"));
        StringBuilder buf = new StringBuilder();
        buf.append("select sum(syllable_count) as sum_syllables from cmudict where pronunciation_index = 0 and word in (");
        buf.append(inList).append(")");
        
        int count = 0;
        try (Connection conn = ds.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(buf.toString());
            if (rs.next()) {
                count = rs.getInt("sum_syllables");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CMUDict.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Return the number of syllables in a list of words. 
     * 
     * @param words the list of words
     * @param countDuplicates true if you want to count all occurrences of identical words
     * @return the number of syllables in all the words
     */
    public int countSyllables(final List<String> words, final boolean countDuplicates) {
        if (words == null || words.isEmpty()) {
            return 0;
        }
        
        if (!countDuplicates) {
            return countSyllables(words);
        }
        
        // Count how many occurrences of each word there are
        Map<String, Integer> occurrences = new HashMap<>();
        words.stream().map(w -> w.toUpperCase()).forEach((word) -> {
            if (!occurrences.containsKey(word)) {
                occurrences.put(word, 1); 
            } else {
                occurrences.put(word, occurrences.get(word) + 1);
            }
        });
        
        String inList = occurrences.keySet().stream()
                .map(w -> w.replaceAll("'", "''"))
                .collect(Collectors.joining("','", "'", "'"));
        StringBuilder buf = new StringBuilder();
        buf.append("select word, syllable_count from cmudict where pronunciation_index = 0 and word in (");
        buf.append(inList).append(")");
        
        int count = 0;
        try (Connection conn = ds.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(buf.toString());
            while (rs.next()) {
                String word = rs.getString("word");
                int syllableCount = rs.getInt("syllable_count");
                
                int multiplier = (occurrences.containsKey(word)) ? occurrences.get(word) : 1;
                count += (syllableCount * multiplier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CMUDict.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
}
