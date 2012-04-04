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

import net.triptech.buildulator.model.UserRole;

/**
 * The Enum UserRole.
 */
public enum UserRole {

    ROLE_ADMIN("label_net_triptech_buildulator_model_userrole_admin"),
    ROLE_USER("label_net_triptech_buildulator_model_userrole_user"),
    ANONYMOUS("label_net_triptech_buildulator_model_userrole_anonymous");

    /** The message key. */
    private String messageKey;

    /**
     * Instantiates a new user role.
     *
     * @param name the name
     */
    private UserRole(String name) {
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


    /**
     * Check whether the user role meets the minimum role requirements.
     *
     * @param userAccessRole the user access role
     * @param minimumAccessRole the minimum access role
     * @return true, if successful
     */
    public static final boolean allowAccess(final UserRole userAccessRole,
            final UserRole minimumAccessRole) {

        boolean allowAccess = false;

        UserRole userRole = userAccessRole;
        UserRole accessRole = minimumAccessRole;

        if (userAccessRole == null) {
            userRole = UserRole.ANONYMOUS;
        }
        if (accessRole == null) {
            accessRole = UserRole.ANONYMOUS;
        }

        int userRolePos = 0;
        int accessRolePos = 0;
        int pos = 0;
        for (UserRole role : UserRole.values()) {
            if (userRole == role) {
                userRolePos = pos;
            }
            if (accessRole == role) {
                accessRolePos = pos;
            }
            pos++;
        }

        if (userRolePos <= accessRolePos) {
            allowAccess = true;
        }
        return allowAccess;
    }
}
