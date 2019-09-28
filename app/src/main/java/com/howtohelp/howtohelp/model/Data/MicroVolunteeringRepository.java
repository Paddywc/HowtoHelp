package com.howtohelp.howtohelp.model.Data;

/**
 * The interface Micro volunteering repository interface.
 */
interface MicroVolunteeringRepositoryInterface {
  /**
   * All subclasses of DatabaseOpportunityRepository should implement a setParameters method so that
   * the OpportunityRepository can set the parameters for the API query. Subclasses require
   * different parameters so setParameters should be part of an interface, not an abstract method of
   * DatabaseOpportunityRepository
   *
   * @param category   social issue category to filter results by
   * @param maxResults maximum amount of opportunities to return
   */
  void setParameters(String category, int maxResults);
}

/** Repository for MicroVolunteering objects */
public class MicroVolunteeringRepository extends DatabaseOpportunityRepository<MicroVolunteering>
        implements MicroVolunteeringRepositoryInterface {

  /**
   * API endpoint for MicroVolunteering table in opportunities database
   */
  private static final String API_ENDPOINT =
          "https://xn0tnkywz7.execute-api.us-east-2.amazonaws.com/Prototype/query_micro_volunteering";

  /**
   * Instantiates a new Micro volunteering repository.
   */
  public MicroVolunteeringRepository() {
    super();
  }

  public void setParameters(String category, int maxResults) {
    addParameter("Category", category);
    addParameter("Limit", "" + maxResults);
  }

  // METHODS
  void addTableCellToOpportunityObject(
          String tableColumnApiTitle, String cellValue, MicroVolunteering microVolunteering) {
    switch (tableColumnApiTitle) {
      case "Title":
        microVolunteering.setTitle(cellValue);
        break;
      case "Description":
        microVolunteering.setDescription(cellValue);
        break;
      case "Type":
        microVolunteering.setMicroType(cellValue);
        break;
      case "Url":
        microVolunteering.setUrl(cellValue);
        break;
    }
  }

  @Override
  public void queryAllOpportunitiesMatchingParameters() throws Exception {
    retrieveOpportunities(API_ENDPOINT);
  }

  @Override
  MicroVolunteering initializeOpportunitySubclassObject() {
    return new MicroVolunteering();
  }
}
