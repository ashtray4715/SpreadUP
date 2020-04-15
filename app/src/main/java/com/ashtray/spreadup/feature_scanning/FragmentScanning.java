package com.ashtray.spreadup.feature_scanning;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.entities.MyFragment;

public class FragmentScanning extends MyFragment {

    public FragmentScanning() {
        // Required empty public constructor
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanning, container, false);
    }
}
