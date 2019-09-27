package com.example.howtohelp;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import com.example.howtohelp.model.Data.SocialIssueRepository;
import com.example.howtohelp.ui.OpportunitiesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public final class ExtractUrlCalledWhenTextReceivedTest {

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class, true, true);

  @Before
  public void intentsInit() {
    Intents.init();
  }

  @After
  public void intentsTeardown() {
    Intents.release();
  }

  @Test
  public void testExtractUrlCalledWhenReturningSocialIssue() throws Exception {
    final String URL = "  https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    SocialIssueRepository socialIssueRepository = new SocialIssueRepository();
    SocialIssueRepository spy = spy(socialIssueRepository);
    spy.returnSocialIssueForUrl(URL);
    verify(spy, atLeastOnce()).extractUrlFromText(URL);
  }
}
