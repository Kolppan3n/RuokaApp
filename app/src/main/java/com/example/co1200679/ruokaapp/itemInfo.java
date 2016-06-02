package com.example.co1200679.ruokaapp;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;

public class ItemInfo extends TextView {

    private String nimi = "Testi";
    private int ID;
    private int edellinenID;
    private int moodi;

    public ItemInfo(Context context) {
        super(context);
        this.setText(getNimi());
    }

    public ItemInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText(getNimi());
    }

    public ItemInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setText(getNimi());
    }

    public String getNimi() { return nimi; }

    public void setNimi(String nimi) {
        this.nimi = nimi;
        this.setText(nimi);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getEdellinenID() {
        return edellinenID;
    }

    public void setEdellinenID(int edellinenID) {
        this.edellinenID = edellinenID;
    }

    public int getMoodi() {
        return moodi;
    }

    public void setMoodi(int moodi) {
        this.moodi = moodi;
    }
}
