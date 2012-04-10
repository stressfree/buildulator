package net.triptech.buildulator.model;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@RooIntegrationTest(entity = Material.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring-test/applicationContext.xml")
public class MaterialIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
}
