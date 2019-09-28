package com.howtohelp.howtohelp.model;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.howtohelp.howtohelp.model.Data.DatabaseOpportunity;
import com.howtohelp.howtohelp.model.Data.DonationRepository;
import com.howtohelp.howtohelp.model.Data.MicroVolunteeringRepository;
import com.howtohelp.howtohelp.model.Data.PoliticianRepository;

import java.util.ArrayList;
import java.util.List;

/** The sole opportunity data structure to be used in the UI */
public class OpportunityRepository {

  /** List of opportunity objects */
  private MediatorLiveData<List<Opportunity>> repository;

  /**
   * Private DonationRepository attribute used to query the Donation table of the opportunities
   * database
   */
  private DonationRepository donationRepository;

  /**
   * Private PoliticianRepository attribute used to query the Politician table of the opportunities
   * database
   */
  private PoliticianRepository politicianRepository;

  /**
   * Private MicroVolunteeringRepository attribute used to query the MicroVolunteering table of the
   * opportunities database
   */
  private MicroVolunteeringRepository microVolunteeringRepository;

  /** Minimum index when inserting new entries into a random position in the repository */
  private int startingRandomIndex;

  /**
   * Instantiates all attributes. Observes changes made to donationRepository, politicianRepository,
   * and microVolunteeringRepository. Calls addElementsNotInRepository when notified of a change
   */
  public OpportunityRepository() {
    repository = new MediatorLiveData<>();
    repository.postValue(new ArrayList<>());
    donationRepository = new DonationRepository();
    politicianRepository = new PoliticianRepository();
    microVolunteeringRepository = new MicroVolunteeringRepository();
    startingRandomIndex = 1;

    observeDonations();
    observePoliticians();
    observeMicroVolunteering();
  }

  /** Calls addElementsNotInRepository whenever a new element is added to donationRepository */
  public void observeDonations() {
    repository.addSource(donationRepository.getRepository(), this::addElementsNotInRepository);
  }

  /** Calls addElementsNotInRepository whenever a new element is added to politicianRepository */
  public void observePoliticians() {
    repository.addSource(politicianRepository.getRepository(), this::addElementsNotInRepository);
  }

  /**
   * Calls addElementsNotInRepository whenever a new element is added to microVolunteeringRepository
   */
  public void observeMicroVolunteering() {
    repository.addSource(
        microVolunteeringRepository.getRepository(), this::addElementsNotInRepository);
  }

  /**
   * Checks if parameter DatabaseOpportunity already exists in the repository
   *
   * @param opportunity any subclass of DatabaseOpportunity
   * @return true if opportunity is found in repository, false otherwise
   */
  private boolean isInRepository(DatabaseOpportunity opportunity) {
    String opportunityTitle = opportunity.getTitle();

    boolean found = false;
    int repositorySize = repository.getValue().size();
    if (repositorySize > 0) {
      int index = repositorySize - 1;

      // Should loop through each element, rather than just the last element, as new elements could
      // be added to repository while method is being called
      while (!found & index >= 0) {
        if (repository.getValue().get(index).getTitle().equals(opportunityTitle)) found = true;
        else --index;
      }
    }

    return found;
  }

  /**
   * Gets repository index position to insert a new opportunity into
   *
   * @return a valid index position after startingRandomIndex
   */
  private int getInsertionPoint() {
    int insertionPoint = 0;
    int repositorySize = repository.getValue().size();
    if (repositorySize > 1) {
      insertionPoint =
          (int) (Math.random() * (repositorySize - startingRandomIndex)) + 1 + startingRandomIndex;
    } else if (repositorySize == 1) insertionPoint = 1;
    return insertionPoint;
  }

  /**
   * Adds parameter to repository
   *
   * @param <T> a subclass of DatabaseOpportunity
   * @param t an instance of T
   */
  private <T extends DatabaseOpportunity> void addToRepository(T t) {
    Opportunity asOpportunity = new Opportunity(t);
    repository.getValue().add(getInsertionPoint(), asOpportunity);
    // So that observers  are notified of the change
    repository.postValue(repository.getValue());
  }

  /**
   * Adds parameter to repository if it is not there already
   *
   * @param <T> subclass of DatabaseOpportunity
   * @param observedOpportunities opportunity to add
   */
  private <T extends DatabaseOpportunity> void addElementsNotInRepository(
      List<T> observedOpportunities) {

    int observedOpportunitiesSize = observedOpportunities.size();
    if (observedOpportunitiesSize > 0) {

      // Should loop through each element, rather than just the last element, as new elements could
      // be added to observedOpportunitiesSize while method is being called
      for (int i = 0; i < observedOpportunitiesSize; i++) {
        if (!isInRepository(observedOpportunities.get(i)))
          addToRepository(observedOpportunities.get(i));
      }
    }
  }

  /**
   * Add all Donation objects in the opportunities database that match the parameters matching
   * parameters.
   *
   * @param userLocation the user's location
   * @param category social issue category
   */
  public void addAllDonationsMatchingParameters(
      MutableLiveData<UserLocation> userLocation, MutableLiveData<String> category) {

    donationRepository.setParameters(category.getValue(), userLocation.getValue().getCountry());
    try {
      donationRepository.queryAllOpportunitiesMatchingParameters();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Add all Politician objects in the opportunities database that match the parameters matching
   * parameters.
   *
   * @param userLocation the user's location
   * @param maxResults max results to return
   */
  public void addPoliticiansMatchingParameters(
      MutableLiveData<UserLocation> userLocation, int maxResults) {

    politicianRepository.setParameters(
        userLocation.getValue().getCountry(),
        userLocation.getValue().getPostCode(),
        userLocation.getValue().getLatitude(),
        userLocation.getValue().getLongitude(),
        maxResults);
    try {
      politicianRepository.queryAllOpportunitiesMatchingParameters();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Add all MicroVolunteering objects in the opportunities database that match the parameters
   * matching parameters.
   *
   * @param category the opportunity category
   * @param maxResults maximum amount of results to return
   */
  public void addMicroVolunteeringMatchingParameters(
      MutableLiveData<String> category, int maxResults) {

    microVolunteeringRepository.setParameters(category.getValue(), maxResults);
    try {
      microVolunteeringRepository.queryAllOpportunitiesMatchingParameters();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets repository.
   *
   * @return the repository
   */
  public MutableLiveData<List<Opportunity>> getRepository() {
    return repository;
  }

  /**
   * Sets starting random index.
   *
   * @param startingRandomIndex the starting random index
   */
  public void setStartingRandomIndex(int startingRandomIndex) {
    if (startingRandomIndex > this.startingRandomIndex)
      this.startingRandomIndex = startingRandomIndex;
  }

  /**
   * Gets starting random index.
   *
   * @return the starting random index
   */
  int getStartingRandomIndex() {
    return startingRandomIndex;
  }
}
