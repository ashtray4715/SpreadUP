package com.ashtray.spreadup.feature_connected;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

public class FragmentConnected extends MyFragment {

    private TextView textViewForShowingConnectionStatus;
    private Fragment currentShowingFragment;

    public FragmentConnected() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_connected, container, false);

        DTManager.getInstance().setDtManagerConnCallBack(new DTManagerConnCallBackHandler());
        textViewForShowingConnectionStatus = v.findViewById(R.id.TextViewForShowingConnectionStatus);
        updateConnectionStatus();

        showSendDataFragment();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(true);
        myFragmentCallBacks.setActivityTitle(SpreadUpApplication.getInstance().getString(R.string.fragment_title_connected));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_for_connected_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            handleBackButtonPressed();
        }else if (item.getItemId() == R.id.navigation_send_file) {
            showSendDataFragment();
        } else if(item.getItemId() == R.id.navigation_receiver) {
            showReceivingStatusFragment();
        } else if(item.getItemId() == R.id.navigation_sender) {
            showSendingStatusFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
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
                fragmentTransaction.replace(R.id.RelativeLayoutForConnectedOptions, currentShowingFragment);
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
                fragmentTransaction.replace(R.id.RelativeLayoutForConnectedOptions, currentShowingFragment);
                fragmentTransaction.show(currentShowingFragment);
                fragmentTransaction.commit();
            });
        }
    }

    private void showSendDataFragment() {
        if(currentShowingFragment != null && currentShowingFragment instanceof FragmentSendData) {
            return;
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

}
