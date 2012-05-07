package net.triptech.buildulator.model.bom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class Element.
 */
@RooJavaBean
public class Element extends SustainabilityBase {

    /** The materials. */
    private List<Material> materials = new ArrayList<Material>();


    /**
     * Adds the material.
     *
     * @param material the material
     */
    public final void addMaterial(final Material material) {
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
