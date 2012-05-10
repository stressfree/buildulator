package net.triptech.buildulator.model;

import static org.junit.Assert.assertTrue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@RooIntegrationTest(entity = Project.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring-test/applicationContext.xml")
public class ProjectIntegrationTest {

    @Test
    public void testDataFieldMethods() throws Exception {

        Project project = new Project();

        String dataString = FileUtils.readFileToString(
                FileUtils.toFile(
                    this.getClass().getResource("/bom.json")
                )
            );

        project.setDataField("test", dataString);
        project.setDataField("test2", dataString);

        String readString = project.getDataField("test");

        assertTrue(StringUtils.equals(dataString, readString));
    }

}
