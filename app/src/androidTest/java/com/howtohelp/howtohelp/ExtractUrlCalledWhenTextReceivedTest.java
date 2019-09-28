package com.howtohelp.howtohelp;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import com.howtohelp.howtohelp.model.Data.SocialIssueRepository;
import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public final class ExtractUrlCalledWhenTextReceivedTest {

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class, true, true);

  @BeforeEach
  public void intentsInit() {
    Intents.init();
  }

  @AfterEach
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
