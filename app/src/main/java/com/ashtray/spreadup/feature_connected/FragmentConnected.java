package com.ashtray.spreadup.feature_connected;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;
import com.gobinda.DTConnectedClient;
import com.gobinda.DTManager;

public class FragmentConnected extends MyFragment {

    private TextView textViewForShowingConnectionStatus;
    private Fragment currentShowingFragment;

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

    private void showReceivingStatusFragment() {
        if(currentShowingFragment != null && currentShowingFragment instanceof FragmentReceivingStatus) {
            return;
        }
        if(getActivity() != null) {
            getActivity().runOnUiThread(()->{
                currentShowingFragment = new FragmentReceivingStatus();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, currentShowingFragment);
                fragmentTransaction.show(currentShowingFragment);
                fragmentTransaction.commit();
            });
        }
    }

    private void showSendingStatusFragment() {
        if(currentShowingFragment != null && currentShowingFragment instanceof FragmentSendingStatus) {
            return;
        }
        if(getActivity() != null) {
            getActivity().runOnUiThread(()->{
                currentShowingFragment = new FragmentSendingStatus();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, currentShowingFragment);
                fragmentTransaction.show(currentShowingFragment);
                fragmentTransaction.commit();
            });
        }
    }

    private void showSendingFileFragment() {
        if(currentShowingFragment != null && currentShowingFragment instanceof FragmentSendData) {
            return;
        }
        if(getActivity() != null) {
            getActivity().runOnUiThread(()->{
                currentShowingFragment = new FragmentSendData();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, currentShowingFragment);
                fragmentTransaction.show(currentShowingFragment);
                fragmentTransaction.commit();
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_connected, container, false);

        DTManager.getInstance().setDtManagerConnCallBack(new DTManagerConnCallBackHandler());

        textViewForShowingConnectionStatus = v.findViewById(R.id.TextViewForShowingConnectionStatus);
        DTConnectedClient connectedClient = DTManager.getInstance().getConnectedClient();

        if(connectedClient != null) {
            String status = SpreadUpApplication.getInstance().getString(R.string.connection_status_yes) + connectedClient.getName();
            textViewForShowingConnectionStatus.setText(status);
            textViewForShowingConnectionStatus.setBackgroundResource(R.color.connected_color);
        } else {
            String status = SpreadUpApplication.getInstance().getString(R.string.connection_status_no);
            textViewForShowingConnectionStatus.setText(status);
            textViewForShowingConnectionStatus.setBackgroundResource(R.color.disconnected_color);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(true);
        myFragmentCallBacks.setActivityTitle(SpreadUpApplication.getInstance().getString(R.string.fragment_title_connected));
    }

    private class DTManagerConnCallBackHandler implements DTManager.DTManagerConnCallBack {

        @Override
        public void onDisconnectDevice() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    String status = SpreadUpApplication.getInstance().getString(R.string.connection_status_no);
                    textViewForShowingConnectionStatus.setText(status);
                    textViewForShowingConnectionStatus.setBackgroundResource(R.color.disconnected_color);
                });
            }
        }

        @Override
        public void onConnectDevice() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    DTConnectedClient connectedClient = DTManager.getInstance().getConnectedClient();
                    String status = SpreadUpApplication.getInstance().getString(R.string.connection_status_yes) + connectedClient.getName();
                    textViewForShowingConnectionStatus.setText(status);
                    textViewForShowingConnectionStatus.setBackgroundResource(R.color.connected_color);
                });
            }
        }

        @Override
        public void onConnectionFailed() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    String status = SpreadUpApplication.getInstance().getString(R.string.connection_status_failed);
                    textViewForShowingConnectionStatus.setText(status);
                    textViewForShowingConnectionStatus.setBackgroundResource(R.color.disconnected_color);
                });
            }
        }
    }
}
