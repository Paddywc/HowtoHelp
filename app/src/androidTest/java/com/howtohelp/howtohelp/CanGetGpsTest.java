package com.howtohelp.howtohelp;

import android.Manifest;
import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class CanGetGpsTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class);

  @Test
  public void canGetGpsTest() {
    boolean testRan = false;
    // Use  ActivityTestRule rule object to get activity
    Activity activity = activityTestRule.getActivity();

    FusedLocationProviderClient fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(activity);
    Task<LocationAvailability> locationAvailabilityTask =
        fusedLocationClient.getLocationAvailability();
    locationAvailabilityTask.addOnCompleteListener(
        task -> {
          LocationAvailability locationAvailability = task.getResult();
          assertTrue(locationAvailability.isLocationAvailable());
        });

    // Test only runs on success of getLocationAvailability()
    // Below is used to verify that test ran

  }
}
