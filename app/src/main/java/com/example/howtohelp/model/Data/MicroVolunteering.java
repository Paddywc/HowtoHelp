package com.example.howtohelp.model.Data;

/**
 * MicroVolunteering object representing an entry in the MicroVolunteering table in the opportunities database
 */
public class MicroVolunteering extends DatabaseOpportunity {

  /**
   * Type of MicroVolunteering activity
   */
  private String microType;

  /**
   * Description of activity
   */
  private String description;

  /**
   * Instantiates a new MicroVolunteering object
   */
  public MicroVolunteering() {
  }

  /**
   * Gets micro type.
   *
   * @return the micro type
   */
  public String getMicroType() {
    return microType;
  }

  /**
   * Sets micro type.
   *
   * @param microType the micro type
   */
  public void setMicroType(String microType) {
    this.microType = microType;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
