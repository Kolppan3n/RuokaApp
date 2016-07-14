package com.example.co1200679.ruokaapp;

import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class PagerAdapter extends FragmentPagerAdapter {

    FragmentValikko vava;
    FragmentValikko ova;
    int MV;
    String LV;
    int MO;
    String LO;


    public PagerAdapter(FragmentManager fm,int mv, String lv, int mo, String lo) {
        super(fm);
        MV = mv;
        LV = lv;
        MO = mo;
        LO = lo;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                Bundle args = new Bundle();
                args.putInt("moodi", MV);
                args.putString("sqlqry", LV);
                vava = new FragmentValikko();
                vava.setArguments(args);
                return vava;
            }
            case 1: {
                Bundle args = new Bundle();
                args.putInt("moodi", MO);
                args.putString("sqlqry",LO);
                ova = new FragmentValikko();
                ova.setArguments(args);
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

    public boolean taakse(int hermanni) {
        switch (hermanni) {
            case 0: {
                return vava.takaisin();
            }
            case 1: {
                return ova.takaisin();
            }
            default:
                Log.d("Herp", "Derp");
                return false;
        }
    }


    public void avaaRuuat(String lause)
    {
        ova.LL.UusiLause(lause,1);
        ova.populateList(lause);
    }
}
