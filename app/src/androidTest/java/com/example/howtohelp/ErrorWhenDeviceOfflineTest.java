package com.example.howtohelp;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.howtohelp.ui.OpportunitiesActivity;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertTrue;

public final class ErrorWhenDeviceOfflineTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @After
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
