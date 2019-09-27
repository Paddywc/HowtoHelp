package com.example.howtohelp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * FragmentStatePagerAdapter for the viewpager in the user guide. Used for the guide's steps as
 * swipeable cards
 */
public class FaqAdapter extends FragmentStatePagerAdapter {

  /** Amount of cards. Should always be three */
  private int count;

  /**
   * Instantiates a new User guide adapter. Sets count to 3
   *
   * @param fm FragmentManager of the user guide fragment
   */
  public FaqAdapter(FragmentManager fm) {
    super(fm);
    count = 6;
  }

  public int getCount() {
    return count;
  }

  // so that pages are updated dynamically
  @Override
  public int getItemPosition(Object item) {
    return POSITION_NONE;
  }

  /**
   * Instantiates and returns a userGuideCardFragment bundled with an integer representing its
   * position in the ViewPager
   *
   * @param position the position of the item (passed by default by the parent class)
   */
  public Fragment getItem(int position) {
    FaqCardFragment faqCardFragment = new FaqCardFragment();
    Bundle bundle = new Bundle();
    bundle.putInt("position", position);
    faqCardFragment.setArguments(bundle);

    return faqCardFragment;
  }
}
