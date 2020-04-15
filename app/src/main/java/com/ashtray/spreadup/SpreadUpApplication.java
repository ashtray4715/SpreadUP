package com.ashtray.spreadup;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.ads.MobileAds;

public class SpreadUpApplication extends Application {

    private static SpreadUpApplication application;
    public static SpreadUpApplication getInstance() {
        return application;
    }

    private boolean mobileAdsInitialized;
    public boolean isMobileAdsInitialized() { return mobileAdsInitialized; }

    @Override
    public void onCreate() {
        super.onCreate();

        // saving the application object for handling context
        application = this;

        //initializing mobile ads sdk
        mobileAdsInitialized = false;
        if (isInternetAvailable()) {
            MobileAds.initialize(SpreadUpApplication.getInstance(), "ca-app-pub-3940256099942544~3347511713");
            mobileAdsInitialized = true;
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}