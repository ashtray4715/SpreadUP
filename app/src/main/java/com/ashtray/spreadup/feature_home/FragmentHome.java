package com.ashtray.spreadup.feature_home;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;

public class FragmentHome extends MyFragment {

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }

    private void advertiseButtonPressed() {
        myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_ADVERTISING);
    }

    private void scanButtonPressed() {
        myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_SCANNING);
    }
}
