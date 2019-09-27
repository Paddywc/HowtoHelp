package com.example.howtohelp;

import android.Manifest;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.howtohelp.model.Opportunity;
import com.example.howtohelp.ui.OpportunitiesActivity;
import com.example.howtohelp.ui.OpportunitiesViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.content.Intent.ACTION_SEND;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public final class SendDeviceLocationToDatabaseTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class);

  @Before
  public void intentsInit() {
    Intents.init();
  }

  @After
  public void intentsTeardown() {
    Intents.release();
  }

  @After
  public void setMockModeToFalse() {

    FusedLocationProviderClient fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            activityTestRule.getActivity().getApplicationContext());
    fusedLocationProviderClient.setMockMode(false);
  }

  private Opportunity getPoliticianOpportunity() {
    Opportunity returnOpportunity = null;
    OpportunitiesViewModel viewModel =
        ViewModelProviders.of(activityTestRule.getActivity()).get(OpportunitiesViewModel.class);
    List<Opportunity> currentOpportunities = viewModel.getOpportunities().getValue();
    for (Opportunity opportunity : currentOpportunities) {
      if (opportunity.getOpportunityType().equals("Politician")) {
        returnOpportunity = opportunity;
        break;
      }
    }
    return returnOpportunity;
  }

  @Test
  public void testSendDeviceLocationToDatabase() {
    final double DONEGAL_LAT = 54.82068665;
    final double DONEGAL_LONG = -8.526219348874058;
    final String DONEGAL_AREA_NAME = "Donegal";

    LocationManager locationManager =
        (LocationManager)
            activityTestRule
                .getActivity()
                .getSystemService(
                    activityTestRule.getActivity().getApplicationContext().LOCATION_SERVICE);

    // SET LOCATION
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_COARSE);

    Location mockLocation = new Location(locationManager.getBestProvider(criteria, true));
    mockLocation.setAccuracy(criteria.getAccuracy());
    mockLocation.setLatitude(DONEGAL_LAT);
    mockLocation.setLongitude(DONEGAL_LONG);
    mockLocation.setTime(System.currentTimeMillis());
    mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());

    FusedLocationProviderClient fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            activityTestRule.getActivity().getApplicationContext());
    fusedLocationProviderClient.setMockMode(true);
    fusedLocationProviderClient.setMockLocation(mockLocation);

    final String ARTICLE_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    Intent intent = new Intent();
    intent.setAction(ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, ARTICLE_URL);

    activityTestRule.getActivity().finish();
    activityTestRule.launchActivity(intent);

    Opportunity politicianOpportunity = getPoliticianOpportunity();
    int loops = 0;
    while (loops < 10 && politicianOpportunity == null) {
      try {
        Thread.sleep(1000);
        ++loops;
        politicianOpportunity = getPoliticianOpportunity();

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    assertNotNull(politicianOpportunity);
    assertEquals(DONEGAL_AREA_NAME, politicianOpportunity.getArea());
  }
}
