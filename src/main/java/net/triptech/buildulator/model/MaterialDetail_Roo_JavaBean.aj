// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.triptech.buildulator.model;

import net.triptech.buildulator.model.MaterialDetail;
import net.triptech.buildulator.model.MaterialType;

privileged aspect MaterialDetail_Roo_JavaBean {
    
    public String MaterialDetail.getName() {
        return this.name;
    }
    
    public String MaterialDetail.getPreviousName() {
        return this.previousName;
    }
    
    public void MaterialDetail.setPreviousName(String previousName) {
        this.previousName = previousName;
    }
    
    public MaterialType MaterialDetail.getMaterialType() {
        return this.materialType;
    }
    
    public void MaterialDetail.setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }
    
    public String MaterialDetail.getUnitOfMeasure() {
        return this.unitOfMeasure;
    }
    
    public void MaterialDetail.setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
    
    public double MaterialDetail.getEnergyPerUnit() {
        return this.energyPerUnit;
    }
    
    public void MaterialDetail.setEnergyPerUnit(double energyPerUnit) {
        this.energyPerUnit = energyPerUnit;
    }
    
    public double MaterialDetail.getCarbonPerUnit() {
        return this.carbonPerUnit;
    }
    
    public void MaterialDetail.setCarbonPerUnit(double carbonPerUnit) {
        this.carbonPerUnit = carbonPerUnit;
    }
    
    public int MaterialDetail.getLifeYears() {
        return this.lifeYears;
    }
    
    public void MaterialDetail.setLifeYears(int lifeYears) {
        this.lifeYears = lifeYears;
    }
    
    public double MaterialDetail.getWastagePercent() {
        return this.wastagePercent;
    }
    
    public void MaterialDetail.setWastagePercent(double wastagePercent) {
        this.wastagePercent = wastagePercent;
    }
    
}
