package com.example.howtohelp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.howtohelp.model.Data.SocialIssueRepository;
import com.example.howtohelp.model.Opportunity;
import com.example.howtohelp.model.OpportunityRepository;
import com.example.howtohelp.model.UserLocation;

import java.util.List;
import java.util.Objects;

/** ViewModel for storing opportunities data and handling data logic for activities. */
public class OpportunitiesViewModel extends ViewModel {

  /**
   * Single source of truth for opportunities data. Only its repository LiveData list should be made
   * available to UI
   */
  private OpportunityRepository opportunityRepository;

  /** Can classify a URL. */
  private SocialIssueRepository socialIssueRepository;

  /** The category of the article. */
  private MutableLiveData<String> category;

  /** Used to store location data */
  private MutableLiveData<UserLocation> userLocation;

  /**
   * LiveData list observed by the activity. ViewModel should not be dependent on the type of data
   * within list
   */
  private MutableLiveData<List<Opportunity>> opportunities;

  /** Has the SocialIssueRepository called addDonations */
  private boolean queriedDonations,
      /** ... addMicroVolunteering */
      queriedMicroVolunteering,
      /** ... addPoliticians */
      queriedPoliticians;

  /** Set to true by the activity when it inflates the list of opportunities */
  private boolean opportunitiesFragmentInitialized;

  /** Set as the category value when the URL sent to the viewmodel is not valid */
  private final String INVALID_URL_TEXT = "INVALID URL";

  /** Set as the category value when classification of article is ongoing */
  private static final String CATEGORY_CLASSIFICATION_IN_PROGRESS_TEXT = "Classifying Article";

  /**
   * Instantiates a new Opportunities view model. Sets all lists as empty ArrayKists. Sets all
   * boolean values to false. Sets category value to {@value
   * #CATEGORY_CLASSIFICATION_IN_PROGRESS_TEXT}
   */
  public OpportunitiesViewModel() {
    opportunityRepository = new OpportunityRepository();
    socialIssueRepository = new SocialIssueRepository();
    opportunities = opportunityRepository.getRepository();
    category = new MutableLiveData<>();
    category.setValue(CATEGORY_CLASSIFICATION_IN_PROGRESS_TEXT);
    userLocation = new MutableLiveData<>();
    queriedDonations = queriedMicroVolunteering = queriedPoliticians = false;
    opportunitiesFragmentInitialized = false;
  }

  /**
   * Checks if the category value is 'Other'
   *
   * @return true if category is not null and equals 'Other', false otherwise
   */
  public boolean categoryIsOther() {
    boolean other = false;
    if (!categoryIsNull()) other = category.getValue().equals("Other");
    return other;
  }

  /**
   * Checks if category is null
   *
   * @return true if category is null, fasle otherwise
   */
  public boolean categoryIsNull() {
    return category.getValue() == null;
  }

  /**
   * Checks if category value has been set to a valid social issue
   *
   * @return false if category is null, equal to classification in progress text, or equal to
   *     invalid url text. Otherwise returns true
   */
  public boolean classifiedArticle() {
    return !(categoryIsNull()
        || category.getValue().equals(CATEGORY_CLASSIFICATION_IN_PROGRESS_TEXT)
        || category.getValue().equals(INVALID_URL_TEXT));
  }

  /**
   * Checks if the ViewModel contains any opportunities
   *
   * @return true if opportunities is not null and its size is greater than zero, false otherwise
   */
  public boolean containsOpportunities() {
    boolean found = opportunities.getValue() != null;
    if (found) found = opportunities.getValue().size() > 0;

    return found;
  }

  /**
   * Checks if the activity should create the opportunities fragment. If it returns true, sets
   * opportunitiesFragmentInitialized to true
   *
   * @return true if the opportunities fragment has not already been initialized, the article has
   *     been classified, and opportunities contains data. Otherwise returns false.
   */
  public boolean shouldCreateOpportunitiesFragment() {
    boolean returnValue = false;
    if (classifiedArticle()
        && !(categoryIsOther()
            || opportunitiesFragmentInitialized
            || userLocation.getValue() == null)) {
      boolean atLeastOne = opportunities != null;
      if (atLeastOne) atLeastOne = Objects.requireNonNull(opportunities.getValue()).size() > 0;
      if (atLeastOne) {
        returnValue = true;
        opportunitiesFragmentInitialized = true;
      }
    }

    return returnValue;
  }

  /**
   * Resets all values to those set in constructor, except for userLocation. Used when the activity
   * is sent a new article
   */
  public void emptyData() {
    opportunityRepository = new OpportunityRepository();
    socialIssueRepository = new SocialIssueRepository();
    opportunities = opportunityRepository.getRepository();
    category = new MutableLiveData<>();
    category.setValue(CATEGORY_CLASSIFICATION_IN_PROGRESS_TEXT);
    queriedDonations = queriedMicroVolunteering = queriedPoliticians = false;
    opportunitiesFragmentInitialized = false;
  }

