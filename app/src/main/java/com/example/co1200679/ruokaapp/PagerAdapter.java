package com.example.co1200679.ruokaapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:{
                VasenValikko vava = new VasenValikko();
                return vava;
            }
            case 1:{
                OikeaValikko ova = new OikeaValikko();
                return ova;
            }
            default:
                Log.d("Herp", "Derp");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
