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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.triptech.buildulator.FlashScope;
import net.triptech.buildulator.model.Person;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("/user")
@Controller
public class UserController extends BaseController {

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Person person, BindingResult bindingResult,
            Model uiModel, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("person", person);

            FlashScope.appendMessage(
                    getMessage("buildulator_object_validation", Person.class), request);

            return "user/update";
        }

        Person user = loadUser(request);

        if (user != null && StringUtils.equalsIgnoreCase(
                user.getOpenIdIdentifier(), person.getOpenIdIdentifier())) {
            // Only save the change if the logged in user is the same

            // Set some defaults from the current user
            person.setUserStatus(user.getUserStatus());
            person.setUserRole(user.getUserRole());

            uiModel.asMap().clear();
            person.merge();

            FlashScope.appendMessage(getMessage("buildulator_user_updated"), request);
        }

        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model uiModel, HttpServletRequest request) {

        String page = "redirect:/";

        Person person = loadUser(request);

        if (person != null) {
            page = "user/update";
            uiModel.addAttribute("person", person);
        }

        return page;
    }

    @ModelAttribute("controllerUrl")
    public final String getControllerUrl() {
    	return "/user";
    }
}
