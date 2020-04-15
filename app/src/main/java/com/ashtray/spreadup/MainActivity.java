package com.ashtray.spreadup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.ashtray.spreadup.entities.MyFragment;
import com.ashtray.spreadup.feature_advertising.FragmentAdvertising;
import com.ashtray.spreadup.feature_connected.FragmentConnected;
import com.ashtray.spreadup.feature_home.FragmentHome;
import com.ashtray.spreadup.feature_scanning.FragmentScanning;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements MyFragment.MyFragmentCallBacks {

    private AdView myAdView;
    private MyFragment myFragmentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAdView = findViewById(R.id.adView);
        myAdView.setVisibility(View.GONE);

        showFragment(MyFragment.MyFragmentName.FRAGMENT_HOME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //since app is in pause state so close ads but in different thread so that user don't see anything
        new Handler().postDelayed(this::closeAds, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Initialize mobile ads after 1 second
        new Handler().postDelayed(this::loadAds, 100);
    }

    private void loadAds() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (!SpreadUpApplication.getInstance().isInternetAvailable()) {
                return;
            }

            if (!SpreadUpApplication.getInstance().isMobileAdsInitialized()) {
                return;
            }

            AdRequest adRequest = new AdRequest.Builder().addTestDevice("F8E6FE3BFDB10141A96AE737E9E9DBF1").build();
            //AdRequest adRequest = new AdRequest.Builder().build();
            myAdView.loadAd(adRequest);
            myAdView.setVisibility(View.VISIBLE);
        });
    }

    private void closeAds() {
        if (myAdView != null) {
            myAdView.destroy();
            myAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!myFragmentObject.handleBackButtonPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void showFragment(MyFragment.MyFragmentName myFragmentName) {
        switch (myFragmentName) {
            case FRAGMENT_HOME:
                myFragmentObject = new FragmentHome();
                break;
            case FRAGMENT_ADVERTISING:
                myFragmentObject = new FragmentAdvertising();
                break;
            case FRAGMENT_SCANNING:
                myFragmentObject = new FragmentScanning();
                break;
            case FRAGMENT_CONNECTED:
                myFragmentObject = new FragmentConnected();
                break;
        }

        runOnUiThread(()->{
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, myFragmentObject);
            fragmentTransaction.show(myFragmentObject);
            fragmentTransaction.commit();
        });
    }

    @Override
    public void showToastMessage(String message, boolean forShortTime) {
        runOnUiThread(() -> Toast.makeText(this, message, (forShortTime)? Toast.LENGTH_SHORT:Toast.LENGTH_LONG).show());
    }
}
