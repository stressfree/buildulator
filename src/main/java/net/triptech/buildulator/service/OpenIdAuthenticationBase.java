package net.triptech.buildulator.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.triptech.buildulator.model.Person;
import net.triptech.buildulator.model.Project;

import org.apache.commons.lang.StringUtils;


/**
 * The Class OpenIdAuthenticationBase.
 */
public abstract class OpenIdAuthenticationBase {

    /**
     * Transfer projects.
     *
     * @param request the request
     * @param person the person
     */
    protected final void transferProjects(final HttpServletRequest request,
            final Person person) {

        String id = (String) request.getSession().getAttribute("PreviousSessionId");

        if (StringUtils.isNotBlank(id)) {
            // Load projects associated with the old session and assign it to the user
            List<Project> pjs = Project.findAllProjects(id);

            for (Project project : pjs) {
                project.setSession(null);
                project.setPerson(person);

                project.merge();
                project.flush();
            }
        }
    }

}
