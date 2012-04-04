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

/**
 * The Enum UserRole.
 */
public enum UserStatus {

    ACTIVE("label_net_triptech_buildulator_model_userstatus_active"),
    DISABLED("label_net_triptech_buildulator_model_userstatus_disabled");

    /** The message key. */
    private String messageKey;

    /**
     * Instantiates a new user status.
     *
     * @param name the name
     */
    private UserStatus(String name) {
        this.messageKey = name;
    }

    /**
     * Gets the message key.
     *
     * @return the message key
     */
    public String getMessageKey() {
        return messageKey;
    }
}