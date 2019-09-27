package com.example.howtohelp.model.Data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.howtohelp.RxSchedulersOverrideRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class DatabaseOpportunityRepositoryTest {

  private String TEST_KEY = "Test key";
  private String TEST_VALUE = "A Value";

  // Used so that constructor can be called
  private class MockedDatabaseOpportunityRepository extends DatabaseOpportunityRepository {

    public MockedDatabaseOpportunityRepository() {
      super();
    }

    @Override
    void addTableCellToOpportunityObject(
        String tableColumnApiTitle, String cellValue, DatabaseOpportunity opportunitySubtype) {}

    @Override
    DatabaseOpportunity initializeOpportunitySubclassObject() {
      return null;
    }

    @Override
    public void queryAllOpportunitiesMatchingParameters() {}
  }

  @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @Rule public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

  @Test
  public void addParameter() {
    DatabaseOpportunityRepository databaseOpportunityRepository =
        new MockedDatabaseOpportunityRepository();
    databaseOpportunityRepository.addParameter(TEST_KEY, TEST_VALUE);
    HashMap<String, String> returnedParameters = databaseOpportunityRepository.getParameters();
    assertEquals(1, returnedParameters.size());
    assertEquals(TEST_VALUE, returnedParameters.get(TEST_KEY));
  }

  @Test
  public void getRepository() {
    DatabaseOpportunityRepository databaseOpportunityRepository =
        new MockedDatabaseOpportunityRepository();
    MutableLiveData repository = databaseOpportunityRepository.getRepository();
    assertEquals(ArrayList.class, repository.getValue().getClass());
  }

  @Test
  public void getParameters() {
    final String KEY = "Test Key";
    final String VALUE = "Test value";

    DatabaseOpportunityRepository databaseOpportunityRepository =
        new MockedDatabaseOpportunityRepository();
    databaseOpportunityRepository.addParameter(KEY, VALUE);
    HashMap<String, String> returnedParameters = databaseOpportunityRepository.getParameters();
    assertEquals(1, returnedParameters.size());
    assertEquals(VALUE, returnedParameters.get(KEY));
  }
}
