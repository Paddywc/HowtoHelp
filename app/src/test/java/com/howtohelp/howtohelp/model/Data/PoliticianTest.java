package com.howtohelp.howtohelp.model.Data;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class PoliticianTest {

  @Test
  public void constructor() {
    final String TITLE = "Mr. Man";
    final String URL = "www.aurl.com";
    final String AREA = "This place";
    final String TWITTER = "tweettweet";
    final String EMAIL = "example@email.com";
    final String PARTY = "woo!";

    Politician politician = new Politician(TITLE, URL, AREA, TWITTER, EMAIL, PARTY);
    assertEquals("Title set in constructor", TITLE, politician.getTitle());
    assertEquals("URL set in constructor", URL, politician.getUrl());
    assertEquals("Area set in constructor", AREA, politician.getArea());
    assertEquals("Twitter set in constructor", TWITTER, politician.getTwitter());
    assertEquals("Email set in constructor", EMAIL, politician.getEmail());
    assertEquals("Party set in constructor", PARTY, politician.getParty());
  }

  @Test
  public void getArea() {}

  @Test
  public void setArea() {}

  @org.junit.jupiter.api.Test
  public void getTwitter() {}

  @Test
  public void setTwitter() {}

  @Test
  public void getEmail() {}

  @Test
  public void setEmail() {}

  @Test
  public void getParty() {}

  @Test
  public void setParty() {}
}
