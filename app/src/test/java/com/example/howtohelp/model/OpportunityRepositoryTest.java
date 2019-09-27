package com.example.howtohelp.model;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.howtohelp.RxSchedulersOverrideRule;
import com.example.howtohelp.model.Data.Donation;
import com.example.howtohelp.model.Data.DonationRepository;
import com.example.howtohelp.model.Data.MicroVolunteering;
import com.example.howtohelp.model.Data.MicroVolunteeringRepository;
import com.example.howtohelp.model.Data.Politician;
import com.example.howtohelp.model.Data.PoliticianRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OpportunityRepositoryTest {

  private MutableLiveData<List<Donation>> donationList;
  private MutableLiveData<List<Politician>> politicianList;
  private MutableLiveData<List<MicroVolunteering>> microVolunteeringList;
  final String DONATION_TITLE = "A Donation";
  final String POLITICIAN_TITLE = "Chuck Norris";
  final String MICRO_VOLUNTEERING_TITLE = "Be Sound";

  @Mock
  private DonationRepository donationRepository;
  @Mock
  private PoliticianRepository politicianRepository;
  @Mock
  private MicroVolunteeringRepository microVolunteeringRepository;

  @InjectMocks
  private OpportunityRepository opportunityRepository = new OpportunityRepository();

  @Before
  public void initializeMocks() throws Exception {
    initMocks(this);
    donationList = new MutableLiveData<>();
    donationList.postValue(new ArrayList<>());
    politicianList = new MutableLiveData<>();
    politicianList.postValue(new ArrayList<>());
    microVolunteeringList = new MutableLiveData<>();
    microVolunteeringList.postValue(new ArrayList<>());

    when(donationRepository.getRepository()).thenReturn(donationList);
    doAnswer((Answer<Void>) invocation -> {
      Donation donation = mock(Donation.class);
      when(donation.getTitle()).thenReturn(DONATION_TITLE);
      donationList.getValue().add(donation);
      donationList.postValue(donationList.getValue());
      return null;
    }).when(donationRepository).queryAllOpportunitiesMatchingParameters();

    when(politicianRepository.getRepository()).thenReturn(politicianList);
    doAnswer((Answer<Void>) invocation -> {
      Politician politician = mock(Politician.class);
      when(politician.getTitle()).thenReturn(POLITICIAN_TITLE);
      politicianList.getValue().add(politician);
      politicianList.postValue(politicianList.getValue());
      return null;
    }).when(politicianRepository).queryAllOpportunitiesMatchingParameters();

    when(microVolunteeringRepository.getRepository()).thenReturn(microVolunteeringList);
    doAnswer((Answer<Void>) invocation -> {
      MicroVolunteering microVolunteering = mock(MicroVolunteering.class);
      when(microVolunteering.getTitle()).thenReturn(MICRO_VOLUNTEERING_TITLE);
      microVolunteeringList.getValue().add(microVolunteering);
      microVolunteeringList.postValue(microVolunteeringList.getValue());
      return null;
    }).when(microVolunteeringRepository).queryAllOpportunitiesMatchingParameters();
  }

  @Before
  public void observeOpportunityRepository()  {

    opportunityRepository.getRepository().observeForever(opportunities -> {
    });
  }

  @After
  public void destroyRepository() {
    opportunityRepository = null;
  }

  @After
  public void emptyLists() {
    donationList.postValue(new ArrayList<>());
    politicianList.postValue(new ArrayList<>());
    microVolunteeringList.postValue(new ArrayList<>());
  }


  @ClassRule
  public static InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @ClassRule
  public static RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();


  @Test
  public void constructor() {
    OpportunityRepository opportunityRepository = new OpportunityRepository();
    assertNotNull("Test initializes repository in constructor", opportunityRepository.getRepository().getValue());
  }

  @Test
  public void addAllDonationsMatchingParameters() throws Exception {
    opportunityRepository.observeDonations();

    UserLocation userLocation = mock(UserLocation.class);
    when(userLocation.getCountry()).thenReturn("");
    MutableLiveData liveDataUserLocation = new MutableLiveData();
    liveDataUserLocation.postValue(userLocation);

    String category = "";
    MutableLiveData liveDataCategory = new MutableLiveData();
    liveDataCategory.postValue(category);


    opportunityRepository.addAllDonationsMatchingParameters(liveDataUserLocation, liveDataCategory);

    assertEquals("Test new values added to donationRepository are added to repository", DONATION_TITLE, opportunityRepository.getRepository().getValue().get(0).getTitle());


    doThrow(Exception.class).when(donationRepository).queryAllOpportunitiesMatchingParameters();
    opportunityRepository.addAllDonationsMatchingParameters(liveDataUserLocation, liveDataCategory);
    assertEquals("Test repository unaffected if politicianRepository throws exception", 1, opportunityRepository.getRepository().getValue().size());
  }


  @Test
  public void addPoliticiansMatchingParameters() throws Exception {
    opportunityRepository.observePoliticians();

    UserLocation userLocation = mock(UserLocation.class);
    when(userLocation.getCountry()).thenReturn("");
    when(userLocation.getLatitude()).thenReturn(0.0);
    when(userLocation.getLongitude()).thenReturn(0.0);
    when(userLocation.getCountry()).thenReturn("");
    when(userLocation.getPostCode()).thenReturn("");
    MutableLiveData liveDataUserLocation = new MutableLiveData();
    liveDataUserLocation.postValue(userLocation);

    opportunityRepository.addPoliticiansMatchingParameters(liveDataUserLocation, 1);
    assertEquals("Test new values added to politicianRepository are added to repository", POLITICIAN_TITLE, opportunityRepository.getRepository().getValue().get(0).getTitle());

    final String NEW_TITLE_ONE = "1";
    final String NEW_TITLE_TWO = "2";
    final String NEW_TITLE_THREE = "3";

    Politician politicianOne = mock(Politician.class);
    when(politicianOne.getTitle()).thenReturn(NEW_TITLE_ONE);
    politicianList.getValue().add(politicianOne);
    politicianList.postValue(politicianList.getValue());

    Politician politicianTwo = mock(Politician.class);
    when(politicianTwo.getTitle()).thenReturn(NEW_TITLE_TWO);
    politicianList.getValue().add(politicianTwo);
    politicianList.postValue(politicianList.getValue());

    Politician politicianThree = mock(Politician.class);
    when(politicianThree.getTitle()).thenReturn(NEW_TITLE_THREE);
    politicianList.getValue().add(politicianThree);
    politicianList.postValue(politicianList.getValue());
    politicianList.getValue().add(politicianThree);
    politicianList.postValue(politicianList.getValue());

    Politician duplicateOfPoliticianThree = mock(Politician.class);
    when(duplicateOfPoliticianThree.getTitle()).thenReturn(NEW_TITLE_THREE);
    politicianList.getValue().add(duplicateOfPoliticianThree);
    politicianList.postValue(politicianList.getValue());

    final int EXPECTED_SIZE = 4;
    assertEquals("Test duplicates not added to repository", EXPECTED_SIZE, opportunityRepository.getRepository().getValue().size());

    doThrow(Exception.class).when(politicianRepository).queryAllOpportunitiesMatchingParameters();
    opportunityRepository.addPoliticiansMatchingParameters(liveDataUserLocation, 1);
    assertEquals("Test repository unaffected if politicianRepository throws exception", EXPECTED_SIZE, opportunityRepository.getRepository().getValue().size());
  }

  @Test
  public void addMicroVolunteeringMatchingParameters() throws Exception {
    opportunityRepository.observeMicroVolunteering();

    String category = "";
    MutableLiveData liveDataCategory = new MutableLiveData();
    liveDataCategory.postValue(category);


    opportunityRepository.addMicroVolunteeringMatchingParameters(liveDataCategory, 1);
    assertEquals("Test new values added to politicianRepository are added to repository", MICRO_VOLUNTEERING_TITLE, opportunityRepository.getRepository().getValue().get(0).getTitle());


    doThrow(Exception.class).when(microVolunteeringRepository).queryAllOpportunitiesMatchingParameters();
    opportunityRepository.addMicroVolunteeringMatchingParameters(liveDataCategory, 1);
    assertEquals("Test repository unaffected if politicianRepository throws exception", 1, opportunityRepository.getRepository().getValue().size());

  }


  @Test
  public void setStartingRandomIndex() {
    int RANDOM_STARTING_POINT = 4;
    opportunityRepository.setStartingRandomIndex(RANDOM_STARTING_POINT);
    assertEquals("Test can set random starting index", RANDOM_STARTING_POINT, opportunityRepository.getStartingRandomIndex());

  }
}