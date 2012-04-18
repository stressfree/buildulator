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
package net.triptech.buildulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.triptech.buildulator.DataParser;

import org.apache.commons.lang.StringUtils;



/**
 * The Class DataGrid.
 */
public class DataGrid {

    /** The title. */
    private String title;

    /** A map for holding the header data. */
    private TreeMap<Integer, String> header = new TreeMap<Integer, String>();

    /** A map for holding the body data. */
    private TreeMap<Integer, TreeMap<Integer, String>> body =
            new TreeMap<Integer, TreeMap<Integer, String>>();


    /**
     * Instantiates a new data grid.
     *
     * @param data the data
     */
    public DataGrid(final String data) {

        String[][] parsedData = DataParser.parseTextData(data);

        int y = 0;
        for (String[] row : parsedData) {
            if (y == 0) {
                // The first row of data is the header.
                for (String field : row) {
                    this.addHeaderField(field);
                }
            } else {
                this.addRow(row);
            }
            y++;
        }
    }

    /**
     * Instantiates a new data grid.
     */
    public DataGrid() {}

    /**
     * Sets the title.
     *
     * @param titleVal the new title
     */
    public final void setTitle(final String titleVal) {
        this.title = titleVal;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public final String getTitle() {
        if (StringUtils.isBlank(title)) {
            title = "";
        }
        return title;
    }

    /**
     * Adds a header field.
     *
     * @param headerField the header field
     */
    public void addHeaderField(final String headerField) {
        header.put(header.size(), headerField);
    }

    /**
     * Gets the header field for the supplied integer.
     *
     * @param index the index
     * @return the header field
     */
    public String getHeaderField(final int index) {
        String value = "";
        if (header.containsKey(index)) {
            value = header.get(index);
        }
        return value;
    }

    /**
     * Gets the header fields.
     *
     * @return the header fields
     */
    public List<String> getHeaderFields() {
        List<String> fields = new ArrayList<String>();

        for (int index : header.keySet()) {
            fields.add(header.get(index));
        }
        return fields;
    }


    /**
     * Adds the row.
     *
     * @param rowData the row data
     */
    public void addRow(final List<String> rowData) {
        String[] row = new String[rowData.size()];

        int i = 0;
        for (String value : rowData) {
            row[i] = value;
            i++;
        }
        this.addRow(row);
    }


    /**
     * Adds the row to the body.
     *
     * @param rowData the row data
     */
    public void addRow(final String[] rowData) {
        TreeMap<Integer, String> row = new TreeMap<Integer, String>();

        if (rowData != null) {
            int index = 1;
            for (String value : rowData) {
                row.put(index, value);
                index++;
            }
        }
        body.put(body.size(), row);
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    public final int getRowCount() {
        return body.size();
    }

    /**
     * Gets the row fields for the supplied index.
     *
     * @param rowNumber the row number
     * @return the row fields
     */
    public List<String> getRowFields(final int rowNumber) {
        List<String> data = new ArrayList<String>();

        if (body.containsKey(rowNumber)) {
            TreeMap<Integer, String> row = body.get(rowNumber);
            for (int index : row.keySet()) {
                data.add(row.get(index));
            }
        }
        return data;
    }

    /**
     * Gets the rows as a list.
     *
     * @return the list of row data
     */
    public List<List<String>> getRows() {
        List<List<String>> data = new ArrayList<List<String>>();

        for (int i : body.keySet()) {
            TreeMap<Integer, String> row = body.get(i);
            List<String> rowData = new ArrayList<String>();

            for (int index : row.keySet()) {
                rowData.add(row.get(index));
            }
            data.add(rowData);
        }
        return data;
    }

}
