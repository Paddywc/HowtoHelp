package com.example.howtohelp.model;

import com.example.howtohelp.model.Data.Donation;
import com.example.howtohelp.model.Data.MicroVolunteering;
import com.example.howtohelp.model.Data.Politician;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OpportunityTest {

  @Test
  public void testConstructorWithDonation() {
    final String TEST_DESCRIPTION = "Help fund a student with little employment opportunities";
    final String TEST_URL = "please...";

    Donation donation = new Donation();
    donation.setDescription(TEST_DESCRIPTION);
    donation.setUrl(TEST_URL);

    Opportunity opportunity = new Opportunity(donation);
    assertEquals(TEST_DESCRIPTION, opportunity.getDescription());
    assertEquals(TEST_URL, opportunity.getUrl());
  }

  @Test
  public void testConstructorWithMicroVolunteering() {
    final String MICRO_TYPE = "Test type";
    final String DESCRIPTION = "A description";

    MicroVolunteering microVolunteering = new MicroVolunteering();
    microVolunteering.setMicroType(MICRO_TYPE);
    microVolunteering.setDescription(DESCRIPTION);

    Opportunity opportunity = new Opportunity(microVolunteering);
    assertEquals(MICRO_TYPE, opportunity.getMicroVolunteeringType());
    assertEquals(DESCRIPTION, opportunity.getDescription());
  }

  @Test
  public void testConstructorWithPolitician() {
    final String AREA = "Cool Country";
    final String TWITTER = "MyHandle";
    final String EMAIL = "Person@Email.com";
    final String PARTY = "Yes please!";

    Politician politician = new Politician();
    politician.setArea(AREA);
    politician.setTwitter(TWITTER);
    politician.setEmail(EMAIL);
    politician.setParty(PARTY);

    Opportunity opportunity = new Opportunity(politician);
    assertEquals(AREA, opportunity.getArea());
    assertEquals(TWITTER, opportunity.getTwitter());
    assertEquals(EMAIL, opportunity.getEmail());
    assertEquals(PARTY, opportunity.getParty());
  }

  @Test
  public void getOpportunityType() {
    Politician politician = new Politician();
    Opportunity politicianOpportunity = new Opportunity(politician);
    assertEquals("Politician", politicianOpportunity.getOpportunityType());

    MicroVolunteering microVolunteering = new MicroVolunteering();
    Opportunity microVolunteeringOpportunity = new Opportunity(microVolunteering);
    assertEquals("Micro Volunteering", microVolunteeringOpportunity.getOpportunityType());

    Donation donation = new Donation();
    Opportunity donationOpportunity = new Opportunity(donation);
    assertEquals("Donation", donationOpportunity.getOpportunityType());
  }

  @Test
  public void setOpportunityType() {
    final String TYPE = "A test type";
    Opportunity opportunity = new Opportunity();
    opportunity.setOpportunityType(TYPE);
    assertEquals(TYPE, opportunity.getOpportunityType());
  }

  @Test
  public void getAndSetTitle() {
    final String TITLE = "A test title";
    Opportunity opportunity = new Opportunity();
    opportunity.setTitle(TITLE);
    assertEquals(TITLE, opportunity.getTitle());
  }

  @Test
  public void getAndSetUrl() {
    final String URL = "Test.com";
    Opportunity opportunity = new Opportunity();
    opportunity.setUrl(URL);
    assertEquals(URL, opportunity.getUrl());
  }

  @Test
  public void getAndSetDescription() {
    final String DESCRIPTION = "I hope this works";
    Opportunity opportunity = new Opportunity();
    opportunity.setDescription(DESCRIPTION);
    assertEquals(DESCRIPTION, opportunity.getDescription());
  }

  @Test
  public void testToString() {
    final String TO_STRING_WHEN_ALL_ATTRIBUTES_NULL = "null:null";
    Opportunity opportunity = new Opportunity();
    assertEquals(
        "Test two nulls appear when all attributes null",
        TO_STRING_WHEN_ALL_ATTRIBUTES_NULL,
        opportunity.toString());
  }
}
