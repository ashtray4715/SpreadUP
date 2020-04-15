package com.ashtray.spreadup.feature_home;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;

public class FragmentHome extends MyFragment {

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }

    @Override
    public void handleMenuItemSelection(MenuItem menuItem) {
        // there is no back button in the home fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        v.findViewById(R.id.AdvertiseButtonForShowingAdvertiseFragment).setOnClickListener(v1 -> advertiseButtonPressed());
        v.findViewById(R.id.ScanButtonForShowingScanningFragment).setOnClickListener(v1 -> scanButtonPressed());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(false);
        myFragmentCallBacks.setActivityTitle(SpreadUpApplication.getInstance().getString(R.string.fragment_title_home));
    }

    private void advertiseButtonPressed() {
        myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_ADVERTISING);
    }

    private void scanButtonPressed() {
        myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_SCANNING);
    }
}
