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

        assertEquals("Test 1", bom.getSections().get(0).getName());
        assertEquals("Test 2", bom.getSections().get(1).getName());

        Section section = bom.getSections().get(0);

        assertEquals("Test Assembly 1", section.getAssemblies().get(0).getName());
        assertEquals("Test Assembly 2", section.getAssemblies().get(1).getName());


        assertEquals("m", section.getAssemblies().get(0).getUnits());
        assertEquals(10, (int) section.getAssemblies().get(0).getQuantity());
        assertEquals(320, (int) section.getAssemblies().get(0).getTotalEnergy());
        assertEquals(320, (int) section.getAssemblies().get(0).getTotalCarbon());

        AssemblyQuantity assembly = section.getAssemblies().get(0);

        assertEquals("Test Material 1", assembly.getMaterials().get(0).getName());
        assertEquals("Test Material 2", assembly.getMaterials().get(1).getName());

        assertEquals("m2", assembly.getMaterials().get(0).getUnits());
        assertEquals(100, (int) assembly.getMaterials().get(0).getQuantity());
        assertEquals(200, (int) assembly.getMaterials().get(0).getTotalEnergy());
        assertEquals(200, (int) assembly.getMaterials().get(0).getTotalCarbon());

    }
}
