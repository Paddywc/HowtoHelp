package com.example.howtohelp.model.Data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SocialIssueRepositoryTest {

  private class MockedSocialIssueRepository extends SocialIssueRepository {
    @Override
    protected InputStream getInputStream() {
      return this.getClass().getClassLoader().getResourceAsStream("FilteredClassifier1.model");
    }
  }

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void extractUrlFromText() {
    SocialIssueRepository socialIssueRepository = new SocialIssueRepository();
    final String ARTICLE_URL_ONLY =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    final String ARTICLE_URL_AND_TEXT =
        "SOME more text,  https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate  and some more at the end ";

    Assert.assertEquals(
        ARTICLE_URL_ONLY, socialIssueRepository.extractUrlFromText(ARTICLE_URL_ONLY));
    Assert.assertEquals(
        ARTICLE_URL_ONLY, socialIssueRepository.extractUrlFromText(ARTICLE_URL_AND_TEXT));
  }

  @Test
  public void scrapeArticle() {
    SocialIssueRepository socialIssueRepository = new SocialIssueRepository();
    final String GUARDIAN_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    final String GUARDIAN_CONTENT =
        "A group of rough sleepers who describe themselves as the parliamentary “night shift”, moving into an underpass leading to the parliamentary estate after MPs have left for the day, have been barred from accessing their relatively safe bed space underneath the street. The group of about 10 rough sleepers formed a distinct community with rules of conduct. They made themselves invisible from MPs and parliamentary workers and visitors during the day and only emerged after 11pm to lay out their cardboard, newspaper and sleeping bags in the exit 3 underpass from Westminster tube station to the parliamentary estate. None of the group were involved in begging, substance abuse or rowdy behaviour and those who joined had to abide by the group’s informal code of conduct, especially being willing to get up before 6am to vacate their nocturnal space before parliament’s daytime business begins and leaving the tunnel clean and tidy with no newspaper, cardboard or food and drink waste left behind.";

    final String IRISH_TIMES_URL =
        "https://www.irishtimes.com/news/ireland/irish-news/irish-man-cycles-across-us-i-got-to-see-a-completely-different-side-of-america-1.3988419";
    final String IRISH_TIMES_CONTENT =
        "When Pádraig O’Dea was learning to ride a bicycle in his home town of Tullamore, Co Offaly, more than 50 years ago, he never imagined that his ability to balance on two wheels would lead to a 4,500-mile odyssey across north America. On 5th June, the former prison officer (58), who now lives in Maynooth, commenced his cycling trip in Oregon on the Pacific northwest and on August 17th, he will complete the journey when he arrives in Port Washington on Long Island, New York. The idea for the trip came to him in 2013 when he was holidaying with his wife Julie, daughter Allison (24) and son Ronan (22) at the Grand Canyon. While taking a walk around their campsite one evening he met a French man who had a bike with a trailer attached.";

    final String DAILY_MAIL_URL =
        "https://www.dailymail.co.uk/news/article-7364551/Dominic-Grieve-says-not-facilitate-Corbyn-government-backlash-furious-Tories.html";
    final String DAILY_MAIL_CONTENT =
        "Tory Remainer rebels were in full retreat mode today after suffering a furious backlash from party colleagues over their willingness to discuss blocking a No Deal Brexit with Jeremy Corbyn. Former attorney general Dominic Grieve insisted he 'will not facilitate' making the hard Left Labour leader prime minister after he and three other pro-EU Conservatives were branded Mr Corbyn's 'useful idiots'. Mr Grieve, Sir Oliver Letwin and Dame Caroline Spelman sparked Brexiteer outrage and calls for them to face no confidence votes from their local parties over what they see as traitorous behaviour.";

    final String BUZZFEED_URL =
        "https://www.buzzfeednews.com/article/michaelblackmon/mulan-hong-kong-liu-yifei-crystal?ref=bfnsplash";
    final String BUZZFEED_CONTENT =
        "The outbursts Tuesday came on the heels of 10 weeks of sometimes violent protests initially sparked by an extradition bill that would allow the local government to transfer people accused of some crimes to mainland China. Since 1997, after it returned to China from the British, Hong Kong has been granted some political and legal freedoms that those on the mainland do not enjoy. Critics feared the new bill would erode this degree of independence.But what began as a demonstration against one bill has since ballooned into a massive pro-democracy movement. The authorities in Beijing have blamed, without evidence, Westerners for creating the unrest.Riot police have become a fixture on Hong Kong's streets, and some demonstrators have been severely injured by authorities. (Officials have said police have also been injured.)Liu's decision to wade into the controversy has been blasted by democracy campaigners and online supporters.The actor, who was born in China and later became a naturalized US citizen, has been criticized for living in a country where freedom of speech is celebrated while failing to support protesters fighting for the same rights.“Real ironic for a naturalized American citizen to shit on people fighting for freedom and democracy,” said one person who participated in the hashtag calling for a boycott of the actor’s upcoming film";

    String scrappedGuardian = socialIssueRepository.scrapeArticle(GUARDIAN_URL);
    assertTrue("Test scrape guardian", scrappedGuardian.contains(GUARDIAN_CONTENT));

    String scrappedIrishTimes = socialIssueRepository.scrapeArticle(IRISH_TIMES_URL);
    assertTrue("Test scrape Irish times", scrappedIrishTimes.contains(IRISH_TIMES_CONTENT));

    String scrappedDailyMail = socialIssueRepository.scrapeArticle(DAILY_MAIL_URL);
    assertTrue("Test scrape Daily Mail", scrappedDailyMail.contains(DAILY_MAIL_CONTENT));

    String scrappedBuzzFeed = socialIssueRepository.scrapeArticle(BUZZFEED_URL);
    assertTrue("Test scrape BuzzFeed", scrappedBuzzFeed.contains(BUZZFEED_CONTENT));
  }

  @Test
  public void getFailedText() {
    final String FAILED_TEXT = "Classification Failed";
    SocialIssueRepository socialIssueRepository = new SocialIssueRepository();
    assertEquals(
        "Test can get FAILED_TEXT class variable",
        FAILED_TEXT,
        socialIssueRepository.getFailedText());
  }

  @Test
  public void returnSocialIssueForUrl() throws Exception {

    final String invalidUrl = "wfrr";
    SocialIssueRepository socialIssueRepositoryOne = new MockedSocialIssueRepository();
    String invalidSocialIssue = socialIssueRepositoryOne.returnSocialIssueForUrl(invalidUrl);
    assertEquals(
        "Test invalid text returned URL sent",
        socialIssueRepositoryOne.getFailedText(),
        invalidSocialIssue);

    final String VALID_URL =
        "https://www.buzzfeednews.com/article/michaelblackmon/mulan-hong-kong-liu-yifei-crystal?ref=bfnsplash";
    SocialIssueRepository socialIssueRepositoryTwo = new MockedSocialIssueRepository();
    String validSocialIssue = socialIssueRepositoryTwo.returnSocialIssueForUrl(VALID_URL);
    assertNotEquals(
        "Test social issue does not return invalid text when valid url is sent",
        socialIssueRepositoryOne.getFailedText(),
        validSocialIssue);
  }
}
