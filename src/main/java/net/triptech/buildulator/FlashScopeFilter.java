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
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The Class FlashScopeFilter.
 */
public class FlashScopeFilter extends OncePerRequestFilter {

    /**
     * The doFilterInternal method.
     *
     * @param request the http servlet request
     * @param response the http servlet response
     * @param filterChain the filter chain
     * @throws ServletException the servlet exception
     * @throws IOException the IO exception
     */
    @Override
    protected final void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
        throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<?, ?> flash = (Map<?, ?>) session.getAttribute(
                    FlashScope.FLASH_SCOPE_ATTRIBUTE);
            if (flash != null) {
                for (Object key : flash.keySet()) {
                    Object value = flash.get(key);
                    Object currentValue = request.getAttribute((String) key);

                    if (currentValue == null) {
                        request.setAttribute((String) key, value);
                    }
                }
                session.removeAttribute(FlashScope.FLASH_SCOPE_ATTRIBUTE);
            }
        }
        filterChain.doFilter(request, response);
    }

}
