package com.example.co1200679.ruokaapp;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Liukuvalikko2 extends AppCompatActivity {

    ViewPager pager;
    PagerAdapter pada;
    PagerListerner pala;
    Tietokanta TK;


    int moodi;// AINE = 0 RUUAT = 1 KAAPPI = 2 LISTA = 3

    @Override
    public void onBackPressed() {
        int item = pager.getCurrentItem();
        if (pada.taakse(item)) ;
        else {
            if (item == 1) pager.setCurrentItem(0);
            else {
                TK.close();
                this.finish();
            }
        }
    }


    public void avaaRuuat(String lause) {
        pager.setCurrentItem(1);
        pada.avaaRuuat(lause);
    }

    public void riehuva_puhveli(String teksti)
    {
        Toast.makeText(this,teksti,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko_swapper);
        TK = new Tietokanta(this);
        int MV = getIntent().getIntExtra("MoodiVasen",0);
        int MO = getIntent().getIntExtra("MoodiOikea",1);


        pager = (ViewPager) findViewById(R.id.pager);
        pada = new PagerAdapter(getSupportFragmentManager(),MV,MO);
        pala = new PagerListerner(this);
        pager.setAdapter(pada);
        pager.addOnPageChangeListener(pala);
    }

}
