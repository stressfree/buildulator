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
package net.triptech.buildulator.service;

public class ServiceException extends Exception {

    /** The unique serial version UID for the class. */
    private static final long serialVersionUID = 200399041292945563L;

    /**
     * Constructs an instance of <code>ServiceException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ServiceException(final String msg) {
        super(msg);
    }

}
