package com.example.howtohelp.model.Data;

/**
 * Donation object representing an entry in the Donation table in the opportunities database
 */
public class Donation extends DatabaseOpportunity {

    /**
     * Description of the organization
     */
    // ATTRIBUTES
    private String description;

    /**
     * Instantiates a new Donation.
     */
    // CONSTRUCTOR
    public Donation() {
    }

    /**
     * Instantiates a new Donation.
     */
    public Donation(String title, String url, String description) {
        super(title, url);
        this.description = description;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    // SETTERS AND GETTERS
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
