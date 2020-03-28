/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.insolina.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import org.insolina.config.Config;

/**
 *
 * @author Nigel Currie
 */
public class NLPParser {
    Parser parser;
    
    /**
     * Construct the parser with the configured model
     */
    public NLPParser() {
        try (InputStream modelIn 
                = new FileInputStream(Config.getString("nlp.parsing.model"));) {
          ParserModel model = new ParserModel(modelIn);
          parser = ParserFactory.create(model);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
     * Return the first parse 
     * 
     * @param in the input text
     * @return the first parse of the text
     */
    public String parse(final String in) {
        Parse parses[] = ParserTool.parseLine(in, parser, 1);
        StringBuffer buf = new StringBuffer();
        parses[0].show(buf);
        
        return buf.toString();
    }
}
