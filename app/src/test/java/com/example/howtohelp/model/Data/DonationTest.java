package com.example.howtohelp.model.Data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.howtohelp.RxSchedulersOverrideRule;

import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DonationTest {

  @ClassRule
  public static InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @ClassRule
  public static RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

  @Test
  public void constructor() {
    final String TITLE = "A test title",
        URL = "https://www.awebsite.com",
        DESCRIPTION = "money well spent/given";
    Donation donation = new Donation(TITLE, URL, DESCRIPTION);

    assertEquals("Test constructor set title", TITLE, donation.getTitle());
    assertEquals("Test constructor set URL", URL, donation.getUrl());
    assertEquals("Test constructor set description", DESCRIPTION, donation.getDescription());
  }

  @Test
  public void setAndGetDescription() {
    final String DESCRIPTION = "This is a donation description";
    Donation donation = new Donation();
    donation.setDescription(DESCRIPTION);
    assertEquals("Test set and get donation description", DESCRIPTION, donation.getDescription());
  }
}
