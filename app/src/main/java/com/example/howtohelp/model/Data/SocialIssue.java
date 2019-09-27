package com.example.howtohelp.model.Data;

/** A social issue that is stored in the Category table in the opportunities database*/
public class SocialIssue {


  /** Was the classification successful */
  // ATTRIBUTES
  private boolean valid;

  /** Title of social issue to be presented on screen */
  private String humanTitle;

  /** Social issue class output by the algorithm */
  private String wekaTitle;

  /** Instantiates a new Social issue. */
  public SocialIssue() {}

  /**
   * Instantiates a new Social issue.
   *
   * @param valid social issue is valid
   */
  public SocialIssue(boolean valid) {
        this.valid = valid;
    }

  /**
   * Was url successfully classified
   *
   * @return boolean value representing if social issue is valid
   */
  public boolean isValid() {
        return valid;
    }

  /**
   * Sets valid.
   *
   * @param valid the valid
   */
  public void setValid(boolean valid) {
        this.valid = valid;
    }

  /**
   * Gets human title.
   *
   * @return the human title
   */
  public String getHumanTitle() {
        return humanTitle;
    }

  /**
   * Sets human title.
   *
   * @param humanTitle the human title
   */
  public void setHumanTitle(String humanTitle) {
        this.humanTitle = humanTitle;
    }

  /**
   * Gets weka title.
   *
   * @return the weka title
   */
  public String getWekaTitle() {
        return wekaTitle;
    }

  /**
   * Sets weka title.
   *
   * @param wekaTitle the weka title
   */
  public void setWekaTitle(String wekaTitle) {
        this.wekaTitle = wekaTitle;
    }
}
