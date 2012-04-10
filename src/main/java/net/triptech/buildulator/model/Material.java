package net.triptech.buildulator.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.sf.json.JSONObject;

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
	@Size(min = 1)
	@Column(unique = true)
	private String name;

	/** The unit of measure. */
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

		List<List<String>> materialsJson = new ArrayList<List<String>>();

        for (Material material : materials) {
        	List<String> materialJson = new ArrayList<String>();

        	materialJson.add(material.getName());
        	materialJson.add(material.getUnitOfMeasure());
        	materialJson.add(String.valueOf(material.getLifeYears()));
        	materialJson.add(String.valueOf(material.getCarbonPerUnit()));
        	materialJson.add(String.valueOf(material.getEnergyPerUnit()));
        	materialJson.add(String.valueOf(material.getWastagePercent()));

        	materialsJson.add(materialJson);
        }

    	Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
    	jsonMap.put("sEcho", 1);
    	jsonMap.put("iTotalRecords", materials.size());
    	jsonMap.put("iTotalDisplayRecords", materials.size());
    	jsonMap.put("aaData", materialsJson);

    	JSONObject jsonObject = JSONObject.fromObject(jsonMap);

        return jsonObject.toString();
	}
}
