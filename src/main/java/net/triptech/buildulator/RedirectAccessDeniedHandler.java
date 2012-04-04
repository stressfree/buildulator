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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;


/**
 * The Class RedirectAccessDeniedHandler.
 */
public class RedirectAccessDeniedHandler implements AccessDeniedHandler {

    /** The access denied url. */
    private String accessDeniedUrl;


    /**
     * Instantiates a new redirect access denied handler.
     */
    public RedirectAccessDeniedHandler() {
    }

    /**
     * Instantiates a new redirect access denied handler.
     *
     * @param accessDeniedUrlVal the access denied url
     */
    public RedirectAccessDeniedHandler(final String accessDeniedUrlVal) {
        this.accessDeniedUrl = accessDeniedUrlVal;
    }

    /**
     * Handle the access denied action.
     *
     * @param request the request
     * @param response the response
     * @param accessDeniedException the access denied exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
    public final void handle(final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.sendRedirect(this.accessDeniedUrl);
    }

    /**
     * Gets the access denied url.
     *
     * @return the access denied url
     */
    public final String getAccessDeniedUrl() {
        return accessDeniedUrl;
    }

    /**
     * Sets the access denied url.
     *
     * @param accessDeniedUrlVal the new access denied url
     */
    public final void setAccessDeniedUrl(final String accessDeniedUrlVal) {
        this.accessDeniedUrl = accessDeniedUrlVal;
    }

}
