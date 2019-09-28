package com.howtohelp.howtohelp;

import android.Manifest;
import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.howtohelp.howtohelp.model.Opportunity;
import com.howtohelp.howtohelp.ui.OpportunitiesActivity;
import com.howtohelp.howtohelp.ui.OpportunitiesViewModel;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static android.content.Intent.ACTION_SEND;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class QueryDatabaseWhenArticleClassifiedTest {

  @Rule
  public GrantPermissionRule permissionRule =
      GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

  @Rule
  public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
      new IntentsTestRule<>(OpportunitiesActivity.class, true, false);

  private boolean containsDonation() {
    boolean found = false;
    OpportunitiesViewModel viewModel =
        ViewModelProviders.of(intentsTestRule.getActivity()).get(OpportunitiesViewModel.class);
    List<Opportunity> opportunities = viewModel.getOpportunities().getValue();
    for (Opportunity opportunity : opportunities) {
      if (opportunity.getOpportunityType().equals("Donation")) {
        found = true;
        break;
      }
    }
    return found;
  }

  @Test
  public void testQueryDatabaseWhenArticleClassified() throws Exception {
    final String ARTICLE_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";

    Intent intent = new Intent();
    intent.setAction(ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, "invalid URL");
    intentsTestRule.launchActivity(intent);

    OpportunitiesViewModel viewModel =
        ViewModelProviders.of(intentsTestRule.getActivity()).get(OpportunitiesViewModel.class);

    int loops = 0;
    while (viewModel.getOpportunities().getValue().size() == 0 && loops < 10) {
      Thread.sleep(1000);
      ++loops;
    }
    assertFalse(containsDonation());

    viewModel.setCategory(ARTICLE_URL);
    int secondLoops = 0;
    while (!containsDonation() && secondLoops < 10) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {

      }
      ++secondLoops;
    }

    assertTrue(containsDonation());
  }
}
