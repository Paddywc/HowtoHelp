package com.howtohelp.howtohelp.model;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class UserLocationTest {

  @Test
  public void constructorAndGetters() {
    final String IRELAND_COUNTRY = "Ireland";
    final String UK_COUNTRY = "United Kingdom";
    final String IRELAND_SUBCOUNTRY = "Dublin";
    final String UK_SUBCOUNTRY = "Wales";
    final String POSTCODE = "212";
    final double LATITUDE = 0.12345;
    final double LONGITUDE = 6.789;

    UserLocation irelandUserLocation =
        new UserLocation(IRELAND_COUNTRY, IRELAND_SUBCOUNTRY, POSTCODE, LATITUDE, LONGITUDE);
    assertEquals("Test set Ireland as country", IRELAND_COUNTRY, irelandUserLocation.getCountry());
    assertEquals("Test postcode set in constructor", POSTCODE, irelandUserLocation.getPostCode());
    assertEquals(
        "Test latitude set in constructor", LATITUDE, irelandUserLocation.getLatitude(), 0.0);
    assertEquals(
        "Test longitude set in constructor", LONGITUDE, irelandUserLocation.getLongitude(), 0.0);

    UserLocation ukUserLocation =
        new UserLocation(UK_COUNTRY, UK_SUBCOUNTRY, POSTCODE, LATITUDE, LONGITUDE);
    assertEquals(
        "Test subcountry set as country when country is UK",
        UK_SUBCOUNTRY,
        ukUserLocation.getCountry());
  }
}
