package net.triptech.buildulator.model.bom;

import java.text.DecimalFormat;

/**
 * The Class Format.
 */
public class Format {

    /**
     * Formats the value to 1 decimal place.
     *
     * @param value the value
     * @return the formatted quantity
     */
    public static final String to1DP(final double value) {
        String stringValue = "";

        if (value != 0) {
            DecimalFormat df = new DecimalFormat("0.0");
            stringValue = df.format(value);
        }
        return stringValue;
    }

    /**
     * Formats the value to 2 decimal places.
     *
     * @param value the value
     * @return the formatted quantity
     */
    public static final String to2DP(final double value) {
        String stringValue = "";

        if (value != 0) {
            DecimalFormat df = new DecimalFormat("0.00");
            stringValue = df.format(value);
        }
        return stringValue;
    }
}
