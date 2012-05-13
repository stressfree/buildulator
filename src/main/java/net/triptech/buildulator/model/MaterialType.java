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

import net.triptech.buildulator.model.MaterialType;

/**
 * The Enum MaterialType.
 */
public enum MaterialType {

    CONSTRUCTION(
            "label_net_triptech_buildulator_model_materialtype_construction",
            "(${quantity}*${coefficient}*(${wastage})/100)/${lifespan}"),
    ENERGY_SOURCE(
            "label_net_triptech_buildulator_model_materialtype_energysource",
            "${quantity}*${coefficient}");

    /** The message key. */
    private String messageKey;

    private String calculation;

    /**
     * Instantiates a new user role.
     *
     * @param name the name
     */
    private MaterialType(String name, final String calc) {
        this.messageKey = name;
        this.calculation = calc;
    }

    /**
     * Gets the message key.
     *
     * @return the message key
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Gets the calculation.
     *
     * @return the calculation
     */
    public String getCalculation() {
        return this.calculation;
    }

}
