package com.howtohelp.howtohelp;

import android.Manifest;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static android.content.Intent.ACTION_SEND;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public final class DisplayErrorOnInvalidUrl {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
      new IntentsTestRule<>(OpportunitiesActivity.class, true, false);

  @Test
  public void displayErrorOnInvalidUrlTest() {
    final String INVALID_URL_TEXT =
        "htps://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";

    Intent intent = new Intent();
    intent.setAction(ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, INVALID_URL_TEXT);
    intentsTestRule.launchActivity(intent);
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    onView(withText("CLOSE")).inRoot(isDialog()).check(matches(isDisplayed()));
  }
}
