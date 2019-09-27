package com.example.howtohelp.model.Data;

/**
 * Contains attributes that are common to all opportunity types
 */
public abstract class DatabaseOpportunity {
    // ATTRIBUTES
    /**
     * Opportunity title
     */
    private String title;

    /**
     * URL to opportunity web page
     */
    private String url;

    // CONSTRUCTORS

    /**
     * Instantiates a new Database opportunity.
     */
    public DatabaseOpportunity() {
    }

    /**
     * Instantiates a new Database opportunity.
     *
     * @param title the title of the opportunity
     * @param url   the url to the opportunity webpage
     */
    public DatabaseOpportunity(String title, String url) {
        this.title = title;
        this.url = url;
    }

    // METHODS
    public String toString() {
        return title;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    // GETTERS AND SETTERS
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
