package com.howtohelp.howtohelp;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.howtohelp.howtohelp.ui.OpportunitiesActivity;
import com.howtohelp.howtohelp.ui.OpportunitiesViewModel;

import org.junit.Rule;
import org.junit.Test;

import static android.content.Intent.ACTION_SEND;
import static org.junit.Assert.assertEquals;

public final class ClassifySentUrlTest {

  @Rule
  public IntentsTestRule<OpportunitiesActivity> intentsTestRule =
      new IntentsTestRule<>(OpportunitiesActivity.class, true, false);

  @Test
  public void classifySentText() {
    final String ARTICLE_URL =
        "https://www.theguardian.com/uk-news/2019/aug/11/westminster-rough-sleepers-evicted-from-parliamentary-estate";
    final String EXPECTED_CATEGORY = "Homelessness";
    Intent intent = new Intent();
    intent.setAction(ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, ARTICLE_URL);
    intentsTestRule.launchActivity(intent);

    OpportunitiesViewModel viewModel =
        ViewModelProviders.of(intentsTestRule.getActivity()).get(OpportunitiesViewModel.class);

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(EXPECTED_CATEGORY, viewModel.getCategory().getValue());
  }
}
