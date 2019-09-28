package com.howtohelp.howtohelp.model.Data;

/**
 * Politician object representing an entry in the Politician table in the opportunities database
 */
public class Politician extends DatabaseOpportunity {

    /**
     * Constituency that the politician serves .
     */
    // ATTRIBUTES
    private String area;

    /**
     * Politician's twitter handle
     */
    private String twitter;

    /**
     * Politician's email address
     */
    private String email;

    /**
     * Politician's political party.
     */
    private String party;

    /**
     * Instantiates a new Politician.
     */
    public Politician() {
    }

    /**
     * Instantiates a new Politician.
     */
    public Politician(
            String title, String url, String area, String twitter, String email, String party) {
        super(title, url);
        this.area = area;
        this.twitter = twitter;
        this.email = email;
        this.party = party;
    }

    /**
     * Gets area.
     *
     * @return the area
     */
    // GETTERS AND SETTERS
    public String getArea() {
        return area;
    }

    /**
     * Sets area.
     *
     * @param area the area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * Gets twitter.
     *
     * @return the twitter
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * Sets twitter.
     *
     * @param twitter the twitter
     */
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets party.
     *
     * @return the party
     */
    public String getParty() {
        return party;
    }

    /**
     * Sets party.
     *
     * @param party the party
     */
    public void setParty(String party) {
        this.party = party;
    }
}
