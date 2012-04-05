package net.triptech.buildulator.model;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@RooJpaActiveRecord
public class Project {

    /** The project name. */
    @NotNull
    private String name;

}
