package com.example.howtohelp;

import android.Manifest;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.example.howtohelp.model.Data.SocialIssue;
import com.example.howtohelp.model.Data.SocialIssueRepository;
import com.example.howtohelp.ui.OpportunitiesActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Intent.ACTION_SEND;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public final class SpeedOfClassificationTest {
  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class);

  @Test
  public void testSpeedOfClassification() throws Exception {
    final String ARTICLE_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";

    SocialIssueRepository socialIssueRepository = new SocialIssueRepository();
    long startTime = System.currentTimeMillis();
    String socialIssue = socialIssueRepository.returnSocialIssueForUrl(ARTICLE_URL);
    long endTime = System.currentTimeMillis();
    double timeTaken = (double) (endTime - startTime) / 1000;
    assertTrue(timeTaken < 3);
  }
}
