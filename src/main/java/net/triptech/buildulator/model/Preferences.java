package net.triptech.buildulator.model;

import java.util.List;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.triptech.buildulator.model.Preferences;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord
public class Preferences {

    /** The homepage title. */
    @NotNull
    @Size(min = 2, max = 200)
    private String homepageTitle;

    /** The homepage content. */
    @Lob
    private String homepageContent;

    /** The google tracking id. */
    private String googleTrackingId;


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
