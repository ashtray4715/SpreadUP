package com.ashtray.spreadup.feature_connected;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;

public class FragmentConnected extends MyFragment {

    public FragmentConnected() {
        // Required empty public constructor
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }

    @Override
    public void handleMenuItemSelection(MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            handleBackButtonPressed();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connected, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(true);
        myFragmentCallBacks.setActivityTitle(SpreadUpApplication.getInstance().getString(R.string.fragment_title_connected));
    }
}
