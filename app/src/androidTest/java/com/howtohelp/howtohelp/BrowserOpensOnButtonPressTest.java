package com.howtohelp.howtohelp;

import android.Manifest;
import android.content.Intent;
import android.os.RemoteException;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.howtohelp.howtohelp.model.Opportunity;
import com.howtohelp.howtohelp.ui.OpportunitiesActivity;
import com.howtohelp.howtohelp.ui.OpportunitiesViewModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_SEND;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public final class BrowserOpensOnButtonPressTest {

  private List<Opportunity> opportunityList;
  private final String TEST_URL =
      "https://www.dubsimon.ie/donate/?gclid=EAIaIQobChMI37LFzcWM5AIVSrTtCh31TwXPEAAYASAAEgI1YfD_BwE&gclsrc=aw.ds";

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
      new IntentsTestRule<>(OpportunitiesActivity.class, true, false);

  @BeforeEach
  public void setOpportunityList() {
    Opportunity opportunityOne = new Opportunity();
    opportunityOne.setTitle("Test title");
    opportunityOne.setOpportunityType("Donation");
    opportunityOne.setDescription(
        "interdum velit laoreet id donec ultrices tincidunt arcu non sodales neque sodales ut etiam sit amet nisl purus in mollis nunc sed id semper risus in hendrerit gravida rutrum quisque non tellus orci ac auctor augue mauris augue neque gravida in fermentum et sollicitudin ac orci phasellus egestas tellus rutrum tellus pellentesque eu tincidunt tortor aliquam nulla facilisi cras fermentum odio eu feugiat pretium nibh ipsum consequat nisl vel pretium lectus quam id leo in vitae turpis massa sed elementum tempus egestas sed sed risus pretium quam vulputate dignissim suspendisse in est ante in nibh mauris cursus mattis molestie a\n");
    opportunityOne.setUrl(TEST_URL);

    // Need to to start displaying fragments
    Opportunity opportunityTwo = new Opportunity();
    opportunityTwo.setTitle("Second");
    opportunityTwo.setOpportunityType("Donation");
    opportunityTwo.setDescription(
        "odio ut sem nulla pharetra diam sit amet nisl suscipit adipiscing bibendum est ultricies integer quis auctor elit sed vulputate mi sit amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar sapien et ligula ullamcorper malesuada proin libero nunc consequat interdum varius sit amet mattis vulputate enim nulla aliquet porttitor lacus luctus accumsan tortor posuere ac ut consequat semper viverra\n");
    opportunityTwo.setUrl(
        "https://www.irishtimes.com/news/ireland/irish-news/irish-man-cycles-across-us-i-got-to-see-a-completely-different-side-of-america-1.3988419");

    opportunityList = new ArrayList<>();
    opportunityList.add(opportunityOne);
    opportunityList.add(opportunityTwo);
  }

  @Test
  public void testBrowserOpensOnButtonPress() {

    final String ARTICLE_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    Intent intent = new Intent();
    intent.setAction(ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, ARTICLE_URL);
    intentsTestRule.finishActivity();
    intentsTestRule.launchActivity(intent);

    OpportunitiesViewModel viewModel =
        ViewModelProviders.of(intentsTestRule.getActivity()).get(OpportunitiesViewModel.class);

    try {
      Thread.sleep(9000);
      viewModel.getOpportunities().postValue(opportunityList);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    UiDevice device = UiDevice.getInstance(getInstrumentation());
    try {
      device.wakeUp();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    UiObject donateButton = device.findObject(new UiSelector().clickable(true));
    try {
      donateButton.click();
    } catch (UiObjectNotFoundException e) {
      e.printStackTrace();
    }

    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    UiObject url = device.findObject(new UiSelector().textContains("GIVE ONCE"));
    assertTrue(url.exists());
  }
}
