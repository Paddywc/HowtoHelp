package com.howtohelp.howtohelp.model.Data;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Utilises methods of RemoteDatabaseReader to query opportunities database and store responses in a
 * LiveData list. Subclasses specify the API endpoint (including parameters) and which key/values of
 * the API response to store. This class handles all other interactions with the opportunities
 * database
 *
 * @param <T> a subtype of DatabaseOpportunity.
 */
public abstract class DatabaseOpportunityRepository<T extends DatabaseOpportunity>
        extends RemoteDatabaseReader {
    // STATIC VALUES
    /**
     * Key for the API gateway to the opportunities database
     */
    static final String API_KEY = "gO08DyaFuP2MzZdsM08dh6tjCk5opxo57koiHnXW";

    /**
     * List of the object that is specified in the class declaration of a
     * DatabaseOpportunityRepository. This object type must be a subclass of DatabaseOpportunity
     */
    // ATTRIBUTES
    private MutableLiveData<List<T>> repository;

    /**
     * Parameters to be sent in the API query to the opportunities database, stored in the form.
     * Key/Value of HashMap elements are the keys and values of the API request parameters
     */
    private HashMap<String, String> parameters;

    /**
     * Instantiates a new Database opportunity repository. Initializes parameters and repository.
     */
    public DatabaseOpportunityRepository() {
        parameters = new HashMap<>();
        repository = new MutableLiveData<>();
        repository.postValue(new ArrayList<>());
    }

    /**
     * Add parameter to the API query
     *
     * @param key   the API parameter key
     * @param value the API parameter value
     */
    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    /**
     * If the tableColumnApiTitle equals an attribute of the opportunitySubtype, set that attribute
     * equal to cellValue. Implemented by a DatabaseOpportunityRepository subclass which identifies
     * the possible values for tableColumnApiTitle in the opportunities database table for the type of
     * opportunity (e.g. Donation) and matches these to that opportunity’s attributes
     *
     * @param tableColumnApiTitle alias for the column title that the API returned
     * @param cellValue           a value for a cell in the column
     * @param opportunitySubtype  the subtype of DataBase opportunity which the repository that
     *                            implements this method is response for
     */
    abstract void addTableCellToOpportunityObject(
            String tableColumnApiTitle, String cellValue, T opportunitySubtype);

    /**
     * Allows subclasses of DatabaseOpportunity to be initialized and added to the repository from
     * within this abstract class. This enables DatabaseOpportunityRepository to be responsible for
     * querying and storing functionality expect for setting the query parameters and reading the
     * values that are specific to each opportunity (addTableCellToOpportunityObject)
     *
     * @return an instance of the DatabaseOpportunity subclass that the implementing repository is
     * responsible for (e.g. Donation)
     */
    abstract T initializeOpportunitySubclassObject();

    /**
     * Calls retrieveOpportunities, passing in the API_ENDPOINT for the subclass's table
     *
     * @throws Exception the exception
     */
    public abstract void queryAllOpportunitiesMatchingParameters() throws Exception;

    /**
     * Queries the database using the parameters saved as attributes, then adds all returned data to
     * repository
     *
     * @param apiEndpoint API gateway endpoint, excluding parameters
     * @throws Exception
     */
    void retrieveOpportunities(String apiEndpoint) throws Exception {
        URL url = createFullGetUrl(apiEndpoint, parameters);
        HttpsURLConnection dbConnection = (HttpsURLConnection) url.openConnection();
        dbConnection.setRequestProperty("x-api-key", API_KEY);

        startReading(dbConnection);

        startReadingArray();
        while (!finishedReadingArray()) {
            repository.getValue().add(retrieveSingleOpportunity());
            repository.postValue(repository.getValue());
        }
        stopReadingArray();
        stopAllReading();

        dbConnection.disconnect();
    }

    /**
     * Retrieves single opportunity from the JSON array returned in API request
     *
     * @return the opportunity as the DatabaseOpportunity subtype that the implementing class is
     * responsible for
     * @throws IOException
     */
    private T retrieveSingleOpportunity() throws IOException {

        T opportunitySubtype = initializeOpportunitySubclassObject();
        startReadingJsonObject();
        String key, value;
        while (!finishedReadingObject()) {

            key = readJsonKey();
            value = readJsonValue();

            addTableCellToOpportunityObject(key, value, opportunitySubtype);
        }
        stopReadingJsonObject();
        return opportunitySubtype;
    }

    /**
     * Adds parameters to apiEndpoint and returns the full URL as a URL object
     *
     * @param apiEndpoint   the API endpoint declared in the implementing subclass
     * @param apiParameters the parameters to be included in the GET request
     * @return the full URL as a URL object
     * @throws MalformedURLException the malformed url exception
     */
    private URL createFullGetUrl(String apiEndpoint, HashMap<String, String> apiParameters)
            throws MalformedURLException {
        String urlAsString = apiEndpoint + "?";

        int count = 0;
        for (Map.Entry<String, String> apiParameter : apiParameters.entrySet()) {
            if (count > 0) urlAsString = urlAsString.concat("&");
            urlAsString = urlAsString.concat(apiParameter.getKey() + "=" + apiParameter.getValue());
            ++count;
        }
        return new URL(urlAsString);
    }

    // SETTERS AND GETTERS

    /**
     * Gets repository.
     *
     * @return the repository
     */
    public MutableLiveData<List<T>> getRepository() {
        return repository;
    }

    /**
     * Gets parameters.
     *
     * @return the parameters
     */
    HashMap<String, String> getParameters() {
        return parameters;
    }
}
