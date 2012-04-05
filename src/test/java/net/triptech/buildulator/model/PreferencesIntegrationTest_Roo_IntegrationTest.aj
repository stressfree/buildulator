// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package net.triptech.buildulator.model;

import java.util.List;
import net.triptech.buildulator.model.Preferences;
import net.triptech.buildulator.model.PreferencesDataOnDemand;
import net.triptech.buildulator.model.PreferencesIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect PreferencesIntegrationTest_Roo_IntegrationTest {
    
    declare @type: PreferencesIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: PreferencesIntegrationTest: @Transactional;
    
    @Autowired
    private PreferencesDataOnDemand PreferencesIntegrationTest.dod;
    
    @Test
    public void PreferencesIntegrationTest.testCountPreferenceses() {
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", dod.getRandomPreferences());
        long count = Preferences.countPreferenceses();
        Assert.assertTrue("Counter for 'Preferences' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void PreferencesIntegrationTest.testFindPreferences() {
        Preferences obj = dod.getRandomPreferences();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to provide an identifier", id);
        obj = Preferences.findPreferences(id);
        Assert.assertNotNull("Find method for 'Preferences' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Preferences' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void PreferencesIntegrationTest.testFindAllPreferenceses() {
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", dod.getRandomPreferences());
        long count = Preferences.countPreferenceses();
        Assert.assertTrue("Too expensive to perform a find all test for 'Preferences', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Preferences> result = Preferences.findAllPreferenceses();
        Assert.assertNotNull("Find all method for 'Preferences' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Preferences' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void PreferencesIntegrationTest.testFindPreferencesEntries() {
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", dod.getRandomPreferences());
        long count = Preferences.countPreferenceses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Preferences> result = Preferences.findPreferencesEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Preferences' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Preferences' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void PreferencesIntegrationTest.testFlush() {
        Preferences obj = dod.getRandomPreferences();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to provide an identifier", id);
        obj = Preferences.findPreferences(id);
        Assert.assertNotNull("Find method for 'Preferences' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPreferences(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Preferences' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void PreferencesIntegrationTest.testMergeUpdate() {
        Preferences obj = dod.getRandomPreferences();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to provide an identifier", id);
        obj = Preferences.findPreferences(id);
        boolean modified =  dod.modifyPreferences(obj);
        Integer currentVersion = obj.getVersion();
        Preferences merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Preferences' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void PreferencesIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", dod.getRandomPreferences());
        Preferences obj = dod.getNewTransientPreferences(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Preferences' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Preferences' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Preferences' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void PreferencesIntegrationTest.testRemove() {
        Preferences obj = dod.getRandomPreferences();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Preferences' failed to provide an identifier", id);
        obj = Preferences.findPreferences(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Preferences' with identifier '" + id + "'", Preferences.findPreferences(id));
    }
    
}
