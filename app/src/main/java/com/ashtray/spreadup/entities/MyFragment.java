package com.ashtray.spreadup.entities;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class MyFragment extends Fragment {

    protected MyFragmentCallBacks myFragmentCallBacks;

    public MyFragment(){
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.setMyFragmentCallBacks(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myFragmentCallBacks = null;
    }

    private void setMyFragmentCallBacks(Context context){
        if(context instanceof MyFragmentCallBacks){
            myFragmentCallBacks = (MyFragmentCallBacks) context;
        }else {
            throw new RuntimeException("Must implement MyFragmentCallBacks");
        }
    }

    //currently showing fragment must have to keep an way to control back pressed
    public abstract boolean handleBackButtonPressed();

    //currently showing fragment must have to keep an way to control menu item selection
    public abstract void handleMenuItemSelection(MenuItem menuItem);

    public interface MyFragmentCallBacks{
        void showFragment(MyFragmentName myFragmentName);
        void showToastMessage(String message, boolean forShortTime);
        void setActivityTitle(String title);
        void setBackButtonEnabled(boolean enabled);
    }

    public enum MyFragmentName{
        FRAGMENT_HOME, FRAGMENT_ADVERTISING, FRAGMENT_SCANNING, FRAGMENT_CONNECTED
    }

}