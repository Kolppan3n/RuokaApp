package com.example.co1200679.ruokaapp;

import android.os.Bundle;
import android.support.v4.app.BundleCompat;
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
                Bundle args = new Bundle();
                args.putInt("vari",1);
                args.putString("sqlqry","SELECT aine nimi, aineID _id, kuva, 0 AS moodi FROM AineKanta WHERE edellinenID is 0");
                FragmentValikko vava = new FragmentValikko();
                vava.setArguments(args);
                return vava;
            }
            case 1:{
                Bundle args = new Bundle();
                args.putInt("vari",0);
                args.putString("sqlqry","SELECT ruoka nimi, ruokaID _id, kuva, 1 AS moodi  FROM RuokaKanta");
                FragmentValikko ova = new FragmentValikko();
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
}
