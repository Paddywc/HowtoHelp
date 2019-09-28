package com.howtohelp.howtohelp.model;

import com.howtohelp.howtohelp.model.Data.DatabaseOpportunity;
import com.howtohelp.howtohelp.model.Data.Donation;
import com.howtohelp.howtohelp.model.Data.MicroVolunteering;
import com.howtohelp.howtohelp.model.Data.Politician;

/**
 * The type Opportunity.
 */
public class Opportunity {
    /**
     * The opportunity subclass
     */
    private String opportunityType;

    /**
     * Title of opportunity.
     */
    private String title;

    /**
     * URL for opportunity webpage.
     */
    private String url;

    /**
     * Description of opportunity
     */
    private String description;

    /**
     * Type of MicroVolunteering activity
     */
    private String microVolunteeringType;

    /**
     * The politician's party
     */
    private String party;

    /**
     * The Politician's constituency
     */
    private String area;

    /**
     * The Politician's email address
     */
    private String email;

    /**
     * The Politician's twitter handle
     */
    private String twitter;

    /**
     * Instantiates a new Opportunity.
     */
    public Opportunity() {
    }

    /**
     * Instantiates a new Opportunity. Depending on the type of opportunity passed as a parameter,
     * will extract the relevant values as store them as attributes
     *
     * @param t an instance of a DatabaseOpportunity subclass
     */
    public <T extends DatabaseOpportunity> Opportunity(T t) {
        if (t instanceof Politician) setAppropriateValues((Politician) t);
        else if (t instanceof Donation) setAppropriateValues((Donation) t);
        else setAppropriateValues((MicroVolunteering) t);
    }

    /**
     * Instantiates a new Opportunity with the relevant attributes of the parameter
     *
     * @param donation the donation
     */
    private void setAppropriateValues(Donation donation) {
        opportunityType = "Donation";
        title = donation.getTitle();
        url = donation.getUrl();
        description = donation.getDescription();
    }

    /**
     * Instantiates a new Opportunity with the relevant attributes of the parameter
     *
     * @param politician the politician
     */
    private void setAppropriateValues(Politician politician) {
        opportunityType = "Politician";
        title = politician.getTitle();
        // to avoid null pointer exception when checking if duplicate in repository
        url = title;
        email = politician.getEmail();
        twitter = politician.getTwitter();
        area = politician.getArea();
        party = politician.getParty();
    }

    /**
     * Instantiates a new Opportunity with the relevant attributes of the parameter
     *
     * @param microVolunteering the micro volunteering
     */
    private void setAppropriateValues(MicroVolunteering microVolunteering) {
        opportunityType = "Micro Volunteering";
        title = microVolunteering.getTitle();
        url = microVolunteering.getUrl();
        description = microVolunteering.getDescription();
        microVolunteeringType = microVolunteering.getMicroType();
    }

    /**
     * Gets opportunity type.
     *
     * @return the opportunity type
     */
    public String getOpportunityType() {
        return opportunityType;
    }

    /**
     * Sets opportunity type.
     *
     * @param opportunityType the opportunity type
     */
    public void setOpportunityType(String opportunityType) {
        this.opportunityType = opportunityType;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
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

    /**
     * Gets micro volunteering type.
     *
     * @return the micro volunteering type
     */
    public String getMicroVolunteeringType() {
        return microVolunteeringType;
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
     * Gets area.
     *
     * @return the area
     */
    public String getArea() {
        return area;
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
     * Gets twitter.
     *
     * @return the twitter
     */
    public String getTwitter() {
        return twitter;
    }

    public String toString() {
        return this.opportunityType + ":" + this.title;
    }
}
