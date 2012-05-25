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
import net.triptech.buildulator.model.Preferences;
import net.triptech.buildulator.model.PreferencesDataOnDemand;
import org.springframework.stereotype.Component;

privileged aspect PreferencesDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PreferencesDataOnDemand: @Component;
    
    private Random PreferencesDataOnDemand.rnd = new SecureRandom();
    
    private List<Preferences> PreferencesDataOnDemand.data;
    
    public Preferences PreferencesDataOnDemand.getNewTransientPreferences(int index) {
        Preferences obj = new Preferences();
        setAboutContent(obj, index);
        setAboutEnabled(obj, index);
        setAboutTitle(obj, index);
        setCopyrightNotice(obj, index);
        setGoogleTrackingId(obj, index);
        setHomepageContent(obj, index);
        setHomepageTitle(obj, index);
        setName(obj, index);
        setTargetProjectId(obj, index);
        setTocContent(obj, index);
        setTocEnabled(obj, index);
        setTocTitle(obj, index);
        setUrl(obj, index);
        return obj;
    }
    
    public void PreferencesDataOnDemand.setAboutContent(Preferences obj, int index) {
        String aboutContent = "aboutContent_" + index;
        obj.setAboutContent(aboutContent);
    }
    
    public void PreferencesDataOnDemand.setAboutEnabled(Preferences obj, int index) {
        Boolean aboutEnabled = true;
        obj.setAboutEnabled(aboutEnabled);
    }
    
    public void PreferencesDataOnDemand.setAboutTitle(Preferences obj, int index) {
        String aboutTitle = "aboutTitle_" + index;
        if (aboutTitle.length() > 200) {
            aboutTitle = aboutTitle.substring(0, 200);
        }
        obj.setAboutTitle(aboutTitle);
    }
    
    public void PreferencesDataOnDemand.setCopyrightNotice(Preferences obj, int index) {
        String copyrightNotice = "copyrightNotice_" + index;
        if (copyrightNotice.length() > 100) {
            copyrightNotice = copyrightNotice.substring(0, 100);
        }
        obj.setCopyrightNotice(copyrightNotice);
    }
    
    public void PreferencesDataOnDemand.setGoogleTrackingId(Preferences obj, int index) {
        String googleTrackingId = "googleTrackingId_" + index;
        obj.setGoogleTrackingId(googleTrackingId);
    }
    
    public void PreferencesDataOnDemand.setHomepageContent(Preferences obj, int index) {
        String homepageContent = "homepageContent_" + index;
        obj.setHomepageContent(homepageContent);
    }
    
    public void PreferencesDataOnDemand.setHomepageTitle(Preferences obj, int index) {
        String homepageTitle = "homepageTitle_" + index;
        if (homepageTitle.length() > 200) {
            homepageTitle = homepageTitle.substring(0, 200);
        }
        obj.setHomepageTitle(homepageTitle);
    }
    
    public void PreferencesDataOnDemand.setName(Preferences obj, int index) {
        String name = "name_" + index;
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        obj.setName(name);
    }
    
    public void PreferencesDataOnDemand.setTargetProjectId(Preferences obj, int index) {
        Long targetProjectId = new Integer(index).longValue();
        obj.setTargetProjectId(targetProjectId);
    }
    
    public void PreferencesDataOnDemand.setTocContent(Preferences obj, int index) {
        String tocContent = "tocContent_" + index;
        obj.setTocContent(tocContent);
    }
    
    public void PreferencesDataOnDemand.setTocEnabled(Preferences obj, int index) {
        Boolean tocEnabled = true;
        obj.setTocEnabled(tocEnabled);
    }
    
    public void PreferencesDataOnDemand.setTocTitle(Preferences obj, int index) {
        String tocTitle = "tocTitle_" + index;
        if (tocTitle.length() > 200) {
            tocTitle = tocTitle.substring(0, 200);
        }
        obj.setTocTitle(tocTitle);
    }
    
    public void PreferencesDataOnDemand.setUrl(Preferences obj, int index) {
        String url = "urlxxxxx_" + index;
        if (url.length() > 200) {
            url = url.substring(0, 200);
        }
        obj.setUrl(url);
    }
    
    public Preferences PreferencesDataOnDemand.getSpecificPreferences(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Preferences obj = data.get(index);
        Long id = obj.getId();
        return Preferences.findPreferences(id);
    }
    
    public Preferences PreferencesDataOnDemand.getRandomPreferences() {
        init();
        Preferences obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Preferences.findPreferences(id);
    }
    
    public boolean PreferencesDataOnDemand.modifyPreferences(Preferences obj) {
        return false;
    }
    
    public void PreferencesDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Preferences.findPreferencesEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Preferences' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Preferences>();
        for (int i = 0; i < 10; i++) {
            Preferences obj = getNewTransientPreferences(i);
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
