package com.howtohelp.howtohelp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.howtohelp.howtohelp.model.Opportunity;

import java.util.List;

public class OpportunitiesAdapter extends FragmentStatePagerAdapter {

    private List<Opportunity> opportunities;
    private int count;

    // from:
    // https://developer.android.com/guide/navigation/navigation-swipe-view#implement_swipe_views
    public OpportunitiesAdapter(FragmentManager fm) {
        super(fm);
        count = 0;
    }

    public void setOpportunities(List<Opportunity> opportunities) {
        this.opportunities = opportunities;
        notifyDataSetChanged();
        count = opportunities.size();
        notifyDataSetChanged();
    }

    public int getCount() {
        return count;
    }

    // so that pages are updated dynamically
    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }

    // from:
    // https://developer.android.com/guide/navigation/navigation-swipe-view#implement_swipe_views
    public Fragment getItem(int position) {
        OpportunitiesCard opportunitiesCard = new OpportunitiesCard();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        opportunitiesCard.setArguments(bundle);

        return opportunitiesCard;
    }
}
