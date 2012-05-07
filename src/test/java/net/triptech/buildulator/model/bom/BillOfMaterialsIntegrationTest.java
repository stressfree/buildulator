package net.triptech.buildulator.model.bom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class BillOfMaterialsIntegrationTest {

    /**
     * Test the parseJson method of class BillOfMaterials.
     */
    @Test
    public void testParseJson() throws IOException {

        String bomString = FileUtils.readFileToString(
                FileUtils.toFile(
                    this.getClass().getResource("/bom.json")
                )
            );

        BillOfMaterials bom = BillOfMaterials.parseJson(bomString);

        runBOMTests(bom);
    }

    /**
     * Test the parseJson method when no data is supplied of class BillOfMaterials.
     */
    @Test
    public void testParseEmptyJson() throws IOException {

        BillOfMaterials bom1 = BillOfMaterials.parseJson("");

        assertNotNull(bom1);

        BillOfMaterials bom2 = BillOfMaterials.parseJson(null);

        assertNotNull(bom2);
    }

    /**
     * Test the toJson method of class BillOfMaterials.
     */
    @Test
    public void testToJson() throws IOException {

        String bomString = FileUtils.readFileToString(
                FileUtils.toFile(
                    this.getClass().getResource("/bom.json")
                )
            );

        BillOfMaterials existingBOM = BillOfMaterials.parseJson(bomString);

        String processedJson = existingBOM.toJson();

        BillOfMaterials bom = BillOfMaterials.parseJson(processedJson);

        runBOMTests(bom);
    }

    /**
     * Run bill of materials tests.
     *
     * @param bom the bom
     */
    private void runBOMTests(final BillOfMaterials bom) {

        assertEquals("Test Section 1", bom.getSections().get(0).getName());
        assertEquals("Test Section 2", bom.getSections().get(1).getName());

        Section section = bom.getSections().get(0);

        assertEquals("Test Element 1", section.getElements().get(0).getName());
        assertEquals("Test Element 2", section.getElements().get(1).getName());


        assertEquals("m", section.getElements().get(0).getUnits());
        assertEquals(10, (int) section.getElements().get(0).getQuantity());
        assertEquals(320, (int) section.getElements().get(0).getTotalEnergy());
        assertEquals(320, (int) section.getElements().get(0).getTotalCarbon());

        Element element = section.getElements().get(0);

        assertEquals("Test Material 1", element.getMaterials().get(0).getName());
        assertEquals("Test Material 2", element.getMaterials().get(1).getName());

        assertEquals("m2", element.getMaterials().get(0).getUnits());
        assertEquals(100, (int) element.getMaterials().get(0).getQuantity());
        assertEquals(200, (int) element.getMaterials().get(0).getTotalEnergy());
        assertEquals(200, (int) element.getMaterials().get(0).getTotalCarbon());

    }
}
