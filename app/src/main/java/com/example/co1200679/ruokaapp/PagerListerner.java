package com.example.co1200679.ruokaapp;

/**
 * Created by co1300608 on 21.7.2016.
 */

import android.support.v4.view.ViewPager.OnPageChangeListener;

public class PagerListerner implements OnPageChangeListener {

    Liukuvalikko2 liukkari;
    public PagerListerner(Liukuvalikko2 LV2) {
        liukkari = LV2;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        liukkari.muuttuvaValikko(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
