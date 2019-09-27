package com.example.howtohelp;

import android.Manifest;
import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.example.howtohelp.model.Opportunity;
import com.example.howtohelp.ui.OpportunitiesActivity;
import com.example.howtohelp.ui.OpportunitiesViewModel;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;
import static androidx.test.espresso.Espresso.*;

import java.util.List;

import static android.content.Intent.ACTION_SEND;

public final class DisplayQueryResultsOnScreenTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
      new IntentsTestRule<>(OpportunitiesActivity.class, true, false);

  @Test
  public void testDisplayQueryResultsOnScreen() {
    final String ARTICLE_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    Intent intent = new Intent();
    intent.setAction(ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, ARTICLE_URL);
    intentsTestRule.launchActivity(intent);

    OpportunitiesViewModel viewModel =
        ViewModelProviders.of(intentsTestRule.getActivity()).get(OpportunitiesViewModel.class);

    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    List<Opportunity> opportunities;
    opportunities = viewModel.getOpportunities().getValue();
    // ensure that opportunities are queried
    assertTrue(opportunities.size() > 0);
    UiDevice device = UiDevice.getInstance(getInstrumentation());

    for (Opportunity opportunity : opportunities) {
      UiObject uiObject = device.findObject(new UiSelector().textContains(opportunity.getTitle()));
      assertTrue(uiObject.exists());
      onView(withId(R.id.container)).perform(swipeLeft());
    }
  }
}
