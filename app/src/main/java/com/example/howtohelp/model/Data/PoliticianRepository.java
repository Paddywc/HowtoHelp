package com.example.howtohelp.model.Data;

/** The interface Politician repository interface. */
interface PoliticianRepositoryInterface {
  /**
   * All subclasses of DatabaseOpportunityRepository should implement a setParameters method so that
   * the OpportunityRepository can set the parameters for the API query. Subclasses require
   * different parameters so setParameters should be part of an interface, not an abstract method of
   * DatabaseOpportunityRepository
   *
   * @param country user country, determines which API endpoint to query
   * @param postCode user post code, determines UK constituency
   * @param latitude user latitude, determines Irish constituency
   * @param longitude user longitude user latitude, determines Irish constituency
   * @param maxResults the max results to be returned from the opportunities database
   */
  void setParameters(
      String country, String postCode, double latitude, double longitude, int maxResults);
}

/** Repository for Politician objects */
public class PoliticianRepository extends DatabaseOpportunityRepository<Politician>
    implements PoliticianRepositoryInterface {

  /**
   * API endpoint for triggering lambda function that queries UK politicians from the opportunities
   * database
   */
  private static final String UK_API_ENDPOINT =
      "https://xn0tnkywz7.execute-api.us-east-2.amazonaws.com/Prototype/get_one_uk_politician";

  /**
   * API endpoint for triggering lambda function that queries Irish politicians from the
   * opportunities database
   */
  private static final String IRE_API_ENDPOINT =
      "https://xn0tnkywz7.execute-api.us-east-2.amazonaws.com/Prototype/get_one_ire_politician";

  /**
   * API endpoint for triggering lambda function that queries Canadian politicians from the
   * opportunities database
   */
  private static final String CAN_API_ENDPOINT =
      "https://xn0tnkywz7.execute-api.us-east-2.amazonaws.com/Prototype/get_canada_politician";

  /**
   * API endpoint for triggering lambda function that queries US politicians from the opportunities
   * database
   */
  private static final String US_API_ENDPOINT =
      "https://xn0tnkywz7.execute-api.us-east-2.amazonaws.com/Prototype/get_us_pol";

  /** Instantiates a new Politician repository. */
  public PoliticianRepository() {
    super();
  }

  @Override
  public void queryAllOpportunitiesMatchingParameters() throws Exception {
    if (getParameters().get("Country").equals("Ireland")) retrieveOpportunities(IRE_API_ENDPOINT);
    else if (getParameters().get("Country").equals("Canada"))
      retrieveOpportunities(CAN_API_ENDPOINT);
    else if (getParameters().get("Country").equals("United States"))
      retrieveOpportunities(US_API_ENDPOINT);
    else retrieveOpportunities(UK_API_ENDPOINT);
  }

  void addTableCellToOpportunityObject(
      String tableColumnApiTitle, String cellValue, Politician politician) {
    switch (tableColumnApiTitle) {
      case "Name":
        politician.setTitle(cellValue);
        break;
      case "Area":
        politician.setArea(cellValue);
        break;
      case "Twitter":
        politician.setTwitter(cellValue);
        break;
      case "Party":
        politician.setParty(cellValue);
        break;
      case "Email":
        politician.setEmail(cellValue);
        break;
    }
  }

  Politician initializeOpportunitySubclassObject() {
    return new Politician();
  }

  public void setParameters(
      String country, String postCode, double latitude, double longitude, int maxResults) {

    addParameter("Limit", "" + maxResults);
    addParameter("Country", country);

    // Lambda functions determine Irish or Canadian consistency via latitude and longitude, UK
    // constituency via postcode
    if (country.equals("Ireland") || country.equals("Canada") || country.equals("United States")) {
      addParameter("Latitude", "" + latitude);
      addParameter("Longitude", "" + longitude);
      if (country.equals("United States")) addParameter("Postcode", postCode);
    } else {
      addParameter("Postcode", postCode);
    }
  }
}
