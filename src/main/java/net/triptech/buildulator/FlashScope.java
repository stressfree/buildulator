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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The Class FlashScope.
 */
public final class FlashScope {

    /** The Constant FLASH_SCOPE_ATTRIBUTE. */
    static final String FLASH_SCOPE_ATTRIBUTE = FlashScope.class.getName();

    static final String FLASH_MESSAGE_ATTRIBUTE = "flash_message";

    /**
     * Gets the current flash scope.
     *
     * @param request the request
     * @return the current
     */
    public static Map<String, Object> getCurrent(HttpServletRequest request) {

        Map<String, Object> flashMap = new HashMap<String, Object>();

        HttpSession session = request.getSession();

        Map<?, ?> flash = (Map<?, ?>) session.getAttribute(FLASH_SCOPE_ATTRIBUTE);
        if (flash == null) {
            session.setAttribute(FLASH_SCOPE_ATTRIBUTE, flashMap);
        } else {
            for (Object key : flash.keySet()) {
                Object value = flash.get(key);
                flashMap.put((String) key, value);
            }
        }
        return flashMap;
    }

    /**
     * Appends a string message to the flash scope.
     *
     * @param message the message
     * @param request the request
     */
    public static void appendMessage(final String message,
            final HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();

        HttpSession session = request.getSession();

        Map<String, Object> flashMap = FlashScope.getCurrent(request);

        if (flashMap.containsKey(FLASH_MESSAGE_ATTRIBUTE)) {
            sb.append(flashMap.get(FLASH_MESSAGE_ATTRIBUTE));
            sb.append("<br />");
        }
        sb.append(message);

        flashMap.put(FLASH_MESSAGE_ATTRIBUTE, sb.toString());
        request.setAttribute(FLASH_MESSAGE_ATTRIBUTE, sb.toString());

        session.setAttribute(FLASH_SCOPE_ATTRIBUTE, flashMap);
    }

    /**
     * Instantiates a new flash scope.
     */
    private FlashScope() {
    }

}
