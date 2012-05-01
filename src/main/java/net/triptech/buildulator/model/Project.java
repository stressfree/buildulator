package net.triptech.buildulator.model;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class Project.
 */
@RooJavaBean
@RooJpaActiveRecord
public class Project {

    /** The project name. */
    @NotNull
    private String name;

    /** The location. */
    private String location;

    /** The occupants. */
    private int occupants;

    /** The energy consumption. */
    private double energyConsumption;

    /** The project description. */
    @Lob
    private String description;

}
