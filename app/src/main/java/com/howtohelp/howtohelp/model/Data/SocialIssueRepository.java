package com.howtohelp.howtohelp.model.Data;

import android.content.res.AssetManager;

import com.howtohelp.howtohelp.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/** Repository for SocialIssue objects. Responsible for returning the social issue of an article */
public class SocialIssueRepository extends RemoteDatabaseReader {

  /** Opportunities database API key */
  private static final String API_KEY = "gO08DyaFuP2MzZdsM08dh6tjCk5opxo57koiHnXW";

  /** Endpoint that will return all social issues in the Category table of opportunities database */
  private static final String API_ENDPOINT =
      "https://xn0tnkywz7.execute-api.us-east-2.amazonaws.com/Prototype/get_all_categories";

  /** Set if the classification has failed */
  private static String FAILED_TEXT = "Classification Failed";

  /** The Repository. */
  private List<SocialIssue> repository;

  /** Instantiates a new Social issue repository. */
  public SocialIssueRepository() {
    repository = new ArrayList<>();
  }

  /**
   * Opens an input stream with the article classification algorithm stored in asssets
   *
   * @return input stream containing the article classification algorithm
   * @throws Exception
   */
  protected InputStream getInputStream() throws Exception {
    AssetManager assetManager = App.getContext().getApplicationContext().getAssets();
    return assetManager.open("FilteredClassifier.model");
  }

  /**
   * Retrieves the article classifier algorithm
   *
   * @return the article classifier as a Weka Classifier object
   * @throws Exception
   */
  private Classifier retrieveClassifier() throws Exception {
    InputStream inputStream = this.getInputStream();

    return (Classifier) weka.core.SerializationHelper.read(inputStream);
  }

  /**
   * Creates instances with a text value and a nominal class value. The possible class values are
   * the WekaTitle's of all entries in the Category table in the opportunities database
   *
   * @return the instances
   */
  private Instances createInstances() {
    Attribute text = new Attribute("text", true);

    List<String> possibleCategories = new ArrayList<>();
    for (SocialIssue socialIssue : repository) {
      possibleCategories.add(socialIssue.getWekaTitle());
    }

    Attribute category = new Attribute("class-att", possibleCategories);

    ArrayList<Attribute> attributes = new ArrayList<>(Arrays.asList(text, category));

    Instances instances = new Instances("articles", attributes, 1);
    instances.setClassIndex(1);
    return instances;
  }

  /**
   * Extract the first URL it finds within the parameter String
   *
   * @param textToExtractFrom the text to extract the URL from
   * @return the URL found in the parameter as a String. If no URL is found, returns FAILED_TEXT
   */
  // METHODS
  public String extractUrlFromText(String textToExtractFrom) {
    final String SPACE_REGEX = "\\s+";
    // Split by spaces
    String[] words = textToExtractFrom.split(SPACE_REGEX);
    boolean foundUrl = false;
    String url = FAILED_TEXT;

    for (int index = 0; index < words.length && !foundUrl; index++) {
      try {
        // will fail if not a valid url
        URL urlObject = new URL(words[index]);
        url = words[index];
        foundUrl = true;
      } catch (MalformedURLException ignored) {

      }
    }
    return url;
  }

  /**
   * Scrapes the article found at the parameter URL
   *
   * @param url URL of article
   * @return the article found at the URL. Will return {@value #FAILED_TEXT} if scraping fails
   */
  public String scrapeArticle(String url) {
    String articleContent;
    try {
      Document document = Jsoup.connect(url).get();
      articleContent = document.title() + " ";
      // Want to extract just the news article text
      // Attempt most common outermost DOM tag for news article text
      Elements linesOfText = document.getElementsByAttributeValue("itemprop", "articleBody");
      boolean identifiedText = linesOfText.size() > 0;
      if (!identifiedText) {
        // Attempt second most common
        linesOfText = document.getElementsByAttributeValue("property", "articleBody");
        identifiedText = linesOfText.size() > 0;
      }
      if (!identifiedText) {
        // Article tag is common, but will often include other content e.g. links to
        // related articles
        linesOfText = document.getElementsByTag("article");
        identifiedText = linesOfText.size() > 0;
      }
      // If all else fails, just get all paragraphs
      if (!identifiedText) {
        linesOfText = document.getElementsByTag("p");
      }

      for (Element textLine : linesOfText) {
        articleContent = articleContent.concat(textLine.text());
      }

    } catch (Exception e) {
      articleContent = FAILED_TEXT;
    }

    return articleContent;
  }

  /**
   * Classifies the parameter String as a social issue from the Category table in the opportunities
   * database
   *
   * @param textToClassify the text to classify
   * @return the social issue of the text as a SocialIssue object. The object's 'valid' attribute
   *     will be set to false if classification fails, true otherwise
   */
  private SocialIssue getSocialIssueOfText(String textToClassify) {
    SocialIssue socialIssue = new SocialIssue(false);

    try {

      Classifier classifier = retrieveClassifier();
      Instances instances = createInstances();

      double[] instance1Values = new double[instances.numAttributes()];
      // Second attribute remains null as class is unknown
      instance1Values[0] = instances.attribute(0).addStringValue(textToClassify);
      instances.add(new DenseInstance(1.0, instance1Values));

      double classificationAsDouble = classifier.classifyInstance(instances.firstInstance());
      String classificationAsWekaName =
          instances.classAttribute().value((int) classificationAsDouble);
      for (SocialIssue existingIssue : repository) {
        if (existingIssue.getWekaTitle().equals(classificationAsWekaName)) {
          socialIssue = existingIssue;
          socialIssue.setValid(true);
          break;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return socialIssue;
  }

  /**
   * Add all entires in the Category table of the opportunities database to the repository
   *
   * @throws IOException
   */
  private void addAllDataToRepository() throws IOException {

    URL url = new URL(API_ENDPOINT);
    HttpsURLConnection dbConnection = (HttpsURLConnection) url.openConnection();
    dbConnection.setRequestProperty("x-api-key", API_KEY);

    startReading(dbConnection);

    startReadingArray();
    while (!finishedReadingArray()) {
      startReadingJsonObject();
      SocialIssue socialIssue = new SocialIssue();
      while (!finishedReadingObject()) {
        String key = readJsonKey();
        if (key.equals("WekaTitle")) socialIssue.setWekaTitle(readJsonValue());
        else if (key.equals("HumanTitle")) socialIssue.setHumanTitle(readJsonValue());
      }
      stopReadingJsonObject();
      repository.add(socialIssue);
    }
    finishedReadingArray();
    stopAllReading();

    dbConnection.disconnect();
  }


  /**
   * Classifies the social issue of the parameter URL
   *
   * @param url the url of an article
   * @return the social issue of the article as a String. Will return {@value #FAILED_TEXT} if classification fails
   * @throws Exception
   */
  public String returnSocialIssueForUrl(String url) throws Exception {
    String socialIssueText = FAILED_TEXT;
    addAllDataToRepository();
    String extractedUrl = extractUrlFromText(url);
    if (!extractedUrl.equals(FAILED_TEXT)) {
      String articleText = scrapeArticle(extractedUrl);
      if (!articleText.equals(FAILED_TEXT)) {
        SocialIssue classifiedSocialIssue = getSocialIssueOfText(articleText);
        if (classifiedSocialIssue.isValid())
          socialIssueText = classifiedSocialIssue.getHumanTitle();
      }
    }

    return socialIssueText;
  }


    /**
     * Gets failed text.
     *
     * @return the failed text
     */
    public String getFailedText() {
        return FAILED_TEXT;
    }

}
