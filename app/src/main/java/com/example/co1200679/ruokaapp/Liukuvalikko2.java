package com.example.co1200679.ruokaapp;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

public class Liukuvalikko2 extends FragmentActivity {

    ViewPager pager;
    PagerAdapter pada;
    Tietokanta TK;

    int moodi;// AINE = 0 RUUAT = 1 KAAPPI = 2 LISTA = 3

    @Override
    public void onBackPressed() {
        int item = pager.getCurrentItem();
        if(pada.taakse(item));
        else {
            TK.close();
            this.finish();
        }
    }

    public void avaaRuuat(String lause){
        pager.setCurrentItem(1);
        pada.avaaRuuat(lause);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko_swapper);
        TK = new Tietokanta(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pada = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pada);
    }

}
