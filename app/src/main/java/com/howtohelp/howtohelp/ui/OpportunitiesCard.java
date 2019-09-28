package com.howtohelp.howtohelp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.howtohelp.howtohelp.R;
import com.howtohelp.howtohelp.databinding.FragmentOpportunitiesCardBinding;

public class OpportunitiesCard extends Fragment {

    private OpportunitiesViewModel viewModel;

    public OpportunitiesCard() {
    }

    public static OpportunitiesCard newInstance() {
        return new OpportunitiesCard();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this.getActivity()).get(OpportunitiesViewModel.class);
        View view = getView();
        if (getArguments() != null) {
            int position = getArguments().getInt("position");

            FragmentOpportunitiesCardBinding binding =
                    DataBindingUtil.inflate(
                            inflater, R.layout.fragment_opportunities_card, container, false);
            view = binding.getRoot();
            view.setId(position);

            binding.setLifecycleOwner(this);
            binding.setOpportunity(viewModel.getOpportunityAtIndex(position));
        }

        return view;
    }
}
