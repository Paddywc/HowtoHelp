package com.howtohelp.howtohelp;

import android.Manifest;
import android.widget.VideoView;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

public final class TextBasedGuideTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public ActivityTestRule<OpportunitiesActivity> activityTestRule =
      new ActivityTestRule<>(OpportunitiesActivity.class);

  @Test
  public void testTextBasedGuide() {
    onView(is(instanceOf(VideoView.class))).check(doesNotExist());
  }
}
