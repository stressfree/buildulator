package net.triptech.buildulator.model.bom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;

/**
 * The Class Section.
 */
@RooJavaBean
public class Section extends QuantityBase {

    /** The elements. */
    private List<Element> elements = new ArrayList<Element>();

    /**
     * Adds the element.
     *
     * @param element the element
     */
    public final void addElement(final Element element) {
        this.getElements().add(element);
    }

    /**
     * Removes the element.
     *
     * @param id the id
     */
    public final void removeElement(final int id) {
        if (this.getElements().size() >= id) {
            this.getElements().remove(id);
        }
    }

}
