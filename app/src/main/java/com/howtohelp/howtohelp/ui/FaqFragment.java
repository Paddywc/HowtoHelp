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
public class FaqFragment extends Fragment {

  /** The View pager. */
  private ViewPager viewPager;

  /** The Adapter. */
  private FaqAdapter adapter;

  /** The View model. */
  private OpportunitiesViewModel viewModel;

  /**
   * New instance user guide fragment.
   *
   * @return the user guide fragment
   */
  public static FaqFragment newInstance() {
    return new FaqFragment();
  }

  /** Instantiates a new FAQ fragment. */
  public FaqFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(this.getActivity()).get(OpportunitiesViewModel.class);
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_faq, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Set ViewPager
    viewPager = view.findViewById(R.id.pager);
    // vertically  assigns content
//    viewPager.layout
    viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    adapter = new FaqAdapter(getFragmentManager());
    viewPager.setAdapter(adapter);

    ImageButton backButton = view.findViewById(R.id.back_to_user_guide_button);
    backButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, UserGuideFragment.newInstance())
                .commitNow();
          }
        });
  }
}
