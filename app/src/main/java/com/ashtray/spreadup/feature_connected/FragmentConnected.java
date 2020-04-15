package com.ashtray.spreadup.feature_connected;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;
import com.gobinda.DTManager;

public class FragmentConnected extends MyFragment {

    private TextView textViewForShowingConnectionStatus;

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
        View v = inflater.inflate(R.layout.fragment_connected, container, false);
        textViewForShowingConnectionStatus = v.findViewById(R.id.TextViewForShowingConnectionStatus);
        String status = SpreadUpApplication.getInstance().getString(R.string.connection_status) + DTManager.getInstance().getConnectedClient().getName();
        textViewForShowingConnectionStatus.setText(status);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(true);
        myFragmentCallBacks.setActivityTitle(SpreadUpApplication.getInstance().getString(R.string.fragment_title_connected));
    }
}
