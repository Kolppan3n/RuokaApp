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
    int MO;
    String moodit[] =
            {"SELECT aine nimi, aineID _id, kuva, 0 AS moodi FROM AineKanta WHERE edellinenID is 0",
                    "SELECT ruoka nimi, ruokaID _id, kuva, 1 AS moodi  FROM RuokaKanta",
                    "SELECT aine nimi, KK.aineID _id, kuva, 2 AS moodi, maara / pakkauskoko AS prosentti FROM AineKanta AK, KaappiKanta KK WHERE AK.aineID IS KK.aineID",
                    "SELECT kpl|| ' X '||aine  AS nimi, AK.aineID _id, kuva,3 AS moodi FROM AineKanta AK, OstosKanta OK WHERE AK.aineID IS OK.aineID"};


    public PagerAdapter(FragmentManager fm, int mv, int mo) {
        super(fm);
        MV = mv;
        MO = mo;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                Bundle args = new Bundle();
                args.putInt("moodi", MV);
                args.putString("sqlqry", moodit[MV]);
                vava = new FragmentValikko();
                vava.setArguments(args);
                return vava;
            }
            case 1: {
                Bundle args = new Bundle();
                args.putInt("moodi", MO);
                args.putString("sqlqry", moodit[MO]);
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


    public void avaaRuuat(String lause) {
        ova.LL.UusiLause(lause, 1);
        ova.populateList(lause);
    }
}
