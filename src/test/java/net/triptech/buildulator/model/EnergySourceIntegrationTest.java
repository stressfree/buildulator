package net.triptech.buildulator.model;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@RooIntegrationTest(entity = EnergySource.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring-test/applicationContext.xml")
public class EnergySourceIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
