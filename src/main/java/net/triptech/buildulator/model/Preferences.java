package net.triptech.buildulator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.triptech.buildulator.model.Preferences;

import org.apache.commons.lang.StringUtils;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

/**
 * The Class Preferences.
 */
@RooJavaBean
@RooJpaActiveRecord
public class Preferences {

    /** The name. */
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    /** The copyright notice. */
    @Size(max = 100)
    private String copyrightNotice;

    /** The application url. */
    @NotNull
    @Size(min = 10, max = 200)
    private String url;

    /** The homepage title. */
    @NotNull
    @Size(min = 2, max = 200)
    private String homepageTitle;

    /** The homepage content. */
    @Lob
    private String homepageContent;

    /** The flag to indicate whether the about page is enabled. */
    private boolean aboutEnabled;

    /** The about page title. */
    @NotNull
    @Size(min = 2, max = 200)
    private String aboutTitle;

    /** The about page content. */
    @Lob
    private String aboutContent;

    /** The flag to indicate whether terms and conditions are enabled. */
    private boolean tocEnabled;

    /** The terms and conditions title. */
    @NotNull
    @Size(min = 2, max = 200)
    private String tocTitle;

    /** The terms and conditions content. */
    @Lob
    private String tocContent;

    /** The google tracking id. */
    private String googleTrackingId;

    /** The target project id. */
    private long targetProjectId;

    /** The refresh project list. */
    private String refreshProjectList;

    @Transient
    private Map<Long, Long> refreshProjectMap;


    public final Map<Long, Long> getRefreshProjectMap() {
        if (this.refreshProjectMap == null) {
            this.refreshProjectMap = new HashMap<Long, Long>();

            if (StringUtils.isNotBlank(this.refreshProjectList)) {
                StringTokenizer tk = new StringTokenizer(this.refreshProjectList, ",");

                while (tk.hasMoreTokens()) {
                    String token = tk.nextToken();
                    try {
                        Long parsedId = Long.parseLong(token);
                        this.refreshProjectMap.put(parsedId, parsedId);
                    } catch (NumberFormatException nfe) {
                        // Error parsing to a Long
                    }
                }
            }
        }
        return this.refreshProjectMap;
    }

    /**
     * Add projects to the refresh list.
     *
     * @param projects the projects
     * @return true, if successful
     */
    public final boolean addProjectsToRefresh(final List<Long> projects) {

        boolean projectAdded = false;

        for (Long projectId : projects) {
            if (projectId != null && projectId > 0
                    && !this.getRefreshProjectMap().containsKey(projectId)) {
                this.getRefreshProjectMap().put(projectId, projectId);
                projectAdded = true;
            }
        }

        if (projectAdded) {
            // Update the string representation
            StringBuilder sb = new StringBuilder();
            for (Long key : this.getRefreshProjectMap().keySet()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(key);
            }
            this.setRefreshProjectList(sb.toString());
        }
        return projectAdded;
    }

    /**
     * Load the preferences.
     *
     * @return the preferences
     */
    public static Preferences load() {

        List<Preferences> prefs = Preferences.findAllPreferenceses();

        Preferences preferences = prefs.size() == 0 ? null : prefs.get(0);

        if (preferences == null) {
            preferences = new Preferences();
        }
        return preferences;
    }

}
