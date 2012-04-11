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

/**
 * The Class BuildulatorException.
 *
 * @author David Harrison
 */
public class BuildulatorException extends Exception {

    /** The unique serial version UID for the class. */
    private static final long serialVersionUID = 8811011052659544L;


    /**
     * Creates a new instance of <code>BuildulatorException</code>
     * without detail message.
     */
    public BuildulatorException() {
    }


    /**
     * Constructs an instance of <code>BuildulatorException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BuildulatorException(final String msg) {
        super(msg);
    }
}
