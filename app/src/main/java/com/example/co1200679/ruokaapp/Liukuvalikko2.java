package com.example.co1200679.ruokaapp;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class Liukuvalikko2 extends FragmentActivity {

    ViewPager pager;
    Tietokanta TK;
    String lause;
    LauseLista LL;
    int moodi;// AINE = 0 RUUAT = 1 KAAPPI = 2 LISTA = 3
    int plussa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko_swapper);

        TK = new Tietokanta(this);
        lause = getIntent().getStringExtra("sqlqry");
        moodi = getIntent().getIntExtra("moodi", 0);
        LL = new LauseLista();
        LL.UusiLause(lause, moodi);
        plussa = 0;

        pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pada = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pada);
    }

}
