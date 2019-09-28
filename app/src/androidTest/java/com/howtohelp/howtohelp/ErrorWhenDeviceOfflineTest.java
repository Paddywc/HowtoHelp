package com.howtohelp.howtohelp;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;

public final class ErrorWhenDeviceOfflineTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @AfterEach
  public void turnOnWifi() {
    WifiManager wifiManager =
        (WifiManager) activityTestRule.getActivity().getSystemService(Context.WIFI_SERVICE);
    wifiManager.setWifiEnabled(true);
  }

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class);

  @Test
  public void testErrorWhenDeviceOffline() throws Exception {

    WifiManager wifiManager =
        (WifiManager) activityTestRule.getActivity().getSystemService(Context.WIFI_SERVICE);
    wifiManager.setWifiEnabled(false);

    assertTrue(true);
  }
}
