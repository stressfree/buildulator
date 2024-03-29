package net.triptech.buildulator.model;

import com.darius.Parser;
import com.darius.SyntaxException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.triptech.buildulator.BuildulatorException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

/**
 * The Class MaterialDetail.
 */
@RooJavaBean
@RooJpaActiveRecord
public class MaterialDetail {

    /** The logger. */
    private static Logger logger = Logger.getLogger(MaterialDetail.class);

    /** The name of the material. */
    @NotNull
    @Size(min = 1, max = 100)
    @Column(unique = true)
    private String name;

    /** The previous name. */
    @Transient
    private String previousName;

    /** The type of material. */
    @NotNull
    @Enumerated(EnumType.STRING)
    private MaterialType materialType = MaterialType.CONSTRUCTION;

    /** The unit of measure. */
    @Size(max = 50)
    private String unitOfMeasure;

    /** The energy per unit. */
    private double energyPerUnit;

    /** The carbon per unit. */
    private double carbonPerUnit;

    /** The life span in years of the material. */
    private int lifeYears;

    /** The wastage percent. */
    private double wastagePercent;


    /**
     * Sets the name for the material detail.
     *
     * @param nameVal the new name
     */
    public final void setName(final String nameVal) {
        this.previousName = this.name;
        this.name = nameVal;
    }

    /**
     * The post-update actions for a material detail.
     */
    public List<Long> postUpdate() {

        List<Long> affected = new ArrayList<Long>();

        if (StringUtils.isNotBlank(this.previousName) &&
                !StringUtils.equals(this.name, this.previousName)) {

            List<Project> affectedProjects = Project.findProjectsWithMaterial(
                    this.previousName);

            for (Project project : affectedProjects) {
                String existing = "{\\\"mname\\\":\\\"" + this.previousName + "\\\",";
                String update = "{\\\"mname\\\":\\\"" + this.name + "\\\",";

                String newData = StringUtils.replace(project.getData(), existing, update);
                project.setData(newData);

                project.merge();
                project.flush();
            }
        }

        List<Project> affectedProjects = Project.findProjectsWithMaterial(this.getName());

        for (Project project : affectedProjects) {
            affected.add(project.getId());
        }
        return affected;
    }

    /**
     * Perform calculations.
     *
     * @param quantity the quantity
     * @return the double[]
     */
    public double[] performCalculations(final double quantity) {

        double energy = 0, carbon = 0;

        if (this.materialType != null
                && StringUtils.isNotBlank(this.materialType.getCalculation())) {

            String energyCalculation = buildCalc(quantity, this.getEnergyPerUnit());
            String carbonCalculation = buildCalc(quantity, this.getCarbonPerUnit());

            try {
                energy = Parser.parse(energyCalculation).value();
            } catch (SyntaxException se) {
                logger.error("Error parsing energy calculation: " + se.getMessage());
            }

            try {
                carbon = Parser.parse(carbonCalculation).value();
            } catch (SyntaxException se) {
                logger.error("Error parsing energy calculation: " + se.getMessage());
            }
        }
        return new double[] { energy, carbon };
    }

    /**
     * Covert the materials to a JSON list.
     *
     * @param materials the MaterialDetails
     * @param context the context
     * @return the string
     */
    public static final String toJson(final List<MaterialDetail> materials) {

        List<Map<String, Object>> materialsJson = new ArrayList<Map<String, Object>>();

        for (MaterialDetail material : materials) {
            Map<String, Object> mJson = new LinkedHashMap<String, Object>();

            mJson.put("name", material.getName());
            mJson.put("units", material.getUnitOfMeasure());

            materialsJson.add(mJson);
        }
        JSONArray json = JSONArray.fromObject(materialsJson);

        return json.toString();
    }

    /**
     * Covert the materials to a JSON list.
     *
     * @param materials the MaterialDetails
     * @param context the context
     * @return the string
     */
    public static final String toJson(final List<MaterialDetail> materials,
            final ApplicationContext context) {

        List<Map<String, Object>> materialsJson = new ArrayList<Map<String, Object>>();
        DecimalFormat df = new DecimalFormat("0.#####");

        for (MaterialDetail material : materials) {
            Map<String, Object> mJson = new LinkedHashMap<String, Object>();

            mJson.put("DT_RowId", material.getId());
            mJson.put("0", material.getName());
            mJson.put("1", getMessage(material.getMaterialType().getMessageKey(),
                    context));
            mJson.put("2", material.getUnitOfMeasure());
            mJson.put("3", material.getLifeYears());
            mJson.put("4", df.format(material.getEnergyPerUnit()));
            mJson.put("5", df.format(material.getCarbonPerUnit()));
            mJson.put("6", df.format(material.getWastagePercent()));

            materialsJson.add(mJson);
        }

        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        jsonMap.put("sEcho", 1);
        jsonMap.put("iTotalRecords", materials.size());
        jsonMap.put("iTotalDisplayRecords", materials.size());
        jsonMap.put("aaData", materialsJson);

        JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
    }

