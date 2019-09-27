package com.example.howtohelp.model.Data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.howtohelp.RxSchedulersOverrideRule;

import org.junit.ClassRule;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DonationRepositoryTest {

  @ClassRule
  public static InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @ClassRule
  public static RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

  @Test
  public void setParameters() {
    final String CATEGORY_VALUE = "Test Category";
    final String COUNTRY_VALUE = "Test Country";
    DonationRepository donationRepository = new DonationRepository();
    donationRepository.setParameters(CATEGORY_VALUE, COUNTRY_VALUE);

    HashMap<String, String> returnedParameters = donationRepository.getParameters();
    assertEquals("Test set category value", CATEGORY_VALUE, returnedParameters.get("Category"));
    assertEquals("Test set country value", COUNTRY_VALUE, returnedParameters.get("Country"));
  }

  @Test
  public void addTableCellToOpportunityObject() {
    DonationRepository donationRepository = new DonationRepository();
    Donation donation = new Donation();

    final String TITLE_VALUE = "TEST TITLE";
    donationRepository.addTableCellToOpportunityObject("Title", TITLE_VALUE, donation);
    assertEquals("Test set title value from table", TITLE_VALUE, donation.getTitle());

    final String DESCRIPTION_VALUE = "A description";
    donationRepository.addTableCellToOpportunityObject("Description", DESCRIPTION_VALUE, donation);
    assertEquals(
        "Test set description value from table", DESCRIPTION_VALUE, donation.getDescription());

    final String URL_VALUE = "www.cool.com";
    donationRepository.addTableCellToOpportunityObject("URL", URL_VALUE, donation);
    assertEquals("Test set country value from table", URL_VALUE, donation.getUrl());
  }

  @Test
  public void queryAllOpportunitiesMatchingParameters() throws Exception {
    DonationRepository donationRepository = new DonationRepository();
    donationRepository.setParameters("Homelessness", "Ireland");
    donationRepository.queryAllOpportunitiesMatchingParameters();
    assertTrue(
        "Test that donations added to repository",
        donationRepository.getRepository().getValue().size() > 0);
  }

  @Test
  public void initializeOpportunitySubclassObject() {
    DonationRepository donationRepository = new DonationRepository();
    DatabaseOpportunity databaseOpportunity =
        donationRepository.initializeOpportunitySubclassObject();
    assertTrue(
        "Test initialized DatabaseOpportunity is of type Donation",
        databaseOpportunity instanceof Donation);
  }
}
