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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/cron")
@Controller
public class CronController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String runCron() {

        String result = "Cron ran successfully";

        try {
            Project.deleteExpiredAnonymousProjects();
        } catch (Exception e) {
            result = "Error running cron: " + e.getMessage();
        }
        return result;
    }

}
