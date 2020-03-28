/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Nigel Currie
 * 
 * Add a method for returning all the Noun Phrases and Verb Phrases in the tree, as subtrees (as an 
 * array of SyntaxNode). We can print these out to see them, and maybe also index into the array to 
 * do the crossover.
 * 
 * Nodes in the tree are addressed using an array of integers. The position in the array indicates the
 * level in the tree; the integer value indicates the position in the child list.
 */
public class SyntaxNode {
    public String tag;
    public String text;
    public List<SyntaxNode> children = new ArrayList<>();
    public List<Integer> treePath;
    
    private static final Set<String> PUNCTUATION_CHARS = Stream.of(".", "?", "!", "-", ",", ";", ":")
         .collect(Collectors.toCollection(HashSet::new));

    /** 
     * Constructor. Text will be either just a syntax tag, like NP or VP, or a 
     * tag plus a word or punctuation mark separated by a space.
     * 
     * @param text the input text
     */
    public SyntaxNode(final String text) {
        if (text != null) {
            String[] fields = text.split(" ");
            this.tag = fields[0];
            if (fields.length > 1) {
                this.text = fields[1];
            }
        }
    }

    /**
     * Print out the syntax tree, displaying the tree using an indent of some kind
     * 
     * @param prefix the prefix to use for the indent
     */
    public void print(final String prefix, final int level, final boolean printTreePath) {
        String thisLevelPrefix = repeat(prefix, level);
        
        StringBuilder buf = new StringBuilder();
        buf.append(thisLevelPrefix).append("[").append(tag).append("]").append((text == null) ? "" : " " + text);
        if (printTreePath) {
            buf.append(" ").append(getTreePathString());
        }
        System.out.println(buf.toString());
        printChildren(prefix, level + 1, printTreePath);
    }
    
    public String repeat(final String s, final int count) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < count; i++) {
            buf.append(s);
        }
        return buf.toString();
    }

    /**
     * Generate the original text of the sentence
     * 
     * @param prefix the prefix
     * @return the sentence
     */
    public String toSentence(final String prefix) {
        StringBuilder buf = new StringBuilder();
        if (text != null) {
            if (prefix != null) {
                buf.append(prefix);
            }
            buf.append(text);
        }

        children.stream().forEach((node) -> {
            buf.append(node.toSentence(" "));
        });
        
        return buf.toString();
    }

    /**
     * Print all the children of this node
     * 
     * @param prefix the prefix to use for indentation
     */
    public void printChildren(final String prefix, final int level, final boolean printTreePath) {
        children.stream().forEach((node) -> {
            node.print(prefix, level, printTreePath);
        });
    }
    
    /**
     * Count the number of words in the tree
     * 
     * @return the number of words
     */
    public int countWords() {
        int sum = 0;
        
        if (text != null && (text.length() > 1 || !PUNCTUATION_CHARS.contains(text))) {
            sum++;
        }
        
        for (SyntaxNode node : children) {
            sum += node.countWords();
        }
        
        return sum;
    }
    
    public List<SyntaxNode> getPhrases() {
        List<SyntaxNode> phrases = new ArrayList<>();
        
        if (tag.equals("NP") || tag.equals("VP")) {
            phrases.add(this);
        }
        
        children.stream().forEach((node) -> {
            phrases.addAll(node.getPhrases());
        });
        
        return phrases;
    }
    
    public List<SyntaxNode> getNounPhrases() {
        List<SyntaxNode> phrases = new ArrayList<>();
        
        if (tag.equals("NP")) {
            phrases.add(this);
        }
        
        children.stream().forEach((node) -> {
            phrases.addAll(node.getNounPhrases());
        });
        
        return phrases;
    }
    
    public void setTreePath() {
        setTreePath(new ArrayList<>(), 1);
    }
    
    public void setTreePath(final List<Integer> parentPath, final int currentPos) {
        treePath = new ArrayList<>(parentPath);
        treePath.add(currentPos);
        
        for (int i = 0; i < children.size(); i++) {
            SyntaxNode child = children.get(i);
            child.setTreePath(treePath, i + 1);
        }
    }

    public String getTreePathString() {
        if (treePath == null) {
            return "{}";
        }
        
        StringBuilder buf = new StringBuilder();
        buf.append("{");
        Iterator<Integer> iter = treePath.iterator();
        while (iter.hasNext()) {
            buf.append(iter.next());
            if (iter.hasNext()) {
                buf.append(".");
            }
        }
        buf.append("}");
        
        return buf.toString();
    }
    
    public SyntaxNode findParent(final SyntaxNode root) {
        if (treePath == null) {
            return null;
        }
        
        List<Integer> parentTreePath = treePath.subList(0, treePath.size() - 1);
        return findNode(root, parentTreePath);
    }
    
    public SyntaxNode findNode(final SyntaxNode root, final List<Integer> path) {
        // Iterate or recurse down the path until you find the node
        if (path == null || path.size() <= 1) {
            return root;
        }
        
        int pathlen = path.size();
        SyntaxNode currentNode = root;
        for (int i = 1; i < pathlen; i++) {
            currentNode = currentNode.children.get(path.get(i) - 1);
        }
        
        return currentNode;
    }
    
    public int getSiblingPosition() {
        if (treePath == null) {
            return 0;
        }
        
        return treePath.get(treePath.size() - 1);
    }

    public List<SyntaxNode> getCandidateNounPhrases(final int maxCrossoverLen) {
        List<SyntaxNode> phrases = new ArrayList<>();
        
        if (tag.equals("NP") && countWords() <= maxCrossoverLen) {
            phrases.add(this);
        }
        
        children.stream().forEach((node) -> {
            phrases.addAll(node.getCandidateNounPhrases(maxCrossoverLen));
        });
        
        return phrases;
    }

    public List<String> getWords() {
        List<String> words = new ArrayList<>();
        
        if (text != null && !PUNCTUATION_CHARS.contains(text)) {
            words.add(text);
        }
        
        children.stream().forEach((node) -> {
            words.addAll(node.getWords());
        });
        
        return words;
    }
}
