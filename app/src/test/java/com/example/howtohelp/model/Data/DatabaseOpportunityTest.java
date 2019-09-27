package com.example.howtohelp.model.Data;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class DatabaseOpportunityTest {

  private DatabaseOpportunity databaseOpportunityEmpty;
  private DatabaseOpportunity databaseOpportunityWithValues;

  final String TEST_TITLE = "Starting Title Value";
  final String TEST_URL = "Starting URL value";

  @Before
  public void initializeDatabaseOpportunities() {
    databaseOpportunityEmpty = mock(DatabaseOpportunity.class, Mockito.CALLS_REAL_METHODS);

    databaseOpportunityWithValues = mock(DatabaseOpportunity.class, Mockito.CALLS_REAL_METHODS);
    databaseOpportunityWithValues.setTitle(TEST_TITLE);
    databaseOpportunityWithValues.setUrl(TEST_URL);
  }

  @Test
  public void getTitle() {
    assertEquals(TEST_TITLE, databaseOpportunityWithValues.getTitle());
  }

  @Test
  public void setTitle() {
    assertNull(databaseOpportunityEmpty.getTitle());
    databaseOpportunityEmpty.setTitle(TEST_TITLE);
    assertEquals(TEST_TITLE, databaseOpportunityEmpty.getTitle());
  }

  @Test
  public void getUrl() {
    assertEquals(TEST_URL, databaseOpportunityWithValues.getUrl());
  }

  @Test
  public void setUrl() {
    assertNull(databaseOpportunityEmpty.getUrl());
    databaseOpportunityEmpty.setTitle(TEST_URL);
    assertEquals(TEST_URL, databaseOpportunityEmpty.getTitle());
  }

  @Test
  public void toStringTest() {
    assertEquals(
        "Test that toString returns Title", TEST_TITLE, databaseOpportunityWithValues.toString());
  }
}
