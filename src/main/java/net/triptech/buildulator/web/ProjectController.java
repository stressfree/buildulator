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

import net.triptech.buildulator.model.Project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Class ProjectController.
 */
@RequestMapping("/projects")
@Controller
public class ProjectController extends BaseController {

    /**
     * Index.
     *
     * @return the string
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "projects/list";
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

    @ModelAttribute("controllerUrl")
    public final String getControllerUrl() {
        return "/projects";
    }

    @ModelAttribute("controllerName")
    public final String getControllerName() {
        return getMessage("controller_projects");
    }
}
