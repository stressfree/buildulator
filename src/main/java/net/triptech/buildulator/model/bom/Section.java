package net.triptech.buildulator.model.bom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class Section.
 */
@RooJavaBean
public class Section extends SustainabilityBase {

    /** The assemblies. */
    private List<AssemblyQuantity> assemblies = new ArrayList<AssemblyQuantity>();

    /**
     * Adds the assembly.
     *
     * @param assembly the assembly
     */
    public final void addAssembly(final AssemblyQuantity assembly) {
        assembly.setId(this.getAssemblies().size());
        this.getAssemblies().add(assembly);
    }

    /**
     * Removes the assembly.
     *
     * @param id the id
     */
    public final void removeAssembly(final int id) {
        if (this.getAssemblies().size() >= id) {
            this.getAssemblies().remove(id);
        }
    }

}
