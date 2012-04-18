/*******************************************************************************
 * Copyright (c) 2012 David Harrison, Triptech Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     David Harrison, Triptech Ltd - initial API and implementation
 ******************************************************************************/
package net.triptech.buildulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.TreeMap;

import net.triptech.buildulator.SmartTokenizer;


/**
 * The Class DataParser.
 */
public class DataParser {

    /**
     * Parses the text data.
     *
     * @param text the text
     *
     * @return the tree map< integer, tree map< integer, string>>
     */
    public static String[][] parseTextData(final String text) {

        TreeMap<Integer, TreeMap<Integer, String>> rowData =
            new TreeMap<Integer, TreeMap<Integer, String>>();

        // This counter holds the maximum number of columns provided
        int maxNumberOfTokens = 0;

        if (text != null) {
            BufferedReader in = new BufferedReader(new StringReader(text));

            String line;
            int lineCounter = 0;

            try {
                while ((line = in.readLine()) != null) {
                    TreeMap<Integer, String> parsedLine = new TreeMap<Integer, String>();

                    SmartTokenizer tabTokenizer = new SmartTokenizer(line, "\t");
                    if (tabTokenizer.countTokens() > 1) {
                        parsedLine = tokenizerToMap(tabTokenizer);
                    } else {
                        SmartTokenizer commaTokenizer = new SmartTokenizer(line, ",");
                        parsedLine = tokenizerToMap(commaTokenizer);
                    }
                    if (parsedLine.size() > maxNumberOfTokens) {
                        maxNumberOfTokens = parsedLine.size();
                    }

                    rowData.put(lineCounter, parsedLine);
                    lineCounter++;
                }
            } catch (IOException ioe) {
                // Error reading string
            }
        }

        String[][]parsedData = new String[rowData.size()][];

        // Now cycle through all the parsed data
        // Ensure that each row has the same (max) number of tokens
        for (int rowIndex : rowData.keySet()) {
            TreeMap<Integer, String> parsedLine = rowData.get(rowIndex);

            // This map holds the final values
            TreeMap<Integer, String> columnTokens = new TreeMap<Integer, String>();

            for (int i = 0; i < maxNumberOfTokens; i++) {
                String value = "";
                if (parsedLine.containsKey(i)) {
                    value = parsedLine.get(i);
                }
                columnTokens.put(i, value);
            }

            parsedData[rowIndex] = new String[columnTokens.size()];

            for (int columnIndex : columnTokens.keySet()) {
                String value = columnTokens.get(columnIndex);
                parsedData[rowIndex][columnIndex] = value;
            }
        }
        return parsedData;
    }

    /**
     * Tokenizer to map.
     *
     * @param tokenizer the tokenizer
     *
     * @return the tree map< integer, string>
     */
    private static TreeMap<Integer, String> tokenizerToMap(
            final SmartTokenizer tokenizer) {

        TreeMap<Integer, String> parsedData = new TreeMap<Integer, String>();

        int lineCounter = 0;
        if (tokenizer != null) {
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                parsedData.put(lineCounter, token.trim());
                lineCounter++;
            }
        }
        return parsedData;
    }

}
