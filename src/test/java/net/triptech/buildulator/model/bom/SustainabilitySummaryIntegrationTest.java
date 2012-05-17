package net.triptech.buildulator.model.bom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class SustainabilitySummaryIntegrationTest {

    /**
     * Test the parseJson method of class SustainabilitySummary.
     */
    @Test
    public void testParseJson() throws IOException {

        String ssString = FileUtils.readFileToString(
                FileUtils.toFile(
                    this.getClass().getResource("/ss.json")
                )
            );

        SustainabilitySummary ss = SustainabilitySummary.parseJson(ssString);

        runSSTests(ss);
    }

    /**
     * Test the parseJson method when no data is supplied of class SustainabilitySummary.
     */
    @Test
    public void testParseEmptyJson() throws IOException {

        SustainabilitySummary ss1 = SustainabilitySummary.parseJson("");

        assertNotNull(ss1);

        SustainabilitySummary ss2 = SustainabilitySummary.parseJson(null);

        assertNotNull(ss2);
    }

    /**
     * Test the toJson method of class SustainabilitySummary.
     */
    @Test
    public void testToJson() throws IOException {

        SustainabilitySummary newSS = new SustainabilitySummary();

        newSS.setElOperationalDetailed(2);
        newSS.setElOperationalTotal(2);
        newSS.setElConstructionDetailed(1);
        newSS.setElConstructionTotal(2);

        newSS.setOccupants(2);
        newSS.setEnergyOperational(500.0);
        newSS.setEnergyConstruction(500.0);
        newSS.setCarbonOperational(2000.0);
        newSS.setCarbonConstruction(3000.0);

        List<Double> energyChange = new ArrayList<Double>();
        energyChange.add(50.0);
        energyChange.add(100.0);
        energyChange.add(200.0);
        energyChange.add(600.0);
        energyChange.add(800.0);
        energyChange.add(1000.0);

        List<Double> carbonChange = new ArrayList<Double>();
        carbonChange.add(500.0);
        carbonChange.add(1000.0);
        carbonChange.add(2000.0);
        carbonChange.add(3000.0);
        carbonChange.add(4000.0);
        carbonChange.add(5000.0);

        newSS.setTotalEnergyChange(energyChange);
        newSS.setTotalCarbonChange(carbonChange);

        String processedJson = newSS.toJson();

        SustainabilitySummary ss = SustainabilitySummary.parseJson(processedJson);

        runSSTests(ss);
    }

    /**
     * Run bill of materials tests.
     *
     * @param ss the ss
     */
    private void runSSTests(final SustainabilitySummary ss) {

        assertEquals(1000, (int) ss.getEnergyTotal());
        assertEquals(5000, (int) ss.getCarbonTotal());
        assertEquals(500, (int) ss.getEnergyPerOccupant());
        assertEquals(2500, (int) ss.getCarbonPerOccupant());

    }
}
