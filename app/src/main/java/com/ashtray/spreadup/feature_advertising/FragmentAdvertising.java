package com.ashtray.spreadup.feature_advertising;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.SpreadUpApplication;
import com.ashtray.spreadup.entities.MyFragment;
import com.gobinda.DTManager;

public class FragmentAdvertising extends MyFragment {

    private TextView textViewForShowingAdvertisingStatus;

    public FragmentAdvertising() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_advertising, container, false);
        textViewForShowingAdvertisingStatus = v.findViewById(R.id.TextViewForShowingAdvertisingStatus);
        textViewForShowingAdvertisingStatus.setText(R.string.advertising_starting);

        DTManager.getInstance().setDtManagerConnCallBack(new DTManagerConnCallBackHandler());
        DTManager.getInstance().setDtManagerAdvertiserCallBack(new DTManagerAdvertisingCallBackHandler());
        new Handler().postDelayed(() -> DTManager.getInstance().startAdvertising("gobinda_ad"), 100);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(true);
        myFragmentCallBacks.setActivityTitle(SpreadUpApplication.getInstance().getString(R.string.fragment_title_advertising));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            handleBackButtonPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleBackButtonPressed() {
        new Handler().postDelayed(() -> DTManager.getInstance().stopAdvertising(), 100);
        myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_HOME);
        return true;
    }

    private class DTManagerAdvertisingCallBackHandler implements DTManager.DTManagerAdvertiserCallBack {

        @Override
        public void onStarted() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> textViewForShowingAdvertisingStatus.setText(R.string.advertising_started));
            }
        }

        @Override
        public void onStopped() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> textViewForShowingAdvertisingStatus.setText(R.string.advertising_stopped));
            }
        }

        @Override
        public void onStartFailed() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> textViewForShowingAdvertisingStatus.setText(R.string.advertising_failed_to_start));
            }
        }

        @Override
        public void onStoppedFailed() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> textViewForShowingAdvertisingStatus.setText(R.string.advertising_failed_to_stop));
            }
        }
    }

    private class DTManagerConnCallBackHandler implements DTManager.DTManagerConnCallBack {

        @Override
        public void onDisconnectDevice() {
            // ignore
        }

        @Override
        public void onConnectDevice() {
            myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_CONNECTED);
        }

        @Override
        public void onConnectionFailed() {
            // ignore
        }
    }
}