  /**
   * Checks if new opportunities should be added to the fragment
   *
   * @param sizeOfOpportunitiesInAdapter the size of opportunities in the view pager adapter
   * @return true if opportunitiesFragmentInitialized is true and the parameter int is greater than
   *     the size of opportunities in the ViewModel, false otherwise
   */
  public boolean shouldAddToFragment(int sizeOfOpportunitiesInAdapter) {
    return opportunitiesFragmentInitialized
        && Objects.requireNonNull(opportunities.getValue()).size() > 1
        && sizeOfOpportunitiesInAdapter != opportunities.getValue().size();
  }

  /**
   * Returns the element in opportunities at the parameter index as a livedata object. Used in
   * opportunities detail fragment to send the value to the layout value. Value should be wrapped in
   * LiveData to avoid creating a dependency on the object type
   *
   * @param index the index of opportunities
   * @return the element in opportunities at the parameter index as a livedata object
   */
  public LiveData<Opportunity> getOpportunityAtIndex(int index) {
    MutableLiveData<Opportunity> returnOpportunity = new MutableLiveData<>();
    returnOpportunity.setValue(Objects.requireNonNull(opportunities.getValue()).get(index));
    return returnOpportunity;
  }

  /**
   * Sets user location.
   *
   * @param country the country
   * @param subCountry the sub country
   * @param postCode the post code
   * @param latitude the latitude
   * @param longitude the longitude
   */
  public void setUserLocation(
      String country, String subCountry, String postCode, double latitude, double longitude) {
    userLocation.postValue(new UserLocation(country, subCountry, postCode, latitude, longitude));
  }

  /** Adds Politicians to the opportunities repository and sets queriedPoliticians to true */
  private void addPoliticians() {
    queriedPoliticians = true;
    opportunityRepository.addPoliticiansMatchingParameters(userLocation, 3);
  }

  /** Adds Donations to the opportunities repository and sets queriedDonations to true */
  private void addDonations() {
    queriedDonations = true;
    opportunityRepository.addAllDonationsMatchingParameters(userLocation, category);
  }

  /**
   * Adds MicroVolunteering opportunities to the opportunities repository and sets
   * queriedMicroVolunteering to true
   */
  private void addMicroVolunteering() {
    queriedMicroVolunteering = true;
    opportunityRepository.addMicroVolunteeringMatchingParameters(category, 100);
  }

  /**
   * Quires opportunities repository for all opportunity types that the viewmodel has not already
   * queried but has the requisite data to query
   */
  public void queryAppropriateData() {
    boolean categoryInitialized =
        category.getValue() != null
            && !category.getValue().equals(INVALID_URL_TEXT)
            && !category.getValue().equals(CATEGORY_CLASSIFICATION_IN_PROGRESS_TEXT);
    boolean userLocationInitialized = userLocation.getValue() != null;

    if (categoryInitialized && !queriedMicroVolunteering) addMicroVolunteering();
    if (userLocationInitialized && !queriedPoliticians) addPoliticians();
    if (categoryInitialized && userLocationInitialized && !queriedDonations) addDonations();
  }

  /**
   * Gets category.
   *
   * @return the category
   */
  public MutableLiveData<String> getCategory() {
    return category;
  }

  /**
   * Sets category bu classifying article at the parameter URL. Sets category as {@link
   * #INVALID_URL_TEXT} is parameter is not found to be a valid URL by the socialIssueRepository
   *
   * @param url the url
   */
  public void setCategory(String url) {
    if (socialIssueRepository == null) socialIssueRepository = new SocialIssueRepository();
    try {
      String socialIssue = socialIssueRepository.returnSocialIssueForUrl(url);
      if (!socialIssue.equals(socialIssueRepository.getFailedText()))
        category.postValue(socialIssue);
      else {
        category.postValue(INVALID_URL_TEXT);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    socialIssueRepository = null;
  }

  /**
   * Gets user location.
   *
   * @return the user location
   */
  public MutableLiveData<UserLocation> getUserLocation() {
    return userLocation;
  }

  /**
   * Gets opportunities.
   *
   * @return the opportunities
   */
  public MutableLiveData<List<Opportunity>> getOpportunities() {
    return opportunities;
  }

  /**
   * Sets the starting random index in the opportunities repository to the parameter int
   *
   * @param highestIndexVisitedByUser the highest index visited by the user in the viewpager
   *     opportunities list
   */
  public void setHighestIndexVisitedByUser(int highestIndexVisitedByUser) {
    opportunityRepository.setStartingRandomIndex(highestIndexVisitedByUser);
  }

  /**
   * Gets invalid url text.
   *
   * @return the invalid url text
   */
  public String getINVALID_URL_TEXT() {
    return INVALID_URL_TEXT;
  }

  /**
   * Sets detail fragment initialized.
   *
   * @param opportunitiesFragmentInitialized the detail fragment initialized
   */
  public void setOpportunitiesFragmentInitialized(boolean opportunitiesFragmentInitialized) {
    this.opportunitiesFragmentInitialized = opportunitiesFragmentInitialized;
  }
}
