package com.example.howtohelp.ui.ui.socialissue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.howtohelp.RxSchedulersOverrideRule;
import com.example.howtohelp.model.Data.SocialIssueRepository;
import com.example.howtohelp.model.Opportunity;
import com.example.howtohelp.model.OpportunityRepository;
import com.example.howtohelp.ui.OpportunitiesViewModel;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OpportunitiesViewModelTest {

  private final String TEST_CATEGORY = "Test Category";
  private final String INVALID_URL = "nooooo!";

  @Mock private OpportunityRepository opportunityRepository;

  @Mock private SocialIssueRepository socialIssueRepository;

  @InjectMocks private OpportunitiesViewModel viewModelWithMocks = new OpportunitiesViewModel();

  @ClassRule
  public static InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @ClassRule
  public static RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

  @Before
  public void initializeMocks() throws Exception {
    initMocks(this);
    when(socialIssueRepository.returnSocialIssueForUrl(TEST_CATEGORY)).thenReturn(TEST_CATEGORY);
    SocialIssueRepository realSocialIssueRepository = new SocialIssueRepository();
    String failedText = realSocialIssueRepository.getFailedText();
    when(socialIssueRepository.returnSocialIssueForUrl(INVALID_URL)).thenReturn(failedText);
  }

  @Test
  public void shouldCreateFragment() {
    OpportunitiesViewModel viewModel = viewModelWithMocks;

    List<Opportunity> opportunities = new ArrayList<>();

    viewModel.getOpportunities().postValue(opportunities);
    assertFalse(
        "Test return false when opportunities list is empty", viewModel.shouldCreateOpportunitiesFragment());

    Opportunity opportunity = mock(Opportunity.class);
    opportunities.add(opportunity);
    viewModel.getOpportunities().postValue(opportunities);
    assertFalse(
        "Test returns true when there is at least one value in opportunities list",
        viewModel.shouldCreateOpportunitiesFragment());

    assertFalse(
        "Test method only returns false after it has returned true",
        viewModel.shouldCreateOpportunitiesFragment());
  }

  @Test
  public void setHighestIndexVisitedByUser() {
    final int VALUE = 5;
    viewModelWithMocks.setHighestIndexVisitedByUser(VALUE);
    verify(opportunityRepository, times(1)).setStartingRandomIndex(VALUE);
  }

  @Test
  public void shouldAddToFragment() {
    OpportunitiesViewModel viewModel = new OpportunitiesViewModel();
    assertFalse(
        "Test returns false when detail fragment has not been initialized",
        viewModel.shouldAddToFragment(1));

    List<Opportunity> opportunities = new ArrayList<>();
    Opportunity opportunity = mock(Opportunity.class);
    opportunities.add(opportunity);
    viewModel.getOpportunities().postValue(opportunities);
    viewModel.shouldCreateOpportunitiesFragment();
    assertFalse(
        "Test returns false when opportunities list size is 1 or smaller",
        viewModel.shouldAddToFragment(1));

    opportunities.add(opportunity);
    assertFalse(
        "Test returns true when opportunities list size is 2 or greater ",
        viewModel.shouldAddToFragment(1));
  }

  @Test
  public void getOpportunityAtIndex() {
    final String TITLE_ONE = "One";
    final String TITLE_TWO = "Two";
    final String TITLE_THREE = "Three";

    OpportunitiesViewModel viewModel = new OpportunitiesViewModel();
    List<Opportunity> opportunities = new ArrayList<>();

    Opportunity opportunityOne = mock(Opportunity.class);
    when(opportunityOne.getTitle()).thenReturn(TITLE_ONE);
    opportunities.add(opportunityOne);
    Opportunity opportunityTwo = mock(Opportunity.class);
    when(opportunityTwo.getTitle()).thenReturn(TITLE_TWO);
    opportunities.add(opportunityTwo);
    Opportunity opportunityThree = mock(Opportunity.class);
    when(opportunityThree.getTitle()).thenReturn(TITLE_THREE);
    opportunities.add(opportunityThree);

    viewModel.getOpportunities().postValue(opportunities);
    assertEquals(
        "Test returns opportunity at argument index",
        TITLE_TWO,
        viewModel.getOpportunityAtIndex(1).getValue().getTitle());
  }

  @Test
  public void setUserLocation() {
    final String POSTCODE = "ABC 123";

    OpportunitiesViewModel viewModel = new OpportunitiesViewModel();

    viewModel.setUserLocation("", "", POSTCODE, 0.0, 0.0);
    assertEquals(
        "Test sets UserLocation attribute of ViewModel",
        POSTCODE,
        viewModel.getUserLocation().getValue().getPostCode());
  }

  @Test
  public void queryAppropriateData() {
    viewModelWithMocks.queryAppropriateData();
    // Test no opportunities are queried when ViewModel has neither location or category initialized
    verify(opportunityRepository, never())
        .addAllDonationsMatchingParameters(any(MutableLiveData.class), any(MutableLiveData.class));
    verify(opportunityRepository, never())
        .addPoliticiansMatchingParameters(any(MutableLiveData.class), any(Integer.class));
    verify(opportunityRepository, never())
        .addMicroVolunteeringMatchingParameters(any(MutableLiveData.class), any(Integer.class));

    // Test only MicroVolunteering opportunities are queried when ViewModel has category but no
    // location
    viewModelWithMocks.setCategory(TEST_CATEGORY);
    viewModelWithMocks.queryAppropriateData();
    verify(opportunityRepository, never())
        .addAllDonationsMatchingParameters(any(MutableLiveData.class), any(MutableLiveData.class));
    verify(opportunityRepository, never())
        .addPoliticiansMatchingParameters(any(MutableLiveData.class), any(Integer.class));
    verify(opportunityRepository, times(1))
        .addMicroVolunteeringMatchingParameters(any(MutableLiveData.class), any(Integer.class));

    // Test MicroVolunteering opportunities can only be queried once
    viewModelWithMocks.queryAppropriateData();
    verify(opportunityRepository, times(1))
        .addMicroVolunteeringMatchingParameters(any(MutableLiveData.class), any(Integer.class));

    // Test Donation and Politician queried when both userLocation and category  initialized
    viewModelWithMocks.setUserLocation("", "", "", 0.0, 0.0);
    viewModelWithMocks.queryAppropriateData();
    verify(opportunityRepository, times(1))
        .addAllDonationsMatchingParameters(any(MutableLiveData.class), any(MutableLiveData.class));
    verify(opportunityRepository, times(1))
        .addPoliticiansMatchingParameters(any(MutableLiveData.class), any(Integer.class));
    verify(opportunityRepository, times(1))
        .addMicroVolunteeringMatchingParameters(any(MutableLiveData.class), any(Integer.class));

    // Test once all opportunity types are queried no more quires can be conducted
    viewModelWithMocks.queryAppropriateData();
    verify(opportunityRepository, times(1))
        .addAllDonationsMatchingParameters(any(MutableLiveData.class), any(MutableLiveData.class));
    verify(opportunityRepository, times(1))
        .addPoliticiansMatchingParameters(any(MutableLiveData.class), any(Integer.class));
    verify(opportunityRepository, times(1))
        .addMicroVolunteeringMatchingParameters(any(MutableLiveData.class), any(Integer.class));
  }

  @Test
  public void setAndCategory() {
    viewModelWithMocks.setCategory(TEST_CATEGORY);
    assertEquals(
        "Test category setter and getter returns the ViewModel's category",
        TEST_CATEGORY,
        viewModelWithMocks.getCategory().getValue());

    viewModelWithMocks.setCategory(INVALID_URL);
    assertEquals(
        "Test sets category to invalid URL text when invalid url sent",
        viewModelWithMocks.getINVALID_URL_TEXT(),
        viewModelWithMocks.getCategory().getValue());
  }

  @Test
  public void getInvalidUrlText() {
    final String INVALID_URL_TEXT = "INVALID URL";
    assertEquals(viewModelWithMocks.getINVALID_URL_TEXT(), INVALID_URL_TEXT);
  }
}
