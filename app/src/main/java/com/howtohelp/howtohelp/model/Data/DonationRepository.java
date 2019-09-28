package com.howtohelp.howtohelp.model.Data;

/**
 * The interface Donation repository interface.
 */
interface DonationRepositoryInterface {
  /**
   * All subclasses of DatabaseOpportunityRepository should implement a setParameters method so that
   * the OpportunityRepository can set the parameters for the API query. Subclasses require
   * different parameters so setParameters should be part of an interface, not an abstract method of
   * DatabaseOpportunityRepository
   *
   * @param category social issue category to filter results by
   * @param country  country to filter results by
   */
  void setParameters(String category, String country);
}

/** Repository for Donation objects */
public class DonationRepository extends DatabaseOpportunityRepository<Donation>
        implements DonationRepositoryInterface {

  /**
   * API endpoint for Donation table in opportunities database
   */
  private static final String API_ENDPOINT =
          "https://xn0tnkywz7.execute-api.us-east-2.amazonaws.com/Prototype/query_donations";

  /**
   * Instantiates a new Donation repository.
   */
  public DonationRepository() {
    super();
  }

  public void setParameters(String category, String country) {
    addParameter("Category", category);
    addParameter("Country", country);
  }

  // METHODS
  void addTableCellToOpportunityObject(
          String tableColumnApiTitle, String cellValue, Donation donation) {
    switch (tableColumnApiTitle) {
      case "Title":
        donation.setTitle(cellValue);
        break;
      case "Description":
        donation.setDescription(cellValue);
        break;
      case "URL":
        donation.setUrl(cellValue);
        break;
    }
  }

  @Override
  public void queryAllOpportunitiesMatchingParameters() throws Exception {
    retrieveOpportunities(API_ENDPOINT);
  }

  @Override
  Donation initializeOpportunitySubclassObject() {
    return new Donation();
  }
}
