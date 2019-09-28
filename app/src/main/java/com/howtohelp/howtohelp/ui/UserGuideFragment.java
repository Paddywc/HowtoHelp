package com.howtohelp.howtohelp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.howtohelp.howtohelp.R;

/** Fragment for the user guide */
public class UserGuideFragment extends Fragment {

  /** The View pager. */
  private ViewPager viewPager;

  /** The Adapter. */
  private UserGuideAdapter adapter;

  /** The View model. */
  private OpportunitiesViewModel viewModel;

  /**
   * New instance user guide fragment.
   *
   * @return the user guide fragment
   */
  public static UserGuideFragment newInstance() {
    return new UserGuideFragment();
  }

  /** Instantiates a new User guide fragment. */
  public UserGuideFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(this.getActivity()).get(OpportunitiesViewModel.class);
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_user_guide, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Set ViewPager
    viewPager = view.findViewById(R.id.pager);
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    adapter = new UserGuideAdapter(getFragmentManager());
    viewPager.setAdapter(adapter);

    ImageButton faqButton = view.findViewById(R.id.faq_button);
    faqButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, FaqFragment.newInstance())
                .commitNow();
      }
    });

    // If the ViewModel contains opportunities, sets the back button to visible. If the user presses
    // this button, inflate the opportunities list
    if (viewModel.containsOpportunities()) {
      ImageButton backButton = view.findViewById(R.id.back_to_opportunities_button);
      backButton.setVisibility(View.VISIBLE);
      backButton.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getFragmentManager()
                  .beginTransaction()
                  .replace(R.id.container, OpportunitiesCollectionFragment.newInstance())
                  .commitNow();
            }
          });
    }
  }
}
