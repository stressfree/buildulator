// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.triptech.buildulator.model;

import net.triptech.buildulator.model.Project;

privileged aspect Project_Roo_JavaBean {
    
    public String Project.getName() {
        return this.name;
    }
    
    public void Project.setName(String name) {
        this.name = name;
    }
    
    public String Project.getLocation() {
        return this.location;
    }
    
    public void Project.setLocation(String location) {
        this.location = location;
    }
    
    public int Project.getOccupants() {
        return this.occupants;
    }
    
    public void Project.setOccupants(int occupants) {
        this.occupants = occupants;
    }
    
    public double Project.getEnergyConsumption() {
        return this.energyConsumption;
    }
    
    public void Project.setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }
    
    public String Project.getDescription() {
        return this.description;
    }
    
    public void Project.setDescription(String description) {
        this.description = description;
    }
    
}