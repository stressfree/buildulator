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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.triptech.buildulator.FlashScope;
import net.triptech.buildulator.model.EnergySource;
import net.triptech.buildulator.model.Project;
import net.triptech.buildulator.model.Person;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The Class ProjectController.
 */
@RequestMapping("/projects")
@Controller
public class ProjectController extends BaseController {

    /**
     * Index.
     *
     * @param request the request
     * @return the string
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(final HttpServletRequest request) {
        String page = "projects/list";

        if (getProjectCount(request) == 0) {
            page = "redirect:/projects/new";
        }
        return page;
    }

    /**
     * Creates the definition.
     *
     * @param definitionForm the definition form
     * @param bindingResult the binding result
     * @param uiModel the ui model
     * @param request the http servlet request
     * @return the string
     */
    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Project project,
            BindingResult bindingResult, Model uiModel,
            HttpServletRequest request) {

        Person user = getUser(request);

        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("project", project);
            FlashScope.appendMessage(
                    getMessage("buildulator_object_validation", Project.class), request);
            return "projects/create";
        }
        uiModel.asMap().clear();

        if (user == null) {
            String sessionId = request.getSession().getId();

            // Set the session id as the owner
            project.setSession(sessionId);

            // Store the existing session id in case the user logs in
            request.getSession().setAttribute("PreviousSessionId", sessionId);
        } else {
            // Set the logged in person as the owner
            project.setPerson(user);
        }

        project.persist();
        project.flush();

        FlashScope.appendMessage(
                getMessage("buildulator_create_complete", Project.class), request);

        return "redirect:/projects/"
                + encodeUrlPathSegment(project.getId().toString(), request);
    }

    /**
     * Returns the new project form.
     *
     * @param uiModel the ui model
     * @return the string
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("project", new Project());
        return "projects/create";
    }

    /**
     * List the projects.
     *
     * @return the string
     */
    @RequestMapping(value = "/list.json", method = RequestMethod.GET)
    public @ResponseBody String listProjects(final HttpServletRequest request) {
        return Project.toJson(loadProjects(request));
    }


    @ModelAttribute("energySources")
    public final List<EnergySource> getEnergySources() {
        return EnergySource.findAllEnergySources();
    }

    @ModelAttribute("controllerUrl")
    public final String getControllerUrl() {
        return "/projects";
    }

    @ModelAttribute("controllerName")
    public final String getControllerName() {
        return getMessage("controller_projects");
    }

    /**
     * Load projects.
     *
     * @param request the request
     * @return the list
     */
    private List<Project> loadProjects(final HttpServletRequest request) {

        List<Project> projects = new ArrayList<Project>();

        Person user = getUser(request);

        if (user == null) {
            projects = Project.findAllProjects(request.getSession().getId());
        } else {
            projects = Project.findAllProjects(user);
        }
        return projects;
    }
}
