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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.triptech.buildulator.FlashScope;
import net.triptech.buildulator.model.EnergySource;
import net.triptech.buildulator.model.Project;
import net.triptech.buildulator.model.Person;
import net.triptech.buildulator.model.bom.BillOfMaterials;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
     * Display the edit project form.
     *
     * @param id the id
     * @param uiModel the ui model
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/{id}", params = "edit", method = RequestMethod.GET)
    public String editForm(@PathVariable("id") Long id, Model uiModel,
            final HttpServletRequest request, final HttpServletResponse response) {

        String page = "projects/edit";

        Project project = Project.findProject(id);

        if (checkProjectPermission(project, request)) {
            uiModel.addAttribute("project", project);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            page = "resourceNotFound";
        }
        return page;
    }

    /**
     * Returns the project if the user has the rights to view it.
     * Otherwise a 404 error is returned.
     *
     * @param id the id
     * @param uiModel the ui model
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel,
            HttpServletRequest request, final HttpServletResponse response) {

        String page = "projects/show";

        Project project = Project.findProject(id);

        if (checkProjectPermission(project, request)) {
            uiModel.addAttribute("project", project);
            uiModel.addAttribute("bom", BillOfMaterials.parseJson(project.getData()));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            page = "resourceNotFound";
        }
        return page;
    }

    /**
     * Creates the project.
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
     * Update the project.
     *
     * @param project the project
     * @param bindingResult the binding result
     * @param uiModel the ui model
     * @param request the http servlet request
     * @param response the response
     * @return the string
     */
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Project project,
            BindingResult bindingResult, Model uiModel,
            HttpServletRequest request, final HttpServletResponse response) {

        String page = "resourceNotFound";

        Project existingProject = Project.findProject(project.getId());

        if (checkProjectPermission(existingProject, request)) {

            if (bindingResult.hasErrors()) {
                uiModel.addAttribute("project", project);
                FlashScope.appendMessage(
                        getMessage("metahive_object_validation", Project.class), request);
                page = "projects/edit";
            } else {
                existingProject.update(project);

                existingProject.persist();
                existingProject.flush();
                uiModel.asMap().clear();

                FlashScope.appendMessage(
                        getMessage("buildulator_edit_complete", Project.class), request);

                page = "redirect:/projects/" + encodeUrlPathSegment(
                        existingProject.getId().toString(), request);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return page;
    }

    /**
     * Clones the project.
     *
     * @param id the id
     * @param name the name
     * @param request the http servlet request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String clone(
            @RequestParam(value = "projectId", required = false) Long id,
            @RequestParam(value = "projectName", required = false) String name,
            final HttpServletRequest request, final HttpServletResponse response) {

        String page = "resourceNotFound";

        Project existingProject = null;

        if (id != null) {
            existingProject = Project.findProject(id);
        }

        if (checkProjectPermission(existingProject, request)) {

            Project clonedProject = existingProject.clone();

            if (StringUtils.isNotBlank(name)) {
                clonedProject.setName(name);
            }
            clonedProject.persist();
            clonedProject.flush();

            FlashScope.appendMessage(
                    getMessage("buildulator_clone_complete", Project.class), request);

            page = "redirect:/projects/" + encodeUrlPathSegment(
                        clonedProject.getId().toString(), request);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return page;
    }

    /**
     * Delete the project.
     *
     * @param id the id
     * @param request the http servlet request
     * @param response the http servlet response
     * @return the string
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id,
            final HttpServletRequest request, final HttpServletResponse response) {

        String page = "resourceNotFound";

        Project project = Project.findProject(id);

        if (checkProjectPermission(project, request)) {
            project.remove();

            FlashScope.appendMessage(
                    getMessage("buildulator_delete_complete", Project.class), request);

            page = "redirect:/projects";

            if (getProjectCount(request) == 0) {
                page = "redirect:/projects/new";
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return page;
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


    /**
     * Returns the project's bill of materials if the user has the rights to view it.
     * Otherwise a 404 error is returned.
     *
     * @param id the id
     * @param uiModel the ui model
     * @param request the request
     * @param response the response
     * @return the string
     */
    @RequestMapping(value = "/{id}/bom.json", method = RequestMethod.GET)
    public @ResponseBody String show(@PathVariable("id") Long id,
            HttpServletRequest request, final HttpServletResponse response) {

        String result = "";

        Project project = Project.findProject(id);

        if (checkProjectPermission(project, request)) {
            BillOfMaterials bom = BillOfMaterials.parseJson(project.getData());
            result = bom.toJson();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return result;
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

    /**
     * Check the project permissions.
     *
     * @param project the project
     * @param request the request
     * @return true, if successful
     */
    private boolean checkProjectPermission(final Project project,
            final HttpServletRequest request) {

        boolean showProject = false;

        // Check that the user owns the project
        if (project != null) {
            Person user = getUser(request);

            if (user != null && project.getPerson() != null) {
                if (user.getId() == project.getPerson().getId()) {
                    showProject = true;
                }
            } else {
                if (StringUtils.equalsIgnoreCase(
                        request.getSession().getId(), project.getSession())) {
                    showProject = true;
                }
            }
        }
        return showProject;
    }
}
