package com.howtohelp.howtohelp;

import android.Manifest;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.model.Data.SocialIssueRepository;
import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

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
