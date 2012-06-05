package net.triptech.buildulator.model.bom;

import java.text.DecimalFormat;
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
 * The Class SustainabilitySummary.
 */
@RooJavaBean
public class SustainabilitySummary {

    /** The logger. */
    private static Logger logger = Logger.getLogger(SustainabilitySummary.class);

    /** The name. */
    private String name;

    /** The detailed operational elements. */
    private int elOperationalDetailed;

    /** The total operational elements. */
    private int elOperationalTotal;

    /** The total construction elements. */
    private int elConstructionDetailed;

    /** The total construction elements. */
    private int elConstructionTotal;

    /** The occupants. */
    private double occupants;

    /** The operational energy. */
    private double energyOperational;

    /** The construction energy. */
    private double energyConstruction;

    /** The operational carbon. */
    private double carbonOperational;

    /** The construction carbon. */
    private double carbonConstruction;

    /** The total energy change. */
    private List<Double> totalEnergyChange = new ArrayList<Double>();

    /** The total carbon change. */
    private List<Double> totalCarbonChange = new ArrayList<Double>();

    /** The per occupant energy change. */
    private List<Double> perOccupantEnergyChange = new ArrayList<Double>();

    /** The per occupant carbon change. */
    private List<Double> perOccupantCarbonChange = new ArrayList<Double>();


    /**
     * Gets the percent complete.
     *
     * @return the percent complete
     */
    public final String getPercentComplete() {

        String value = "0";

        double operational = 0, cstruct = 0;

        if (this.getElOperationalTotal() > 0) {
            operational = (double) this.getElOperationalDetailed() /
                    (double) this.getElOperationalTotal();
        }

        if (this.getElConstructionTotal() > 0) {
            cstruct = (double) this.getElConstructionDetailed() /
                    (double) this.getElConstructionTotal();
        }

        double percentage = ((operational / 2) + (cstruct / 2)) * 100;

        DecimalFormat df = new DecimalFormat("#");
        value = df.format(percentage);

        return value + "%";
    }

    /**
     * Gets the energy total.
     *
     * @return the energy total
     */
    public final double getEnergyTotal() {
        return this.getEnergyOperational() + this.getEnergyConstruction();
    }

    /**
     * Gets the energy per occupant.
     *
     * @return the energy per occupant
     */
    public final double getEnergyPerOccupant() {
        double value = 0;
        if (this.getOccupants() > 0) {
            value = this.getEnergyTotal() / this.getOccupants();
        }
        return value;
    }

    /**
     * Gets the operational energy per occupant.
     *
     * @return the operational energy per occupant
     */
    public final double getEnergyPerOccupantOperational() {
        double value = 0;
        if (this.getOccupants() > 0) {
            value = this.getEnergyOperational() / this.getOccupants();
        }
        return value;
    }

    /**
     * Gets the construction energy per occupant.
     *
     * @return the construction energy per occupant
     */
    public final double getEnergyPerOccupantConstruction() {
        double value = 0;
        if (this.getOccupants() > 0) {
            value = this.getEnergyConstruction() / this.getOccupants();
        }
        return value;
    }

    /**
     * Gets the carbon total.
     *
     * @return the carbon total
     */
    public final double getCarbonTotal() {
        return this.getCarbonOperational() + this.getCarbonConstruction();
    }

    /**
     * Gets the carbon per occupant.
     *
     * @return the carbon per occupant
     */
    public final double getCarbonPerOccupant() {
        double value = 0;
        if (this.getOccupants() > 0) {
            value = (this.getCarbonTotal() / this.getOccupants());
        }
        return value;
    }

    /**
     * Gets the operational carbon per occupant.
     *
     * @return the operational carbon per occupant
     */
    public final double getCarbonPerOccupantOperational() {
        double value = 0;
        if (this.getOccupants() > 0) {
            value = (this.getCarbonOperational() / this.getOccupants());
        }
        return value;
    }

    /**
     * Gets the construction carbon per occupant.
     *
     * @return the construction carbon per occupant
     */
    public final double getCarbonPerOccupantConstruction() {
        double value = 0;
        if (this.getOccupants() > 0) {
            value = (this.getCarbonConstruction() / this.getOccupants());
        }
        return value;
    }

    /**
     * Covert the sustainability summary object to a JSON string.
     *
     * @return the JSON string
     */
    public final String toJson() {

        Map<String, Object> ssJson = new LinkedHashMap<String, Object>();

        ssJson.put("name", this.getName());

        ssJson.put("occupants", formatValue(this.getOccupants()));

        ssJson.put("percentComplete", this.getPercentComplete());
        ssJson.put("elOperationalDetailed", this.getElOperationalDetailed());
        ssJson.put("elOperationalTotal", this.getElOperationalTotal());
        ssJson.put("elConstructionDetailed", this.getElConstructionDetailed());
        ssJson.put("elConstructionTotal", this.getElConstructionTotal());

        ssJson.put("energyOperational", formatValue(this.getEnergyOperational()));
        ssJson.put("energyConstruction", formatValue(this.getEnergyConstruction()));
        ssJson.put("energyTotal", formatValue(this.getEnergyTotal()));
        ssJson.put("energyPerOccupant", formatValue(this.getEnergyPerOccupant()));
        ssJson.put("energyPerOccupantOperational",
                formatValue(this.getEnergyPerOccupantOperational()));
        ssJson.put("energyPerOccupantConstruction",
                formatValue(this.getEnergyPerOccupantConstruction()));

        ssJson.put("carbonOperational", formatValue(this.getCarbonOperational()));
        ssJson.put("carbonConstruction", formatValue(this.getCarbonConstruction()));
        ssJson.put("carbonTotal", formatValue(this.getCarbonTotal()));
        ssJson.put("carbonPerOccupant", formatValue(this.getCarbonPerOccupant()));
        ssJson.put("carbonPerOccupantOperational",
                formatValue(this.getCarbonPerOccupantOperational()));
        ssJson.put("carbonPerOccupantConstruction",
                formatValue(this.getCarbonPerOccupantConstruction()));

        ssJson.put("totalEnergyChange", getChangeArray(this.getTotalEnergyChange()));
        ssJson.put("totalCarbonChange", getChangeArray(this.getTotalCarbonChange()));

        ssJson.put("perOccupantEnergyChange", getChangeArray(
                this.getPerOccupantEnergyChange()));
        ssJson.put("perOccupantCarbonChange", getChangeArray(
                this.getPerOccupantCarbonChange()));

        JSONObject jsonObject = JSONObject.fromObject(ssJson);

        return jsonObject.toString();
    }

