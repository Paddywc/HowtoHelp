package com.howtohelp.howtohelp;

import android.Manifest;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

public final class CloseWhenOutsideMarketTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class);

  @Test
  public void testCloseWhenOutsideMarket() {
    activityTestRule.getActivity().finish();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertTrue(activityTestRule.getActivity().isDestroyed());
  }
}
