package com.example.howtohelp;

import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.example.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public final class AskForLocationDataTest {

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class);

  @Test
  public void testAskForLocationData() {
    UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
    UiObject uiObject = uiDevice.findObject(new UiSelector().textContains("data"));
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertTrue(uiObject.exists());
  }
}
