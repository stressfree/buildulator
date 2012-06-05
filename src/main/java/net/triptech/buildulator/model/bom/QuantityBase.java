package net.triptech.buildulator.model.bom;

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
        return Format.to1DP(this.quantity);
    }
}
