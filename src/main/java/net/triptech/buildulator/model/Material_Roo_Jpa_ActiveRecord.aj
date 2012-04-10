// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.triptech.buildulator.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.triptech.buildulator.model.Material;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Material_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Material.entityManager;
    
    public static final EntityManager Material.entityManager() {
        EntityManager em = new Material().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Material.countMaterials() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Material o", Long.class).getSingleResult();
    }
    
    public static List<Material> Material.findAllMaterials() {
        return entityManager().createQuery("SELECT o FROM Material o", Material.class).getResultList();
    }
    
    public static Material Material.findMaterial(Long id) {
        if (id == null) return null;
        return entityManager().find(Material.class, id);
    }
    
    public static List<Material> Material.findMaterialEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Material o", Material.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Material.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Material.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Material attached = Material.findMaterial(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Material.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Material.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Material Material.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Material merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
