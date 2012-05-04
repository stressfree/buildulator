package net.triptech.buildulator.model.bom;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class SustainabilityBase.
 */
@RooJavaBean
public abstract class SustainabilityBase extends QuantityBase {

    /** The total energy. */
    private double totalEnergy;

    /** The total carbon. */
    private double totalCarbon;


    /**
     * Gets the formatted total energy.
     *
     * @return the formatted total energy
     */
    public final String getFormattedTotalEnergy() {
        return formatValue(this.totalEnergy);
    }

    /**
     * Gets the formatted total carbon.
     *
     * @return the formatted total carbon
     */
    public final String getFormattedTotalCarbon() {
        return formatValue(this.totalCarbon);
    }

}
