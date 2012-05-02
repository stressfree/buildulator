package net.triptech.buildulator.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import net.sf.json.JSONObject;

import org.hibernate.annotations.Index;
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
    private int occupants = 4;

    /** The energy consumption. */
    private double energyConsumption;

    /** The energy source. */
    @ManyToOne
    @Index(name="energySourceIndex")
    @NotNull
    private EnergySource energySource;

    /** The project description. */
    @Lob
    private String description;

    /** The energy source. */
    @ManyToOne
    @Index(name="ownerPerson")
    private Person person;

    /** The energy source. */
    @Index(name="ownerSession")
    private String session;

    /** The created timestamp. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    /**
     * The on create actions.
     */
    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    /**
     * Covert the projects list to JSON.
     *
     * @param projects the projects
     * @return the string
     */
    public static final String toJson(final List<Project> projects) {

        List<Map<String, Object>> projectsJson = new ArrayList<Map<String, Object>>();

        SimpleDateFormat presentation = new SimpleDateFormat("d MMM yyyy");
        SimpleDateFormat order = new SimpleDateFormat("yyyyMMdd:HH:mm");

        for (Project project : projects) {
            Map<String, Object> mJson = new LinkedHashMap<String, Object>();

            mJson.put("DT_RowId", project.getId());
            mJson.put("0", project.getName());
            mJson.put("1", project.getLocation());
            mJson.put("2", project.getOccupants());
            mJson.put("3", presentation.format(project.getCreated()));
            mJson.put("4", order.format(project.getCreated()));

            projectsJson.add(mJson);
        }

        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        jsonMap.put("sEcho", 1);
        jsonMap.put("iTotalRecords", projects.size());
        jsonMap.put("iTotalDisplayRecords", projects.size());
        jsonMap.put("aaData", projectsJson);

        JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
    }

    /**
     * Find all of the projects for a user in alphabetical order.
     *
     * @return the list
     */
    public static List<Project> findAllProjects(final Person user) {

        TypedQuery<Project> q = entityManager().createQuery("SELECT p FROM Project AS p"
                + " JOIN p.person AS u WHERE u.id = :userId ORDER BY p.name",
                Project.class);
        q.setParameter("userId", user.getId());

        return q.getResultList();
    }

    /**
     * Count the projects for a user.
     *
     * @param user the user
     * @return the long
     */
    public static long countProjects(final Person user) {

        TypedQuery<Long> q = entityManager().createQuery("SELECT COUNT(p) FROM Project p"
                + " JOIN p.person AS u WHERE u.id = :userId", Long.class);
        q.setParameter("userId", user.getId());

        return q.getSingleResult();
    }

    /**
     * Find all of the projects for a session id in alphabetical order.
     *
     * @param sessionId the session id
     * @return the list
     */
    public static List<Project> findAllProjects(final String sessionId) {

        TypedQuery<Project> q = entityManager().createQuery("SELECT p FROM Project AS p"
                + " WHERE lower(p.session) = :sessionId ORDER BY p.name", Project.class);
        q.setParameter("sessionId", sessionId);

        return q.getResultList();
    }

    /**
     * Count the projects for a session id.
     *
     * @param sessionId the session id
     * @return the long
     */
    public static long countProjects(final String sessionId) {

        TypedQuery<Long> q = entityManager().createQuery("SELECT COUNT(p) FROM Project p"
                + " WHERE p.session = :sessionId", Long.class);
        q.setParameter("sessionId", sessionId);

        return q.getSingleResult();
    }

}
