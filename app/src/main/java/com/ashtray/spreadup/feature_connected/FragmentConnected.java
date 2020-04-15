package com.ashtray.spreadup.feature_connected;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;
import com.gobinda.DTConnectedClient;
import com.gobinda.DTManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentConnected extends MyFragment {

    private TextView textViewForShowingConnectionStatus;
    private Fragment currentShowingFragment;

    public FragmentConnected() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_connected, container, false);

        BottomNavigationView bottomNavigationView = v.findViewById(R.id.BottomNavigationViewForConnectedOptions);
        bottomNavigationView.setOnNavigationItemSelectedListener(new MyNavigationItemSelectedListener());

        DTManager.getInstance().setDtManagerConnCallBack(new DTManagerConnCallBackHandler());
        textViewForShowingConnectionStatus = v.findViewById(R.id.TextViewForShowingConnectionStatus);
        updateConnectionStatus();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(true);
        myFragmentCallBacks.setActivityTitle(SpreadUpApplication.getInstance().getString(R.string.fragment_title_connected));
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

    private boolean showReceivingStatusFragment() {
        if(currentShowingFragment != null && currentShowingFragment instanceof FragmentReceivingStatus) {
            return false;
        }
        if(getActivity() != null) {
            getActivity().runOnUiThread(()->{
                currentShowingFragment = new FragmentReceivingStatus();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.RelativeLayoutForConnectedOptions, currentShowingFragment);
                fragmentTransaction.show(currentShowingFragment);
                fragmentTransaction.commit();
            });
        }
        return true;
    }

    private boolean showSendingStatusFragment() {
        if(currentShowingFragment != null && currentShowingFragment instanceof FragmentSendingStatus) {
            return false;
        }
        if(getActivity() != null) {
            getActivity().runOnUiThread(()->{
                currentShowingFragment = new FragmentSendingStatus();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.RelativeLayoutForConnectedOptions, currentShowingFragment);
                fragmentTransaction.show(currentShowingFragment);
                fragmentTransaction.commit();
            });
        }
        return true;
    }

    private boolean showSendDataFragment() {
        if(currentShowingFragment != null && currentShowingFragment instanceof FragmentSendData) {
            return false;
        }
        if(getActivity() != null) {
            getActivity().runOnUiThread(()->{
                currentShowingFragment = new FragmentSendData();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.RelativeLayoutForConnectedOptions, currentShowingFragment);
                fragmentTransaction.show(currentShowingFragment);
                fragmentTransaction.commit();
            });
        }
        return true;
    }

    private void updateConnectionStatus() {
        DTConnectedClient connectedClient = DTManager.getInstance().getConnectedClient();
        if(getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if(connectedClient == null) { // disconnected
                    String status = SpreadUpApplication.getInstance().getString(R.string.connection_status_no);
                    textViewForShowingConnectionStatus.setText(status);
                    textViewForShowingConnectionStatus.setBackgroundResource(R.color.disconnected_color);
                } else { // connected
                    String status = SpreadUpApplication.getInstance().getString(R.string.connection_status_yes) + connectedClient.getName();
                    textViewForShowingConnectionStatus.setText(status);
                    textViewForShowingConnectionStatus.setBackgroundResource(R.color.connected_color);
                }
            });
        }
    }

    private class DTManagerConnCallBackHandler implements DTManager.DTManagerConnCallBack {

        @Override
        public void onDisconnectDevice() {
            updateConnectionStatus();
        }

        @Override
        public void onConnectDevice() {
            updateConnectionStatus();
        }

        @Override
        public void onConnectionFailed() {
            updateConnectionStatus();
        }
    }

    private class MyNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_send_file:
                    return showSendDataFragment();
                case R.id.navigation_receiver:
                    return showReceivingStatusFragment();
                case R.id.navigation_sender:
                    return showSendingStatusFragment();
            }
            return false;
        }
    }
}
