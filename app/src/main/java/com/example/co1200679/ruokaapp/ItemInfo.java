package com.example.co1200679.ruokaapp;

        import android.content.Context;
        import android.util.AttributeSet;
        import android.widget.TextView;

public class ItemInfo extends TextView {

    private String nimi = "Testi";
    private String kpl = "Testi";
    private int ID;
    private int moodi;

    public ItemInfo(Context context) {
        super(context);
    }

    public ItemInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    public int getMoodi() {
        return moodi;
    }

    public void setMoodi(int moodi) {
        this.moodi = moodi;
    }

    public String getKpl() {
        return kpl;
    }

    public void setKpl(String kpl) {
        this.kpl = kpl;
    }
}