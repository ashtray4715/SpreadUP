package com.ashtray.spreadup.feature_scanning;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashtray.spreadup.R;
import com.ashtray.spreadup.entities.MyFragment;
import com.gobinda.DTDiscoveredClient;
import com.gobinda.DTManager;

import java.util.ArrayList;

public class FragmentScanning extends MyFragment {

    private TextView textViewForShowingScanningStatus;

    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private final Object lockForAccessingDiscoveredItemsArrayList = new Object();
    private ArrayList<DTDiscoveredClient> discoveredItems;

    public FragmentScanning() {
        // Required empty public constructor
    }

    @Override
    public boolean handleBackButtonPressed() {
        DTManager.getInstance().stopScanning();
        myFragmentCallBacks.showFragment(MyFragmentName.FRAGMENT_HOME);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scanning, container, false);
        textViewForShowingScanningStatus = v.findViewById(R.id.TextViewForShowingScanningStatus);
        textViewForShowingScanningStatus.setText(R.string.scanning_starting);

        RecyclerView recyclerViewForShowingDiscoveredItems = v.findViewById(R.id.RecyclerViewForShowingDiscoveredItems);
        recyclerViewForShowingDiscoveredItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        discoveredItems = new ArrayList<>(); // initially
        myRecyclerViewAdapter = new MyRecyclerViewAdapter();
        recyclerViewForShowingDiscoveredItems.setAdapter(myRecyclerViewAdapter);

        DTManager.getInstance().setDtManagerConnCallBack(new DTManagerConnCallBackHandler());
        DTManager.getInstance().setDtManagerScannerCallBack(new DTManagerScanningCallBackHandler());
        new Handler().postDelayed(() -> DTManager.getInstance().startScanning("gobinda_sc"), 100);

        return v;
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DiscoveredItemViewHolder> {

        @Override
        public int getItemCount() {
            return discoveredItems.size();
        }

        @NonNull
        @Override
        public DiscoveredItemViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_single_discovered_item, viewGroup, false);
            return new DiscoveredItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final DiscoveredItemViewHolder discoveredItemViewHolder, int i) {
            discoveredItemViewHolder.displayInfo(discoveredItems.get(i));
        }

        class DiscoveredItemViewHolder extends RecyclerView.ViewHolder{
            DTDiscoveredClient currentDiscoveredClient = null;

            private TextView textViewForShowingDiscoveredItemProfileName;
            private DiscoveredItemViewHolder(View itemView) {
                super(itemView);
                textViewForShowingDiscoveredItemProfileName = itemView.findViewById(R.id.TextViewForShowingDiscoveredItemProfileName);
                textViewForShowingDiscoveredItemProfileName.setOnClickListener(view -> DTManager.getInstance().connect(currentDiscoveredClient, "gobinda_sc"));
            }
            private void displayInfo(DTDiscoveredClient currentDiscoveredClient){
                this.currentDiscoveredClient = currentDiscoveredClient;
                textViewForShowingDiscoveredItemProfileName.setText(currentDiscoveredClient.getName());
            }
        }
    }

    private class DTManagerScanningCallBackHandler implements DTManager.DTManagerScannerCallBack {

        @Override
        public void onStarted() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> textViewForShowingScanningStatus.setText(R.string.scanning_started));
            }
        }

        @Override
        public void onStopped() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    textViewForShowingScanningStatus.setText(R.string.scanning_stopped);
                    synchronized (lockForAccessingDiscoveredItemsArrayList){
                        discoveredItems = new ArrayList<>();
                    }
                    myRecyclerViewAdapter.notifyDataSetChanged();
                });
            }
        }

        @Override
        public void onStartFailed() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> textViewForShowingScanningStatus.setText(R.string.scanning_failed_to_start));
            }
        }

        @Override
        public void onStoppedFailed() {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> textViewForShowingScanningStatus.setText(R.string.scanning_failed_to_stop));
            }
        }

        @Override
        public void onDiscover(DTDiscoveredClient dtDiscoveredClient) {
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    synchronized (lockForAccessingDiscoveredItemsArrayList){
                        discoveredItems.add(dtDiscoveredClient);
                    }
                    myRecyclerViewAdapter.notifyDataSetChanged();
                });
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