    /**
     * Sets the value for the supplied field id.
     *
     * @param fieldId the field id
     * @param value the value
     * @param context the context
     * @return the string
     * @throws BuildulatorException the buildulator exception
     */
    public String set(final int fieldId, final String value,
            final ApplicationContext context) throws BuildulatorException {
        String parsedValue = "";
        DecimalFormat df = new DecimalFormat("#.#");

        try {
            switch (fieldId) {
                case 0:
                    this.setName(value);
                    parsedValue = this.getName();
                    break;
                case 1:
                    MaterialType mMaterialType = getMaterialType(value, context);
                    this.setMaterialType(mMaterialType);
                    parsedValue = getMessage(mMaterialType.getMessageKey(), context);
                    break;
                case 2:
                    this.setUnitOfMeasure(value);
                    parsedValue = this.getUnitOfMeasure();
                    break;
                case 3:
                    this.setLifeYears(Integer.parseInt(value));
                    parsedValue = String.valueOf(this.getLifeYears());
                    break;
                case 4:
                    this.setEnergyPerUnit(Double.parseDouble(value));
                    parsedValue = df.format(this.getEnergyPerUnit());
                    break;
                case 5:
                    this.setCarbonPerUnit(Double.parseDouble(value));
                    parsedValue = df.format(this.getCarbonPerUnit());
                    break;
                case 6:
                    this.setWastagePercent(Double.parseDouble(value));
                    parsedValue = df.format(this.getWastagePercent());
                    break;
            }
        } catch (Exception e) {
            throw new BuildulatorException("Error parsing updated value");
        }

        return parsedValue;
    }

    /**
     * Find a MaterialDetail by name. If none is found null is returned.
     *
     * @param name the name
     * @return the MaterialDetail
     */
    public static MaterialDetail findByName(final String name) {

        MaterialDetail material = null;

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("The name argument is required");
        }

        TypedQuery<MaterialDetail> q = entityManager().createQuery(
                "SELECT m FROM MaterialDetail AS m WHERE LOWER(m.name) = LOWER(:name)",
                MaterialDetail.class);
        q.setParameter("name", name);

        List<MaterialDetail> materials = q.getResultList();

        if (materials != null && materials.size() > 0) {
            material = materials.get(0);
        }
        return material;
    }

    /**
     * Find all of the MaterialDetails in alphabetical order.
     *
     * @return the list
     */
    public static List<MaterialDetail> findAllMaterialDetails() {

        TypedQuery<MaterialDetail> q = entityManager().createQuery(
                "SELECT m FROM MaterialDetail AS m ORDER BY m.name",
                MaterialDetail.class);

        return q.getResultList();
    }

    /**
     * Find all of the MaterialDetails of a certain type in alphabetical order.
     *
     * @return the list
     */
    public static List<MaterialDetail> findMaterialDetails(MaterialType mt) {

        TypedQuery<MaterialDetail> q = entityManager().createQuery(
                "SELECT m FROM MaterialDetail AS m WHERE m.materialType = :materialType"
                + " ORDER BY m.name",
                MaterialDetail.class);
        q.setParameter("materialType", mt);

        return q.getResultList();
    }

    /**
     * Gets the material type.
     *
     * @param value the value
     * @param context the context
     * @return the user status
     */
    public static MaterialType getMaterialType(final String value,
            final ApplicationContext context) {
        MaterialType mMaterialType = MaterialType.CONSTRUCTION;

        HashMap<String, MaterialType> materialTypes = new HashMap<String, MaterialType>();

        for (MaterialType mt : MaterialType.values()) {
            materialTypes.put(getMessage(mt.getMessageKey(), context), mt);
        }
        if (materialTypes.containsKey(value)) {
            mMaterialType = materialTypes.get(value);
        }
        return mMaterialType;
    }

    /**
     * Gets the message.
     *
     * @param key the key
     * @param context the context
     * @return the message
     */
    private static String getMessage(final String key, final ApplicationContext context) {
        return context.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * Builds the calculation string.
     *
     * @param quantity the quantity
     * @param coefficient the coefficient
     * @return the string
     */
    private String buildCalc(final double quantity, final double coefficient) {

        String calc = this.getMaterialType().getCalculation();

        calc = StringUtils.replace(calc, "${quantity}", String.valueOf(quantity));
        calc = StringUtils.replace(calc, "${coefficient}", String.valueOf(coefficient));
        calc = StringUtils.replace(calc, "${wastage}", String.valueOf(
                this.getWastagePercent()));
        calc = StringUtils.replace(calc, "${lifespan}", String.valueOf(
                this.getLifeYears()));

        return calc;
    }

}