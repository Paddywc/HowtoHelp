package com.howtohelp.howtohelp;

import android.Manifest;
import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;
import com.howtohelp.howtohelp.ui.OpportunitiesViewModel;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static android.content.Intent.ACTION_SEND;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public final class SpeedTillShownOnScreenTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
      new IntentsTestRule<>(OpportunitiesActivity.class, true, false);

  @Test
  public void testSpeedTillShownOnScreen() throws Exception {
    final String ARTICLE_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    Intent intent = new Intent();
    intent.setAction(ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, ARTICLE_URL);

    long startTime = System.currentTimeMillis();
    intentsTestRule.launchActivity(intent);

    UiDevice device = UiDevice.getInstance(getInstrumentation());
    OpportunitiesViewModel viewModel =
        ViewModelProviders.of(intentsTestRule.getActivity()).get(OpportunitiesViewModel.class);

    boolean opportunityOnScreen = false;

    int loops = 0;
    while (!opportunityOnScreen && loops < 50) {
      Thread.sleep(200);
      try {
        String opportunityTitle = viewModel.getOpportunities().getValue().get(0).getTitle();
        UiObject uiObject = device.findObject(new UiSelector().textContains(opportunityTitle));
        opportunityOnScreen = uiObject.exists();
      } catch (Exception ignored) {
      } finally {
        ++loops;
      }
    }

    long endTime = System.currentTimeMillis();
    double timeTaken = (double) (endTime - startTime) / 1000;
    assertTrue(timeTaken < 5);
  }
}
