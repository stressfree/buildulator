package net.triptech.buildulator.model.bom;

import java.text.DecimalFormat;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class QuantityBase.
 */
@RooJavaBean
public abstract class QuantityBase extends BillOfMaterialsBase {

    /** The quantity. */
    private double quantity;

    /** The units. */
    private String units;


    /**
     * Gets the formatted quantity.
     *
     * @return the formatted quantity
     */
    public final String getFormattedQuantity() {
        return formatValue(this.quantity);
    }

    /**
     * Format the double to a string.
     *
     * @param value the value
     * @return the string
     */
    protected String formatValue(final double value) {
        String stringValue = "";

        if (value != 0) {
            DecimalFormat df = new DecimalFormat("#.0");
            stringValue = df.format(value);
        }
        return stringValue;
    }

}
