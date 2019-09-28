package com.howtohelp.howtohelp.model;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * Stores the attributes from an Android SDK Address object that HowtoHelp requires. Used as a
 * substitute for the Address class to prevent non-ui classes depending on the SDKD
 */
public class UserLocation {

  /** Country of user's current location. For users in the UK, will be country within the UK */
  // ATTRIBUTES
  private String country;

  /** Postcode of the user's current location */
  private String postCode;

  /** User's current latitude */
  private double latitude;

  /** Users current longitude */
  private double longitude;

  /**
   * Instantiates a new User location.
   *
   * @param country result of calling getCountryName on an Address object
   * @param subCountry result of calling getAdminArea on an Address object. If in the UK, will
   *     return country within the UK
   * @param postCode result of calling getPostalCode on an Address object
   * @param latitude result of calling getLatitude on an Address object
   * @param longitude result of calling getLongitude on an Address object
   */
  // CONSTRUCTORS
  public UserLocation(
      String country, String subCountry, String postCode, double latitude, double longitude) {
    try {
      this.country = returnCountryToSet(country, subCountry, postCode);
    } catch (Exception e) {
      this.country = country;
    }
    this.postCode = postCode;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  private String returnCountryToSet(String country, String subCountry, String postCode)
      throws IOException {
    String countryToSet = country;
    if (country.equals("United Kingdom")) {
      if (subCountry != null) countryToSet = subCountry;
      else {
        // Query postcodes.io API to get country in UK for postcode
        String apiEndpoint = "https://api.postcodes.io/postcodes/" + postCode;
        URL url = new URL(apiEndpoint);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        InputStream response = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(response, StandardCharsets.UTF_8);
        JsonReader jsonReader = new JsonReader(reader);

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
          if (jsonReader.nextName().equals("result")) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
              if (jsonReader.nextName().equals("country")) {
                countryToSet = jsonReader.nextString();
              } else {
                jsonReader.skipValue();
              }
            }

            jsonReader.endObject();
          } else {
            jsonReader.skipValue();
          }
        }
        jsonReader.endObject();

        jsonReader.close();
        reader.close();
        response.close();
      }
    }
    return countryToSet;
  }

  /**
   * Gets country.
   *
   * @return the country
   */
  // SETTERS AND GETTERS
  public String getCountry() {
    return country;
  }

  /**
   * Gets post code.
   *
   * @return the post code
   */
  public String getPostCode() {
    return postCode;
  }

  /**
   * Gets latitude.
   *
   * @return the latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Gets longitude.
   *
   * @return the longitude
   */
  public double getLongitude() {
    return longitude;
  }
}
