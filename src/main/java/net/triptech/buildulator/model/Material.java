package net.triptech.buildulator.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.sf.json.JSONObject;
import net.triptech.buildulator.BuildulatorException;

import org.apache.commons.lang.StringUtils;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

/**
 * The Class Material.
 */
@RooJavaBean
@RooJpaActiveRecord
public class Material {

    /** The name of the material. */
    @NotNull
    @Size(min = 1, max = 100)
    @Column(unique = true)
    private String name;

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
     * Covert the materials list to JSON.
     *
     * @param materials the materials
     * @return the string
     */
    public static final String toJson(final List<Material> materials) {

        List<Map<String, Object>> materialsJson = new ArrayList<Map<String, Object>>();
        DecimalFormat df = new DecimalFormat("#.#");

        for (Material material : materials) {
            Map<String, Object> mJson = new LinkedHashMap<String, Object>();

            mJson.put("DT_RowId", material.getId());
            mJson.put("0", material.getName());
            mJson.put("1", material.getUnitOfMeasure());
            mJson.put("2", material.getLifeYears());
            mJson.put("3", df.format(material.getCarbonPerUnit()));
            mJson.put("4", df.format(material.getEnergyPerUnit()));
            mJson.put("5", df.format(material.getWastagePercent()));

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
     * @return the string
     * @throws BuildulatorException the buildulator exception
     */
    public String set(final int fieldId, final String value) throws BuildulatorException {
        String parsedValue = "";
        DecimalFormat df = new DecimalFormat("#.#");

        try {
            switch (fieldId) {
                case 0:
                    this.setName(value);
                    parsedValue = this.getName();
                    break;
                case 1:
                    this.setUnitOfMeasure(value);
                    parsedValue = this.getUnitOfMeasure();
                    break;
                case 2:
                    this.setLifeYears(Integer.parseInt(value));
                    parsedValue = String.valueOf(this.getLifeYears());
                    break;
                case 3:
                    this.setCarbonPerUnit(Double.parseDouble(value));
                    parsedValue = df.format(this.getCarbonPerUnit());
                    break;
                case 4:
                    this.setEnergyPerUnit(Double.parseDouble(value));
                    parsedValue = df.format(this.getEnergyPerUnit());
                    break;
                case 5:
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
     * Find a material by name. If none is found null is returned.
     *
     * @param name the name
     * @return the material
     */
    public static Material findByName(final String name) {

        Material material = null;

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("The name argument is required");
        }

        TypedQuery<Material> q = entityManager().createQuery(
                "SELECT m FROM Material AS m WHERE LOWER(m.name) = LOWER(:name)",
                Material.class);
        q.setParameter("name", name);

        List<Material> materials = q.getResultList();

        if (materials != null && materials.size() > 0) {
            material = materials.get(0);
        }
        return material;
    }

    /**
     * Find all of the materials in alphabetical order.
     *
     * @return the list
     */
    public static List<Material> findAllMaterials() {

        TypedQuery<Material> q = entityManager().createQuery(
                "SELECT m FROM Material AS m ORDER BY m.name",
                Material.class);

        return q.getResultList();
    }
}
