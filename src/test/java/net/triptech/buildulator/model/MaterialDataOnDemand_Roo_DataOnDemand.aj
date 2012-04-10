// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.triptech.buildulator.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import net.triptech.buildulator.model.Material;
import net.triptech.buildulator.model.MaterialDataOnDemand;
import org.springframework.stereotype.Component;

privileged aspect MaterialDataOnDemand_Roo_DataOnDemand {
    
    declare @type: MaterialDataOnDemand: @Component;
    
    private Random MaterialDataOnDemand.rnd = new SecureRandom();
    
    private List<Material> MaterialDataOnDemand.data;
    
    public Material MaterialDataOnDemand.getNewTransientMaterial(int index) {
        Material obj = new Material();
        setCarbonPerUnit(obj, index);
        setEnergyPerUnit(obj, index);
        setLifeYears(obj, index);
        setName(obj, index);
        setUnitOfMeasure(obj, index);
        setWastagePercent(obj, index);
        return obj;
    }
    
    public void MaterialDataOnDemand.setCarbonPerUnit(Material obj, int index) {
        double carbonPerUnit = new Integer(index).doubleValue();
        obj.setCarbonPerUnit(carbonPerUnit);
    }
    
    public void MaterialDataOnDemand.setEnergyPerUnit(Material obj, int index) {
        double energyPerUnit = new Integer(index).doubleValue();
        obj.setEnergyPerUnit(energyPerUnit);
    }
    
    public void MaterialDataOnDemand.setLifeYears(Material obj, int index) {
        int lifeYears = index;
        obj.setLifeYears(lifeYears);
    }
    
    public void MaterialDataOnDemand.setName(Material obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void MaterialDataOnDemand.setUnitOfMeasure(Material obj, int index) {
        String unitOfMeasure = "unitOfMeasure_" + index;
        obj.setUnitOfMeasure(unitOfMeasure);
    }
    
    public void MaterialDataOnDemand.setWastagePercent(Material obj, int index) {
        double wastagePercent = new Integer(index).doubleValue();
        obj.setWastagePercent(wastagePercent);
    }
    
    public Material MaterialDataOnDemand.getSpecificMaterial(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Material obj = data.get(index);
        Long id = obj.getId();
        return Material.findMaterial(id);
    }
    
    public Material MaterialDataOnDemand.getRandomMaterial() {
        init();
        Material obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Material.findMaterial(id);
    }
    
    public boolean MaterialDataOnDemand.modifyMaterial(Material obj) {
        return false;
    }
    
    public void MaterialDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Material.findMaterialEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Material' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Material>();
        for (int i = 0; i < 10; i++) {
            Material obj = getNewTransientMaterial(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
