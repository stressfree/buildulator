package net.triptech.buildulator.model.bom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class AssemblyQuantity.
 */
@RooJavaBean
public class AssemblyQuantity extends SustainabilityBase {

    /** The materials. */
    private List<MaterialQuantity> materials = new ArrayList<MaterialQuantity>();


    /**
     * Adds the material.
     *
     * @param material the material
     */
    public final void addMaterial(final MaterialQuantity material) {
        material.setId(this.getMaterials().size());
        this.getMaterials().add(material);
    }

    /**
     * Removes the material.
     *
     * @param id the id
     */
    public final void removeMaterial(final int id) {
        if (this.getMaterials().size() >= id) {
            this.getMaterials().remove(id);
        }
    }

}
