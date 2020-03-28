/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

import java.util.Stack;

/**
*
* @author Nigel Currie
* Parsing the syntax tree:
* 
* 1. Every time you come across an open parenthesis, put the following characters (up to the next OPENPAREN, 
*    CLOSEPAREN, or End of Input ) on to the stack;
* 2. When you come across a CLOSEPAREN ;
*    (a) Pop the top item off the stack - this is the child;
*    (b) Pop the next item off the stack - this is the parent;
*    (c) Make child a child of parent;
*    (d) Put parent back on the stack
* 3. You should end up with one item on the stack. That is the root node of the tree.
* 
* Use a recursive descent parser
* 
* S -> '(' or ')' or TEXT
* TEXT -> anything
*/  
public class PennTreeParser {
    private String sentence;
    private int currentPos;
    final private Stack<SyntaxNode> stack = new Stack<>();
    final private char OPENPAREN = '(';
    final private char CLOSEPAREN = ')';
    final private char EOI = '\u0000';
    
    public PennTreeParser() {
        
    }

    public PennTreeParser(final String sentence) {
        this.sentence = sentence;
    }

    public SyntaxNode parse() {
        SyntaxNode root = sentence();
        root.setTreePath();
        return root;
    }
    
    public SyntaxNode parse(final String text) {
        this.sentence = text;
        this.currentPos = 0;
        stack.clear();
        SyntaxNode root = sentence();
        root.setTreePath();
        return root;
    }

    public SyntaxNode sentence() {
        char ch = peekToken();
        while (ch != EOI) {
            if (ch == OPENPAREN) {
                openParen();
            } else if (ch == CLOSEPAREN) {
                closeParen();
            } else if (ch == ' ') {
                consume();
            } else {
                SyntaxNode node = text();
                if (node != null) {
                    stack.push(node);
                }
            }

            ch = peekToken();
        }

        if (!stack.isEmpty()) {
            return stack.pop();
        } else {
            return new SyntaxNode("EMPTY");
        }
    }

    public void openParen() {
        consume();       
    }

    public SyntaxNode text() {
        StringBuilder buf = new StringBuilder();
        while (true) {
            if (peekToken() == OPENPAREN || peekToken() == CLOSEPAREN || peekToken() == EOI) {
                break;
            } 

            buf.append(nextToken());
        }

        SyntaxNode node = new SyntaxNode(buf.toString());
        return node;
    }

    public void closeParen() {
        consume();
        if (stack.isEmpty()) {
            return;
        }

        if (stack.size() > 1) {
            SyntaxNode child = stack.pop();
            SyntaxNode parent = stack.pop();
            parent.children.add(child);
            stack.push(parent);
        }
    }

    public void consume() {
        nextToken();
    }

    public char nextToken() {
        if (currentPos >= sentence.length()) {
            return EOI;
        }
        return sentence.charAt(currentPos++);
    }

    public char peekToken() {
        if (currentPos >= sentence.length()) {
            return EOI;
        }

        return sentence.charAt(currentPos);
    }
}
