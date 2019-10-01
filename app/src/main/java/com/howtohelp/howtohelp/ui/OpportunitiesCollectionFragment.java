package com.howtohelp.howtohelp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextSwitcher;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.howtohelp.howtohelp.R;

/** A simple {@link Fragment} subclass. */
public class OpportunitiesCollectionFragment extends Fragment {

  /** The View model. */
  private OpportunitiesViewModel viewModel;

  /** The Adapter. */
  private OpportunitiesAdapter adapter;

  /** The View pager. */
  private ViewPager viewPager;

  /** Instantiates a new Opportunities collection fragment. */
  public OpportunitiesCollectionFragment() {
    // Required empty public constructor
  }

  /**
   * New instance opportunities collection fragment.
   *
   * @return the opportunities collection fragment
   */
  public static OpportunitiesCollectionFragment newInstance() {
    return new OpportunitiesCollectionFragment();
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    viewModel = ViewModelProviders.of(this.getActivity()).get(OpportunitiesViewModel.class);

    return inflater.inflate(R.layout.fragment_opportunities_collection, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    viewModel
        .getOpportunities()
        .observe(
            this,
            opportunities ->
                AsyncTask.execute(
                    () -> {
                      if (viewModel.shouldCreateOpportunitiesFragment()) {
                        viewPager = view.findViewById(R.id.pager);
                        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                        adapter = new OpportunitiesAdapter(getFragmentManager());
                        adapter.setOpportunities(opportunities);
                        getActivity().runOnUiThread(() -> viewPager.setAdapter(adapter));
                      } else if (adapter != null
                          && viewModel.shouldAddToFragment(adapter.getCount())) {
                        getActivity().runOnUiThread(() -> adapter.setOpportunities(opportunities));
                        viewModel.setHighestIndexVisitedByUser(viewPager.getCurrentItem());
                      }
                    }));

    viewModel
        .getUserLocation()
        .observe(
            this,
            userLocation ->
                AsyncTask.execute(
                    () -> {
                      viewModel.queryAppropriateData();
                    }));

    viewModel
        .getCategory()
        .observe(
            this,
            new Observer<String>() {

              TextSwitcher toolbarTitle = view.findViewById(R.id.text_switcher);

              @Override
              public void onChanged(String s) {
                if (s.equals(viewModel.getINVALID_URL_TEXT())) {
                  invalidUrlAlertDialog();
                } else if (!(viewModel.classifiedArticle() || viewModel.categoryIsNull())) {
                  toolbarTitle.setText(s);
                } else if (viewModel.categoryIsOther()) {
                  viewModel.emptyData();
                  categoryIsOtherDialog();
                } else if (!viewModel.categoryIsNull()) {
                  toolbarTitle.setText("Help Combat " + s);
                  AsyncTask.execute(
                      () -> {
                        viewModel.queryAppropriateData();
                      });
                }
              }
            });

    ImageButton infoButton = view.findViewById(R.id.info_button);
    infoButton.setOnClickListener(
        v -> {
          viewModel.setOpportunitiesFragmentInitialized(false);
          getFragmentManager()
              .beginTransaction()
              .replace(R.id.container, UserGuideFragment.newInstance())
              .commitNow();
        });
  }

  /**
   * Invalid url alert dialog alert dialog.
   *
   * @return the alert dialog
   */
  private AlertDialog invalidUrlAlertDialog() {
    return new AlertDialog.Builder(getContext())
        .setTitle(getResources().getString(R.string.invalid_url_title))
        .setMessage(getResources().getString(R.string.invalid_url_body))
        .setCancelable(false)
        .setNegativeButton(
            "View Guide",
            (dialog, which) -> {
              viewModel.emptyData();
              getFragmentManager()
                  .beginTransaction()
                  .replace(R.id.container, UserGuideFragment.newInstance())
                  .commitNow();
            })
        .setPositiveButton(
            "Close HowtoHelp", (dialog, which) -> getActivity().finishAndRemoveTask())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
  }

  /**
   * Category is other dialog alert dialog.
   *
   * @return the alert dialog
   */
  private AlertDialog categoryIsOtherDialog() {
    return new AlertDialog.Builder(getContext())
        .setTitle(R.string.other_dialog_title)
        .setMessage(R.string.other_dialog_body)
        .setCancelable(false)
        .setNegativeButton(
            "View Guide",
            (dialog, which) -> {
              viewModel.emptyData();
              getFragmentManager()
                  .beginTransaction()
                  .replace(R.id.container, UserGuideFragment.newInstance())
                  .commitNow();
            })
        .setPositiveButton(
            "Close HowtoHelp", (dialog, which) -> getActivity().finishAndRemoveTask())

        // A null listener allows the button to dismiss the dialog and take no further
        // action.
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
  }
}
