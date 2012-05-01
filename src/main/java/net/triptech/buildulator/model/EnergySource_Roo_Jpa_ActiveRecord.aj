// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.triptech.buildulator.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.triptech.buildulator.model.EnergySource;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EnergySource_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager EnergySource.entityManager;
    
    public static final EntityManager EnergySource.entityManager() {
        EntityManager em = new EnergySource().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EnergySource.countEnergySources() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EnergySource o", Long.class).getSingleResult();
    }
    
    public static EnergySource EnergySource.findEnergySource(Long id) {
        if (id == null) return null;
        return entityManager().find(EnergySource.class, id);
    }
    
    public static List<EnergySource> EnergySource.findEnergySourceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EnergySource o", EnergySource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EnergySource.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EnergySource.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EnergySource attached = EnergySource.findEnergySource(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EnergySource.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EnergySource.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EnergySource EnergySource.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EnergySource merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
