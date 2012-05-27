package net.triptech.buildulator.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.triptech.buildulator.DataParser;
import net.triptech.buildulator.model.bom.BillOfMaterials;
import net.triptech.buildulator.model.bom.Element;
import net.triptech.buildulator.model.bom.Material;
import net.triptech.buildulator.model.bom.Section;
import net.triptech.buildulator.model.bom.SustainabilitySummary;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Index;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class Project.
 */
@RooJavaBean
@RooJpaActiveRecord
public class Project {

    /** The operating energy key. */
    public static String OPERATING_ENERGY = "operating_energy";

    /** The construction key. */
    public static String CONSTRUCTION = "construction";

    /** The summary key. */
    public static String SUMMARY = "summary";

    /** The project name. */
    @NotNull
    private String name;

    /** A flag as to whether the project is a template. */
    private boolean template;

    /** A flag as to whether the project can be compared with others. */
    private boolean comparable;

    /** The location. */
    private String location;

    /** The occupants. */
    private double occupants = 4;

    /** The project description. */
    @Lob
    private String description;

    /** The project data. */
    @Lob
    private String data;

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

    /** The hash id. */
    @Index(name="hashIdentifier")
    private String hashId;


    /**
     * The on-create actions.
     */
    @PrePersist
    protected void onCreate() {
        created = new Date(Calendar.getInstance().getTimeInMillis());

        // Generate the hashId for this project
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(this.created.getTime()));
        sb.append("_");
        if (this.person == null) {
            sb.append(this.session);
        } else {
            sb.append(this.person.getId());
        }

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(sb.toString().getBytes(), 0, sb.length());
            this.hashId = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException nse) {
            System.out.println("The MD5 algorythm is not available: " + nse.getMessage());
        }

        if (StringUtils.isNotBlank(this.getData())) {
            this.recalculateTotals();
        }
    }

    /**
     * The pre-update actions.
     */
    @PreUpdate
    protected void preUpdate() {
        if (StringUtils.isNotBlank(this.getData())) {
            this.recalculateTotals();
        }
    }


    /**
     * Format the double to a string.
     *
     * @param value the value
     * @return the string
     */
    public String getFormattedOccupants() {
        String formattedOccupants = "";

        if (this.occupants != 0) {
            DecimalFormat df = new DecimalFormat("#.#");
            formattedOccupants = df.format(this.occupants);
        }
        return formattedOccupants;
    }

    /**
     * Sets the data field.
     *
     * @param key the key
     * @param dataString the data string
     */
    public final void setDataField(final String key, final String dataString) {

        Map<String, String> parsedData = this.parseData();

        parsedData.put(key, dataString);

        JSONObject jsonObject = JSONObject.fromObject(parsedData);

        this.setData(jsonObject.toString());
    }

    /**
     * Gets the data field.
     *
     * @param key the key
     * @return the data field
     */
    public final String getDataField(final String key) {
        String dataString = "";

        Map<String, String> parsedData = this.parseData();

        if (parsedData.containsKey(key)) {
            dataString = parsedData.get(key);
        }
        return dataString;
    }


    /**
     * Checks if the description is set.
     *
     * @return true, if is description set
     */
    public final boolean isDescriptionSet() {

        boolean descriptionSet = false;

        if (StringUtils.isNotBlank(DataParser.stripHtml(this.getDescription()))) {
            descriptionSet = true;
        }
        return descriptionSet;
    }

    /**
     * Update the project details based on the supplied project.
     *
     * @param project the project
     */
    public final void update(final Project project) {
        this.setName(project.getName());
        this.setTemplate(project.isTemplate());
        this.setComparable(project.isComparable());
        this.setLocation(project.getLocation());
        this.setOccupants(project.getOccupants());
        this.setDescription(project.getDescription());
    }

    /**
     * Clone the project.
     */
    public final Project clone() {

        Project project = new Project();

        project.setName("Copy of " + this.getName());
        project.setLocation(this.getLocation());
        project.setOccupants(this.getOccupants());
        project.setDescription(this.getDescription());
        project.setData(this.getData());
        project.setPerson(this.getPerson());
        project.setSession(this.getSession());

        return project;
    }

    /**
     * Recalculate the supplied list of projects.
     *
     * @param projects the projects
     */
    public static final void recalculate(final List<Project> projects) {
        Map<String, MaterialDetail> materials = new HashMap<String, MaterialDetail>();

        if (projects != null) {
            for (Project project : projects) {

                project.setDataField(OPERATING_ENERGY, recalculateDataField(
                        project.getDataField(OPERATING_ENERGY), materials));

                project.setDataField(CONSTRUCTION, recalculateDataField(
                        project.getDataField(CONSTRUCTION), materials));
            }
        }
    }

    /**
     * Covert the projects list to JSON.
     *
     * @param projects the projects
     * @param authorString the author string
     * @param anonymousString the anonymous string
     * @return the string
     */
    public static final String toJson(final List<Project> projects,
            final String anonymousString) {

        List<Map<String, Object>> projectsJson = new ArrayList<Map<String, Object>>();

        SimpleDateFormat presentation = new SimpleDateFormat("d MMM yyyy");
        SimpleDateFormat order = new SimpleDateFormat("yyyyMMdd:HH:mm");

        for (Project project : projects) {
            Map<String, Object> mJson = new LinkedHashMap<String, Object>();

            StringBuilder author = new StringBuilder();
            if (project.getPerson() != null) {
                Person person = project.getPerson();
                if (StringUtils.isNotBlank(person.getFirstName())) {
                    author.append(person.getFirstName());
                }
                if (author.length() > 0) {
                    author.append(" ");
                }
                if (StringUtils.isNotBlank(person.getLastName())) {
                    author.append(person.getLastName());
                }
            } else {
                author.append(anonymousString);
            }

            mJson.put("DT_RowId", project.getId());
            mJson.put("0", project.getName());
            mJson.put("1", project.getLocation());
            mJson.put("2", project.getOccupants());
            mJson.put("3", presentation.format(project.getCreated()));
            mJson.put("4", order.format(project.getCreated()));
            mJson.put("5", author.toString());
            mJson.put("6", project.isTemplate());
            mJson.put("7", project.isComparable());

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
     * Returns a JSON string for the comparable projects.
     *
     * @param project the project
     * @param userProjects the user projects
     * @return the string
     */
    public static final String toJson(final Project project,
            final List<Project> userProjects) {

        List<Project> comparableProjects = Project.findComparableProjects();

        if (comparableProjects == null) {
            comparableProjects = new ArrayList<Project>();
        }

        // Remove the current project from the user projects if it exists.
        if (userProjects != null) {
            List<Integer> removeIndexes = new ArrayList<Integer>();

            int index = 0;
            for (Project userProject : userProjects) {
                if (project.getId() == userProject.getId()) {
                    removeIndexes.add(index);
                } else {
                    for (Project compareableProject : comparableProjects) {
                        if (userProject.getId() == compareableProject.getId()) {
                            removeIndexes.add(index);
                        }
                    }
                }
                index++;
            }

            int removeCount = 0;
            for (int removeIndex : removeIndexes) {
                userProjects.remove(removeIndex - removeCount);
                removeCount++;
            }

            int removeComparable = -1;
            int comparableIndex = 0;
            for (Project comparableProject : comparableProjects) {
                if (project.getId() == comparableProject.getId()) {
                    removeComparable = comparableIndex;
                    comparableIndex++;
                }
            }

            if (removeComparable > -1) {
                comparableProjects.remove(removeComparable);
            }
        }

        Map<String, Object> projectJson = new LinkedHashMap<String, Object>();
        projectJson.put("id", project.getId());
        projectJson.put("name", project.getName());

        List<Map<String, Object>> comparableJson = new ArrayList<Map<String, Object>>();

        for (Project comparableProject : comparableProjects) {
            Map<String, Object> pjtJson = new LinkedHashMap<String, Object>();
            pjtJson.put("id", comparableProject.getId());
            pjtJson.put("name", comparableProject.getName());

            comparableJson.add(pjtJson);
        }

        List<Map<String, Object>> userJson = new ArrayList<Map<String, Object>>();

        if (userProjects != null) {
            for (Project userProject : userProjects) {
                Map<String, Object> pjtJson = new LinkedHashMap<String, Object>();
                pjtJson.put("id", userProject.getId());
                pjtJson.put("name", userProject.getName());

                userJson.add(pjtJson);
            }
        }

        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        jsonMap.put("project", projectJson);
        jsonMap.put("comparable", comparableJson);
        jsonMap.put("user", userJson);

        JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
    }



    /**
     * Find all of the project templates.
     *
     * @return the list
     */
    public static List<Project> findProjectTemplates() {

        TypedQuery<Project> q = entityManager().createQuery("SELECT p FROM Project AS p"
                + " WHERE p.template = :template ORDER BY p.name",
                Project.class);
        q.setParameter("template", true);

        return q.getResultList();
    }

    /**
     * Find all of the comparable projects.
     *
     * @return the list
     */
    public static List<Project> findComparableProjects() {

        TypedQuery<Project> q = entityManager().createQuery("SELECT p FROM Project AS p"
                + " WHERE p.comparable = :comparable ORDER BY p.name",
                Project.class);
        q.setParameter("comparable", true);

        return q.getResultList();
    }

    /**
     * Find a project based on the supplied hash id.
     *
     * @param hashIdentifier the hash identifier
     * @return the project
     */
    public static Project findProject(final String hashIdentifier) {

        Project project = null;

        if (StringUtils.isNotBlank(hashIdentifier)) {
            TypedQuery<Project> q = entityManager().createQuery(
                    "SELECT p FROM Project AS p WHERE p.hashId = :hashId", Project.class);
            q.setParameter("hashId", hashIdentifier);

            List<Project> projects = q.getResultList();

            project = projects.size() == 0 ? null : projects.get(0);
        }
        return project;
    }

    /**
     * Find all of the projects for a user in alphabetical order.
     *
     * @param user the user
     * @return the list
     */
    public static List<Project> findAllProjects(final Person user) {

        boolean viewExtras = canViewOrEditTemplatesOrComparables(user);

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT p FROM Project AS p JOIN p.person AS u");
        hql.append(" WHERE u.id = :userId");
        if (viewExtras) {
            hql.append(" OR p.template = :template OR p.comparable = :comparable");
        }
        hql.append(" ORDER BY p.name");

        TypedQuery<Project> q = entityManager().createQuery(hql.toString(),
                Project.class);
        q.setParameter("userId", user.getId());
        if (viewExtras) {
            q.setParameter("template", true);
            q.setParameter("comparable", true);
        }

        return q.getResultList();
    }

    /**
     * Count the projects for a user.
     *
     * @param user the user
     * @return the long
     */
    public static long countProjects(final Person user) {

        boolean viewExtras = canViewOrEditTemplatesOrComparables(user);

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT COUNT(p) FROM Project p JOIN p.person AS u");
        hql.append(" WHERE u.id = :userId");
        if (viewExtras) {
            hql.append(" OR p.template = :template OR p.comparable = :comparable");
        }

        TypedQuery<Long> q = entityManager().createQuery(hql.toString(), Long.class);
        q.setParameter("userId", user.getId());
        if (viewExtras) {
            q.setParameter("template", true);
            q.setParameter("comparable", true);
        }

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
     * Find all of the projects that mention the material detail name.
     *
     * @param materialName the material name
     * @return the list
     */
    public static List<Project> findProjectsWithMaterial(final String materialName) {

        String name = "%{\\\\\\\"mname\\\\\\\":\\\\\\\"" + materialName + "\\\\\\\",%";

        TypedQuery<Project> q = entityManager().createQuery("SELECT p FROM Project AS p"
               + " WHERE p.data LIKE :materialName", Project.class);
        q.setParameter("materialName", name);

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

    /**
     * Delete expired anonymous projects.
     */
    public static void deleteExpiredAnonymousProjects() {

        Calendar today = Calendar.getInstance();
        Date yesterday = new Date(today.getTimeInMillis() - 86400000);

        TypedQuery<Project> q = entityManager().createQuery("SELECT p FROM Project p"
                + " WHERE p.person IS NULL AND p.created < :yesterday",
                Project.class);
        q.setParameter("yesterday", yesterday);

        List<Project> expiredProjects = q.getResultList();

        for (Project project : expiredProjects) {
            project.remove();
        }
    }

    /**
     * Can view or edit templates or comparable projects.
     *
     * @param user the user
     * @return true, if successful
     */
    public static boolean canViewOrEditTemplatesOrComparables(final Person user) {
        boolean allowed = false;

        if (user != null && user.getUserRole() != null) {
            if (user.getUserRole() == UserRole.ROLE_ADMIN) {
                allowed = true;
            }
            if (user.getUserRole() == UserRole.ROLE_EDITOR) {
                allowed = true;
            }
        }
        return allowed;
    }


    /**
     * Recalculate the supplied data field JSON string.
     *
     * @param jsonString the json string
     * @param materials the materials
     * @return the string
     */
    private static String recalculateDataField(final String jsonString,
            final Map<String, MaterialDetail> materials) {

        BillOfMaterials bom = BillOfMaterials.parseJson(jsonString);

        for (Section section : bom.getSections()) {
            for (Element element : section.getElements()) {
                for (Material material : element.getMaterials()) {
                    if (!materials.containsKey(material.getName())) {
                        MaterialDetail md = MaterialDetail.findByName(material.getName());
                        if (md != null) {
                            materials.put(md.getName(), md);
                        }
                    }
                    if (materials.containsKey(material.getName())) {
                        MaterialDetail md = materials.get(material.getName());

                        double[] results = md.performCalculations(material.getQuantity());
                        material.setTotalEnergy(results[0]);
                        material.setTotalCarbon(results[1]);
                    }
                }
            }
        }
        bom.recalculateTotals();

        return bom.toJson();
    }

    /**
     * Parses the data and returns a map.
     *
     * @return the map
     */
    private Map<String, String> parseData() {

        Map<String, String> parsedData = new HashMap<String, String>();

        if (StringUtils.isNotBlank(this.getData())) {
            try {
                JSONObject json = (JSONObject) JSONSerializer.toJSON(this.getData());
                Iterator<?> iter = json.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = json.getString(key);

                    parsedData.put(key, value);
                }
            } catch (ClassCastException ce) {
                // Error parsing the data
            } catch (JSONException je) {
                // Error parsing JSON string
            }
        }
        return parsedData;
    }

    /**
     * Recalculate the totals for the project.
     *
     * @param parsedData the parsed data
     */
    private void recalculateTotals() {

        Map<String, String> parsedData = this.parseData();

        BillOfMaterials oe = new BillOfMaterials();
        BillOfMaterials bom = new BillOfMaterials();
        SustainabilitySummary summary = new SustainabilitySummary();

        // Get the operating energy BOM
        if (parsedData.containsKey(OPERATING_ENERGY)) {
            oe = BillOfMaterials.parseJson(parsedData.get(OPERATING_ENERGY));
        }

        // Get the construction BOM
        if (parsedData.containsKey(CONSTRUCTION)) {
            bom = BillOfMaterials.parseJson(parsedData.get(CONSTRUCTION));
        }

        // Get the summary object
        if (parsedData.containsKey(SUMMARY)) {
            summary = SustainabilitySummary.parseJson(parsedData.get(SUMMARY));
        }

        // Recalculate the summaries
        int[] oeDetailedTotals = this.getDetailedElements(oe);
        int[] bomDetailedTotals = this.getDetailedElements(bom);

        summary.setName(this.getName());
        summary.setElOperationalDetailed(oeDetailedTotals[0]);
        summary.setElOperationalTotal(oeDetailedTotals[1]);
        summary.setElConstructionDetailed(bomDetailedTotals[0]);
        summary.setElConstructionTotal(bomDetailedTotals[1]);
        summary.setOccupants(this.getOccupants());
        summary.setEnergyOperational(oe.getTotalEnergy());
        summary.setEnergyConstruction(bom.getTotalEnergy());
        summary.setCarbonOperational(oe.getTotalCarbon());
        summary.setCarbonConstruction(bom.getTotalCarbon());

        summary.getTotalEnergyChange().add(summary.getEnergyTotal());
        summary.getTotalCarbonChange().add(summary.getCarbonTotal());

        summary.getPerOccupantEnergyChange().add(summary.getEnergyPerOccupant());
        summary.getPerOccupantCarbonChange().add(summary.getCarbonPerOccupant());

        parsedData.put(SUMMARY, summary.toJson());

        JSONObject jsonObject = JSONObject.fromObject(parsedData);

        this.setData(jsonObject.toString());
    }

    /**
     * Gets the detailed elements.
     *
     * @param bom the bom
     * @return the detailed elements
     */
    private int[] getDetailedElements(final BillOfMaterials bom) {

        int detailed = 0, total = 0;

        if (bom.getSections() != null) {
            for (Section section : bom.getSections()) {
                if (section.getElements() != null) {
                    for (Element el : section.getElements()) {
                        total++;
                        if (el.getMaterials() != null && el.getMaterials().size() > 0) {
                            detailed++;
                        }
                    }
                }
            }
        }
        return new int[] { detailed, total };
    }
}
