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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/error")
@Controller
public class ErrorController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String error() {
        return "uncaughtException";
    }

    @RequestMapping(value = "/resourceNotFound", method = RequestMethod.GET)
    public String resourceNotFound() {
        return "resourceNotFound";
    }

    @RequestMapping(value = "/dataAccessFailure", method = RequestMethod.GET)
    public String dataAccessFailure() {
        return "dataAccessFailure";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied() {
        return "accessDenied";
    }

    @RequestMapping(value = "/accountDisabled", method = RequestMethod.GET)
    public String accountDisabled() {
        return "accountDisabled";
    }

    @ModelAttribute("controllerUrl")
    public final String getControllerUrl() {
        return "/error";
    }

    @ModelAttribute("controllerName")
    public final String getControllerName() {
        return getMessage("controller_error");
    }
}
