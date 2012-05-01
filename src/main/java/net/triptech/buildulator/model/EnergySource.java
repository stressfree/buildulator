package net.triptech.buildulator.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import net.sf.json.JSONObject;
import net.triptech.buildulator.BuildulatorException;

import org.apache.commons.lang.StringUtils;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * The Class EnergySource.
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class EnergySource {

    /** The name. */
    private String name;

    /** The energy per unit. */
    private double energyPerUnit;

    /** The carbon per unit. */
    private double carbonPerUnit;


    /**
     * Covert the energy sources list to JSON.
     *
     * @param materials the materials
     * @return the string
     */
    public static final String toJson(final List<EnergySource> energySources) {

        List<Map<String, Object>> sourcesJson = new ArrayList<Map<String, Object>>();
        DecimalFormat df = new DecimalFormat("#.#");

        for (EnergySource energySource : energySources) {
            Map<String, Object> esJson = new LinkedHashMap<String, Object>();

            esJson.put("DT_RowId", energySource.getId());
            esJson.put("0", energySource.getName());
            esJson.put("1", df.format(energySource.getCarbonPerUnit()));
            esJson.put("2", df.format(energySource.getEnergyPerUnit()));

            sourcesJson.add(esJson);
        }

        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        jsonMap.put("sEcho", 1);
        jsonMap.put("iTotalRecords", energySources.size());
        jsonMap.put("iTotalDisplayRecords", energySources.size());
        jsonMap.put("aaData", sourcesJson);

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
                    this.setCarbonPerUnit(Double.parseDouble(value));
                    parsedValue = df.format(this.getCarbonPerUnit());
                    break;
                case 2:
                    this.setEnergyPerUnit(Double.parseDouble(value));
                    parsedValue = df.format(this.getEnergyPerUnit());
                    break;
            }
        } catch (Exception e) {
            throw new BuildulatorException("Error parsing updated value");
        }

        return parsedValue;
    }

    /**
     * Find an energy source by name. If none is found null is returned.
     *
     * @param name the name
     * @return the energy source
     */
    public static EnergySource findByName(final String name) {

        EnergySource energySource = null;

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("The name argument is required");
        }

        TypedQuery<EnergySource> q = entityManager().createQuery(
                "SELECT es FROM EnergySource AS es WHERE LOWER(es.name) = LOWER(:name)",
                EnergySource.class);
        q.setParameter("name", name);

        List<EnergySource> energySources = q.getResultList();

        if (energySources != null && energySources.size() > 0) {
            energySource = energySources.get(0);
        }
        return energySource;
    }

    /**
     * Find all of the energy sources in alphabetical order.
     *
     * @return the list
     */
    public static List<EnergySource> findAllEnergySources() {

        TypedQuery<EnergySource> q = entityManager().createQuery(
                "SELECT es FROM EnergySource AS es ORDER BY es.name",
                EnergySource.class);

        return q.getResultList();
    }

}