    /**
     * Parses the json string which represents the sustainability summary.
     *
     * @param jsonString the json sustainability summary string
     * @return the sustainability summary
     */
    public final static SustainabilitySummary parseJson(final String jsonString) {

        SustainabilitySummary ss = new SustainabilitySummary();

        if (StringUtils.isNotBlank(jsonString)) {
            try {
                JSONObject ssJson = (JSONObject) JSONSerializer.toJSON(jsonString);
                if (ssJson != null) {
                    ss = parseSSJson(ssJson);
                }
            } catch (JSONException je) {
                logger.info("Error parsing summary JSON: " + je.getMessage());
            } catch (ClassCastException ce) {
                logger.info("Error casting result to a JSONObject: " + ce.getMessage());
            }
        }
        return ss;
    }

    /**
     * Parses the sustainability summary json.
     *
     * @param ssJson the ss json
     * @return the sustainability summary
     */
    private static SustainabilitySummary parseSSJson(final JSONObject ssJson) {

        SustainabilitySummary ss = new SustainabilitySummary();

        ss.setName(getString(ssJson, "name"));

        ss.setElOperationalDetailed(getInt(ssJson, "elOperationalDetailed"));
        ss.setElOperationalTotal(getInt(ssJson, "elOperationalTotal"));

        ss.setElConstructionDetailed(getInt(ssJson, "elConstructionDetailed"));
        ss.setElConstructionTotal(getInt(ssJson, "elConstructionTotal"));

        ss.setOccupants(getDouble(ssJson, "occupants"));

        ss.setEnergyOperational(getDouble(ssJson, "energyOperational"));
        ss.setEnergyConstruction(getDouble(ssJson, "energyConstruction"));

        ss.setCarbonOperational(getDouble(ssJson, "carbonOperational"));
        ss.setCarbonConstruction(getDouble(ssJson, "carbonConstruction"));

        ss.setTotalEnergyChange(getDoubleArray(ssJson, "totalEnergyChange"));
        ss.setTotalCarbonChange(getDoubleArray(ssJson, "totalCarbonChange"));

        ss.setPerOccupantEnergyChange(getDoubleArray(ssJson, "perOccupantEnergyChange"));
        ss.setPerOccupantCarbonChange(getDoubleArray(ssJson, "perOccupantCarbonChange"));

        return ss;
    }

    /**
     * Gets the last 8 values from the change array.
     *
     * @param array the array
     * @return the change array
     */
    private List<Double[]> getChangeArray(final List<Double> array) {

        List<Double[]> changeArray = new ArrayList<Double[]>();

        if (array != null) {
            int index = 0;
            if (array.size() > 8) {
                index = array.size() - 8;
            }

            double key = 0;

            int extraRows = 8 - array.size();


            while (index < array.size()) {

                double value = array.get(index);

                while (extraRows > 0) {
                    changeArray.add(new Double[] { key, value });
                    extraRows--;
                    key++;
                }

                changeArray.add(new Double[] { key, value });
                index++;
                key++;
            }
        }
        return changeArray;
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
     * Gets the formatted quantity.
     *
     * @param value the value
     * @return the formatted quantity
     */
    private final String formatValue(final double value) {
        String stringValue = "";

        if (value != 0) {
            DecimalFormat df = new DecimalFormat("#.0");
            stringValue = df.format(value);
        }
        return stringValue;
    }

    /**
     * Gets the int from the JSON object.
     *
     * @param obj the obj
     * @param key the key
     * @return the jSON array
     */
    private static int getInt(final JSONObject obj, final String key) {

        int value = 0;

        if (obj != null) {
            if (obj.containsKey(key)) {
                try {
                    value = obj.getInt(key);
                } catch (JSONException je) {
                    logger.debug("Error casting to an int: " + je.getMessage());
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

    /**
     * Gets the double array.
     *
     * @param obj the obj
     * @param key the key
     * @return the double array
     */
    private static List<Double> getDoubleArray(final JSONObject obj, final String key) {

        List<Double> array = new ArrayList<Double>();

        if (obj != null) {
            if (obj.containsKey(key)) {
                try {
                    JSONArray jsonArray = obj.getJSONArray(key);

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONArray doubleArray = jsonArray.getJSONArray(i);

                        array.add(doubleArray.getDouble(1));
                    }
                } catch (JSONException je) {
                    logger.debug("Error casting to a JSONArray: " + je.getMessage());
                }
            }
        }
        return array;
    }
}
