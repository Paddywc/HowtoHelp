package com.howtohelp.howtohelp;

import android.Manifest;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public final class AppearsAsShareTargetTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
      new IntentsTestRule<>(OpportunitiesActivity.class);

  @Test
  public void testAppearsAsShareTarget() {

    android.provider.Browser.sendString(
        intentsTestRule.getActivity().getApplicationContext(), "Test");
    UiDevice device = UiDevice.getInstance(getInstrumentation());
    UiObject howToHelpText = device.findObject(new UiSelector().textContains("HowtoHelp"));
    assertTrue(howToHelpText.exists());
  }
}
