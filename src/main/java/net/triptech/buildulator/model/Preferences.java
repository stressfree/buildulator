package net.triptech.buildulator.model;

import java.util.List;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.triptech.buildulator.model.Preferences;

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
