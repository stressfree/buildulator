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

import net.triptech.buildulator.model.Preferences;
import net.triptech.buildulator.model.Project;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/cron")
@Controller
public class CronController extends BaseController {

    /** The logger. */
    private static Logger logger = Logger.getLogger(CronController.class);


    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String runCron(final HttpServletRequest request) {

        String result = "Cron ran successfully";

        logger.info("Executing cron task");

        try {
            Project.deleteExpiredAnonymousProjects();
        } catch (Exception e) {
            String message = "Error expiring anonymous projects: " + e.getMessage();

            result = message;
            logger.error(message);
        }

        try {
            Preferences preferences = this.getPreferences(request);

            List<Project> projects = new ArrayList<Project>();

            for (Long id : preferences.getRefreshProjectMap().keySet()) {
                Project project = Project.findProject(id);
                if (project != null) {
                    projects.add(project);
                }
            }

            Project.recalculate(projects);

            // Reset the refresh project list
            preferences.setRefreshProjectList("");
            preferences.setRefreshProjectMap(null);
            preferences.merge();
            preferences.flush();
        } catch (Exception e) {
            String message = "Error recalculating outdated projects: " + e.getMessage();

            result = message;
            logger.error(message);
        }

        logger.info("Cron task execution complete");

        return result;
    }

}
