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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;
import com.gobinda.DTConnectedClient;
import com.gobinda.DTManager;

import java.util.Objects;

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
        if(DTManager.getInstance().getConnectedClient() != null) {
            this.showAlertDialogForGoingBackToHome(); // since client is connected
        } else { // since client is not connected so directly show the home fragment
            new Thread(() -> myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_HOME)).start();
        }
        return true;
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

    private void showAlertDialogForGoingBackToHome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Go back to Home!");
        builder.setMessage("Data transfer will be stopped and device will be disconnected?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            //TODO - have to stop data sending and receiving
            DTManager.getInstance().disconnect();
            dialog.dismiss();
            new Thread(() -> myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_HOME)).start();
        });
        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        AlertDialog alert2 = builder.create();
        alert2.show();
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
