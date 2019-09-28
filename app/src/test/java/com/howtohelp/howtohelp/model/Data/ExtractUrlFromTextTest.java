package com.howtohelp.howtohelp.model.Data;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExtractUrlFromTextTest {

  private SocialIssueRepository socialIssueRepository;

  @BeforeEach
  public void setUp() {
    socialIssueRepository = new SocialIssueRepository();
  }

  @Test
  public void testExtractUrlFromText() {
    final String ARTICLE_URL_ONLY =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    final String ARTICLE_URL_AND_TEXT =
        "SOME more text,  https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate  and some more at the end ";

    Assert.assertEquals(
        ARTICLE_URL_ONLY, socialIssueRepository.extractUrlFromText(ARTICLE_URL_ONLY));
    Assert.assertEquals(
        ARTICLE_URL_ONLY, socialIssueRepository.extractUrlFromText(ARTICLE_URL_AND_TEXT));
  }
}
