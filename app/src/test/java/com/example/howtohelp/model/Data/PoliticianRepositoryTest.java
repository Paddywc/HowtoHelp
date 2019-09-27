package com.example.howtohelp.model.Data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.howtohelp.RxSchedulersOverrideRule;

import org.junit.ClassRule;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PoliticianRepositoryTest {

  @ClassRule
  public static InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @ClassRule
  public static RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

  @Test
  public void addTableCellToOpportunityObject() {}

  @Test
  public void initializeOpportunitySubclassObject() {}

  @Test
  public void setParameters() {
    final int LIMIT = 4;
    final String IRELAND_COUNTRY = "Ireland";
    final String UK_COUNTRY = "Scotland";
    final String POSTCODE = "212";
    final double LATITUDE = 0.5493932939;
    final double LONGITUDE = 1.34434;

    PoliticianRepository irelandPoliticianRepository = new PoliticianRepository();
    irelandPoliticianRepository.setParameters(
        IRELAND_COUNTRY, POSTCODE, LATITUDE, LONGITUDE, LIMIT);
    HashMap<String, String> irelandParameters = irelandPoliticianRepository.getParameters();
    assertEquals("Test limit set in Ireland Country", "" + LIMIT, irelandParameters.get("Limit"));
    assertEquals(
        "Test Country set in Ireland Country",
        "" + IRELAND_COUNTRY,
        irelandParameters.get("Country"));
    assertEquals(
        "Test longitude set in Ireland Country",
        "" + LONGITUDE,
        irelandParameters.get("Longitude"));
    assertEquals(
        "Test Latitude set in Ireland Country", "" + LATITUDE, irelandParameters.get("Latitude"));
    assertNull("Test postcode NOT set in Ireland Country", irelandParameters.get("Postcode"));

    PoliticianRepository ukPoliticianRepository = new PoliticianRepository();
    ukPoliticianRepository.setParameters(UK_COUNTRY, POSTCODE, LATITUDE, LONGITUDE, LIMIT);
    HashMap<String, String> ukParameters = ukPoliticianRepository.getParameters();

    assertEquals("Test limit set in UK Country", "" + LIMIT, ukParameters.get("Limit"));
    assertEquals("Test Country set in UK Country", "" + UK_COUNTRY, ukParameters.get("Country"));
    assertNull("Test longitude NOT set in UK Country", ukParameters.get("Longitude"));
    assertNull("Test latitude NOT set in UK Country", ukParameters.get("Latitude"));
    assertEquals("Test postcode set set in UK Country", POSTCODE, ukParameters.get("Postcode"));
  }

  @Test
  public void queryAllOpportunitiesMatchingParameters() throws Exception {
    PoliticianRepository politicianRepository = new PoliticianRepository();
    politicianRepository.setParameters("Northern%20Ireland", "BT472AH", 0.0, 0.0, 1);
    politicianRepository.queryAllOpportunitiesMatchingParameters();
    assertTrue(
        "Test query UK politicians returned results",
        politicianRepository.getRepository().getValue().size() > 0);
    assertEquals(
        "Test query politicians limited to 1",
        1,
        politicianRepository.getRepository().getValue().size());

    PoliticianRepository irelandPoliticianRepository = new PoliticianRepository();
    irelandPoliticianRepository.setParameters("Ireland", "", 52.1545, 9.5669, 1);
    irelandPoliticianRepository.queryAllOpportunitiesMatchingParameters();
    assertTrue(
        "Test query Ire politicians returned results",
        politicianRepository.getRepository().getValue().size() > 0);
  }
}
