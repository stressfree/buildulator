package net.triptech.buildulator.model.bom;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class BillOfMaterials.
 */
@RooJavaBean
public class BillOfMaterials {

    /** The logger. */
    private static Logger logger = Logger.getLogger(BillOfMaterials.class);

    /** The sections. */
    private List<Section> sections = new ArrayList<Section>();


    /**
     * Adds the section.
     *
     * @param section the section
     */
    public final void addSection(final Section section) {
        section.setId(this.getSections().size());
        this.getSections().add(section);
    }

    /**
     * Removes the section.
     *
     * @param id the id
     */
    public final void removeSection(final int id) {
        if (this.getSections().size() >= id) {
            this.getSections().remove(id);
        }
    }


    /**
     * Covert the bill of materials object to a JSON string.
     *
     * @return the JSON string
     */
    public final String toJson() {

        List<Map<String, Object>> ssJson = new ArrayList<Map<String, Object>>();

        for (Section section : this.getSections()) {

            List<Map<String, Object>> esJson = new ArrayList<Map<String, Object>>();

            for (Element element : section.getElements()) {

                List<Map<String, Object>> msJson = new ArrayList<Map<String, Object>>();

                for (Material material : element.getMaterials()) {

                    Map<String, Object> mJson = new LinkedHashMap<String, Object>();

                    mJson.put("id", material.getId());
                    mJson.put("name", material.getName());
                    mJson.put("units", material.getUnits());
                    mJson.put("quantity", material.getFormattedQuantity());
                    mJson.put("totalEnergy", material.getFormattedTotalEnergy());
                    mJson.put("totalCarbon", material.getFormattedTotalCarbon());

                    msJson.add(mJson);
                }

                Map<String, Object> eJson = new LinkedHashMap<String, Object>();

                eJson.put("id", element.getId());
                eJson.put("name", element.getName());
                eJson.put("units", element.getUnits());
                eJson.put("quantity", element.getFormattedQuantity());
                eJson.put("totalEnergy", element.getFormattedTotalEnergy());
                eJson.put("totalCarbon", element.getFormattedTotalCarbon());
                eJson.put("materials", msJson);

                esJson.add(eJson);
            }

            Map<String, Object> sJson = new LinkedHashMap<String, Object>();

            sJson.put("id", section.getId());
            sJson.put("name", section.getName());
            sJson.put("totalEnergy", section.getFormattedTotalEnergy());
            sJson.put("totalCarbon", section.getFormattedTotalCarbon());
            sJson.put("elements", esJson);

            ssJson.add(sJson);
        }

        Map<String, Object> bomJson = new LinkedHashMap<String, Object>();
        bomJson.put("sections", ssJson);

        JSONObject jsonObject = JSONObject.fromObject(bomJson);

        return jsonObject.toString();
    }

    /**
     * Parses the json string which represents the bill of materials.
     *
     * @param jsonString the json bill of materials string
     * @return the bill of materials
     */
    public final static BillOfMaterials parseJson(final String jsonString) {

        BillOfMaterials bom = new BillOfMaterials();

        if (StringUtils.isNotBlank(jsonString)) {
            try {
                JSONObject bomJson = (JSONObject) JSONSerializer.toJSON(jsonString);
                if (bomJson != null) {
                    bom = parseBOMJson(bomJson);
                }
            } catch (JSONException je) {
                logger.info("Error parsing bill of materials JSON: " + je.getMessage());
            } catch (ClassCastException ce) {
                logger.info("Error casting result to a JSONObject: " + ce.getMessage());
            }
        }
        return bom;
    }

    /**
     * Parses the bill of materials JSON.
     *
     * @param bomJson the bill of materials JSON
     * @return the bill of materials
     */
    private static BillOfMaterials parseBOMJson(final JSONObject bomJson) {

        BillOfMaterials bom = new BillOfMaterials();

        JSONArray sectionsJson = getArray(bomJson, "sections");

        for (int s = 0; s < sectionsJson.size(); s++) {
            JSONObject sectionJson = sectionsJson.getJSONObject(s);

            Section section = new Section();
            section.setId(s);
            section.setName(getString(sectionJson, "name"));

            JSONArray elementsJson = getArray(sectionJson, "elements");

            for (int a = 0; a < elementsJson.size(); a++) {
                JSONObject elementJson = elementsJson.getJSONObject(a);

                Element element = new Element();
                element.setId(a);
                element.setName(getString(elementJson, "name"));
                element.setUnits(getString(elementJson, "units"));
                element.setQuantity(getDouble(elementJson, "quantity"));
                element.setTotalEnergy(getDouble(elementJson, "totalEnergy"));
                element.setTotalCarbon(getDouble(elementJson, "totalCarbon"));

                JSONArray materialsJson = getArray(elementJson, "materials");

                for (int m = 0; m < materialsJson.size(); m++) {
                    JSONObject materialJson = materialsJson.getJSONObject(m);

                    Material material = new Material();
                    material.setId(m);
                    material.setName(getString(materialJson, "name"));
                    material.setUnits(getString(materialJson, "units"));
                    material.setQuantity(getDouble(materialJson, "quantity"));
                    material.setTotalEnergy(getDouble(materialJson, "totalEnergy"));
                    material.setTotalCarbon(getDouble(materialJson, "totalCarbon"));

                    element.addMaterial(material);
                }
                section.addElement(element);
            }
            bom.addSection(section);
        }
        return bom;
    }

    /**
     * Gets the JSON array.
     *
     * @param obj the obj
     * @param key the key
     * @return the JSON array
     */
    private static JSONArray getArray(final JSONObject obj, final String key) {

        JSONArray array = new JSONArray();

        if (obj != null) {
            if (obj.containsKey(key)) {
                try {
                    array = obj.getJSONArray(key);
                } catch (JSONException je) {
                    logger.debug("Error casting to a JSONArray: " + je.getMessage());
                }
            }
        }
        return array;
    }

    /**
     * Gets the string from the JSON object.
     *
     * @param obj the obj
     * @param key the key
     * @return the jSON array
     */
    private static String getString(final JSONObject obj, final String key) {

        String value = "";

        if (obj != null) {
            if (obj.containsKey(key)) {
                try {
                    value = obj.getString(key);
                } catch (JSONException je) {
                    logger.debug("Error casting to a string: " + je.getMessage());
                }
            }
        }
        return value;
    }

    /**
     * Gets the double from the JSON object.
     *
     * @param obj the obj
     * @param key the key
     * @return the jSON array
     */
    private static double getDouble(final JSONObject obj, final String key) {

        double value = 0;

        if (obj != null) {
            if (obj.containsKey(key)) {
                try {
                    value = obj.getDouble(key);
                } catch (JSONException je) {
                    logger.debug("Error casting to a double: " + je.getMessage());
                }
            }
        }
        return value;
    }
}
