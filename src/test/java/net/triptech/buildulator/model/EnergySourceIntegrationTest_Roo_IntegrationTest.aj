// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.triptech.buildulator.model;

import java.util.List;
import net.triptech.buildulator.model.EnergySource;
import net.triptech.buildulator.model.EnergySourceDataOnDemand;
import net.triptech.buildulator.model.EnergySourceIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EnergySourceIntegrationTest_Roo_IntegrationTest {
    
    declare @type: EnergySourceIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: EnergySourceIntegrationTest: @Transactional;
    
    @Autowired
    private EnergySourceDataOnDemand EnergySourceIntegrationTest.dod;
    
    @Test
    public void EnergySourceIntegrationTest.testCountEnergySources() {
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", dod.getRandomEnergySource());
        long count = EnergySource.countEnergySources();
        Assert.assertTrue("Counter for 'EnergySource' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void EnergySourceIntegrationTest.testFindEnergySource() {
        EnergySource obj = dod.getRandomEnergySource();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to provide an identifier", id);
        obj = EnergySource.findEnergySource(id);
        Assert.assertNotNull("Find method for 'EnergySource' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'EnergySource' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void EnergySourceIntegrationTest.testFindAllEnergySources() {
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", dod.getRandomEnergySource());
        long count = EnergySource.countEnergySources();
        Assert.assertTrue("Too expensive to perform a find all test for 'EnergySource', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<EnergySource> result = EnergySource.findAllEnergySources();
        Assert.assertNotNull("Find all method for 'EnergySource' illegally returned null", result);
        Assert.assertTrue("Find all method for 'EnergySource' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void EnergySourceIntegrationTest.testFindEnergySourceEntries() {
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", dod.getRandomEnergySource());
        long count = EnergySource.countEnergySources();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<EnergySource> result = EnergySource.findEnergySourceEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'EnergySource' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'EnergySource' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void EnergySourceIntegrationTest.testFlush() {
        EnergySource obj = dod.getRandomEnergySource();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to provide an identifier", id);
        obj = EnergySource.findEnergySource(id);
        Assert.assertNotNull("Find method for 'EnergySource' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyEnergySource(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'EnergySource' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void EnergySourceIntegrationTest.testMergeUpdate() {
        EnergySource obj = dod.getRandomEnergySource();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to provide an identifier", id);
        obj = EnergySource.findEnergySource(id);
        boolean modified =  dod.modifyEnergySource(obj);
        Integer currentVersion = obj.getVersion();
        EnergySource merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'EnergySource' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void EnergySourceIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", dod.getRandomEnergySource());
        EnergySource obj = dod.getNewTransientEnergySource(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'EnergySource' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'EnergySource' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void EnergySourceIntegrationTest.testRemove() {
        EnergySource obj = dod.getRandomEnergySource();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'EnergySource' failed to provide an identifier", id);
        obj = EnergySource.findEnergySource(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'EnergySource' with identifier '" + id + "'", EnergySource.findEnergySource(id));
    }
    
}