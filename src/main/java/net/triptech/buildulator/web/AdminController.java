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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;
import net.triptech.buildulator.FlashScope;
import net.triptech.buildulator.model.Person;
import net.triptech.buildulator.model.Preferences;
import net.triptech.buildulator.model.UserRole;
import net.triptech.buildulator.model.UserStatus;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@Valid Preferences preferences,
            BindingResult bindingResult, Model uiModel, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("preferences", preferences);

            FlashScope.appendMessage(getMessage("buildulator_object_validation",
                    Preferences.class), request);

            return "preferences/update";
        }

        uiModel.asMap().clear();
        if (preferences.getId() != null) {
            // Updating existing preferences
            preferences.merge();
        } else {
            // No preferences exist yet
            preferences.persist();
            preferences.flush();
        }
        FlashScope.appendMessage(getMessage("preferences_edited"), request);

        return "redirect:/admin";
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateForm(Model uiModel) {
        uiModel.addAttribute("preferences", this.getPreferences());
        return "admin/update";
    }

    /**
     * Update the user.
     *
     * @param id the id
     * @param colId the col id
     * @param value the value
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody String updateUser(
            @RequestParam(value = "id", required = true) final String id,
            @RequestParam(value = "columnPosition", required = true) final Integer colId,
            @RequestParam(value = "value", required = true) final String value,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        String returnMessage = "";

        Person person = Person.findByEmailAddress(id);

        if (person != null) {
            try {
                returnMessage = person.set(colId, value, this.getContext());
                person.merge();
                person.flush();
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                returnMessage = this.getMessage("users_update_error");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            returnMessage = this.getMessage("users_update_notfounderror");
        }
        return returnMessage;
    }

    /**
     * Delete the user.
     *
     * @param id the id
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/users/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody String deleteUser(
            @RequestParam(value = "id", required = true) final String id,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        String returnMessage = "";

        Person person = Person.findByEmailAddress(id);

        if (person != null) {
            try {
                person.remove();
                returnMessage = "ok";
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                returnMessage = this.getMessage("users_delete_error");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            returnMessage = this.getMessage("users_delete_notfounderror");
        }
        return returnMessage;
    }

    @RequestMapping(value = "/users/roles.json", method = RequestMethod.GET)
    public @ResponseBody String roles(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        Map<String, String> jsonMap = new LinkedHashMap<String, String>();

        for (UserRole role : UserRole.values()) {
            String name = getMessage(role.getMessageKey());
            jsonMap.put(name, name);
        }
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
    }

    @RequestMapping(value = "/users/statuses.json", method = RequestMethod.GET)
    public @ResponseBody String statuses(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        Map<String, String> jsonMap = new LinkedHashMap<String, String>();

        for (UserStatus status : UserStatus.values()) {
            String name = getMessage(status.getMessageKey());
            jsonMap.put(name, name);
        }
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
    }

    /**
     * List the users.
     *
     * @return the string
     */
    @RequestMapping(value = "/users/list.json", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody String list() {
        return Person.toJson(Person.findAllPeople(), this.getContext());
    }

}
