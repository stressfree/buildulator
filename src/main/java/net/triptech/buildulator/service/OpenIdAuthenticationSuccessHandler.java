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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.triptech.buildulator.model.Person;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * The Class OpenIdAuthenticationSuccessHandler.
 */
public class OpenIdAuthenticationSuccessHandler extends OpenIdAuthenticationBase
        implements AuthenticationSuccessHandler {

    public final void onAuthenticationSuccess(final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication) throws IOException {

        Person person = Person.findByOpenIdIdentifier(authentication.getName());

        if (person != null) {
            // Transfer any previous projects to the authenticated user
            transferProjects(request, person);
        }
        response.sendRedirect(request.getContextPath() + "/projects");
    }

}
