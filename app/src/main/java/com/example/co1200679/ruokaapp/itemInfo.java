package com.example.co1200679.ruokaapp;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;

public class itemInfo extends TextView {

    private String nimi = "Tomaatti";
    private int ID;

    public itemInfo(Context context) {
        super(context);
        this.setText(getNimi());
    }

    public itemInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText(getNimi());
    }

    public itemInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setText(getNimi());
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
}
