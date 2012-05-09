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
package net.triptech.buildulator.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import net.triptech.buildulator.model.Preferences;
import net.triptech.buildulator.model.Person;
import net.triptech.buildulator.model.Project;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;



public abstract class BaseController {

    @Autowired
    private ApplicationContext context;


    /**
     * Gets the application context.
     *
     * @return the context
     */
    protected final ApplicationContext getContext() {
        return context;
    }

    /**
     * Gets the translated message.
     *
     * @param code the code
     * @return the message
     */
    protected final String getMessage(final String code) {
        return context.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * The result count increments.
     *
     * @return the integer[]
     */
    protected Integer[] resultCounts() {
        return new Integer[] { 50, 100, 200 };
    }

    /**
     * Gets the translated message.
     *
     * @param code the code
     * @param objectClass the object class
     * @return the message
     */
    protected final String getMessage(final String code, final Class<?> objectClass) {

        String objectCode = StringUtils.replace(objectClass.getName(), ".", "_");

        String argument = context.getMessage("label_" + objectCode.toLowerCase(), null,
                LocaleContextHolder.getLocale());

        return context.getMessage(code, new String[] { argument.toLowerCase() },
                LocaleContextHolder.getLocale());
    }

    /**
     * Load the person from the request.
     *
     * @param request the request
     * @return the person
     */
    protected final Person getUser(final HttpServletRequest request) {

        Person user = null;

        if (request.getUserPrincipal() != null
                && StringUtils.isNotBlank(request.getUserPrincipal().getName())) {
            user = Person.findByOpenIdIdentifier(request.getUserPrincipal().getName());
        }
        return user;
    }

    public long getProjectCount(final HttpServletRequest request) {

        long count = 0;

        Person user = getUser(request);

        if (user == null) {
            count = Project.countProjects(request.getSession().getId());
        } else {
            count = Project.countProjects(user);
        }
        return count;
    }

    @ModelAttribute("user")
    public Person getCurrentUser(final HttpServletRequest request) {
        return getUser(request);
    }

    @ModelAttribute("preferences")
    public Preferences getPreferences() {
        return Preferences.load();
    }

    /**
     * Encode the url path segment.
     *
     * @param pathSegment the path segment
     * @param httpServletRequest the http servlet request
     * @return the string
     */
    protected String encodeUrlPathSegment(String pathSegment,
            HttpServletRequest httpServletRequest) {

        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

}
