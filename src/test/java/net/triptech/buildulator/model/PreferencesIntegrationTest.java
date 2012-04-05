package net.triptech.buildulator.model;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@RooIntegrationTest(entity = Preferences.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring-test/applicationContext.xml")
public class PreferencesIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
