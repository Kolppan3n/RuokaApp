package com.example.co1200679.ruokaapp;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class Liukuvalikko2 extends AppCompatActivity {

    ViewPager pager;
    PagerAdapter pada;
    PagerListerner pala;
    Tietokanta TK;
    ToolbarFragment Toolbar;


    int moodi;// AINE = 0 RUUAT = 1 KAAPPI = 2 LISTA = 3

    @Override
    public void onBackPressed() {
        int item = pager.getCurrentItem();
        if (pada.taakse(item)) muuttuvaValikko(item);
        else {

                TK.close();
                this.finish();
        }
    }


    public void avaaRuuat(String lause) {
        pada.avaaRuuat(lause);
        pager.setCurrentItem(1);
    }

    public int alkuvalinta(){
        int valitseValikko = getIntent().getIntExtra("valitseValikko",0);
        pager.setCurrentItem(valitseValikko);
        return valitseValikko;
    }

    public void muuttuvaValikko(int numero) {
        int moodi = pada.vava.moodi;
        if (numero == 1) {
            moodi = pada.ova.moodi;
            if(!pada.ova.kassi) moodi = 22;
        }
            Toolbar.muutaToolbar(moodi);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko_swapper);
        TK = new Tietokanta(this);
        int MV = getIntent().getIntExtra("MoodiVasen", 0);
        int MO = getIntent().getIntExtra("MoodiOikea", 1);

        pager = (ViewPager) findViewById(R.id.pager);
        pada = new PagerAdapter(getSupportFragmentManager(), MV, MO);
        pala = new PagerListerner(this);
        pager.setAdapter(pada);
        pager.addOnPageChangeListener(pala);
    }

}
