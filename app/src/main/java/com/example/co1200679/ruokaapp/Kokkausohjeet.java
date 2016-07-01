package com.example.co1200679.ruokaapp;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Kokkausohjeet extends AppCompatActivity {

    Tietokanta TK;
    int ruokaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kokkausohjeet);

        TK = new Tietokanta(this);
        ruokaID = getIntent().getIntExtra("ruokaID", 0);
        String lause = ("SELECT ruoka, ruokaID, resepti FROM RuokaKanta WHERE RuokaID IS " + ruokaID);
        Cursor tiedot = TK.HaeTiedot(lause);
        tiedot.moveToNext();
        loadRecipe(tiedot);
        fillScrollView();
    }

    public void loadRecipe(Cursor tiedot) {
        TextView otsikko = (TextView) findViewById(R.id.ruuanNimi);
        otsikko.setText(tiedot.getString(0));
        RelativeLayout kontti = (RelativeLayout) findViewById(R.id.ohjeKontti);
        View temp = getLayoutInflater().inflate(R.layout.resepti, kontti, false);
        TextView teksti = (TextView) temp.findViewById(R.id.reseptiTeksti);
        teksti.setText(tiedot.getString(2));
        kontti.addView(temp);
    }

    public void fillScrollView() {

        String lause = ("SELECT aine, lkm, mitta FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);
        Cursor tiedot = TK.HaeTiedot(lause);

        LinearLayout kontti = (LinearLayout) findViewById(R.id.ainesKontti);
        View temp;
        ItemInfo item;

        if (kontti.getChildCount() > 0)
            kontti.removeAllViews();


        while (tiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.ainesosa, kontti, false);
            item = (ItemInfo) temp.findViewById(R.id.aine);
            item.setNimi(tiedot.getString(0));
            item = (ItemInfo) temp.findViewById(R.id.lkm);
            item.setText(tiedot.getFloat(1) + " " + tiedot.getString(2));
            item.setKpl("kaksisataa");
            kontti.addView(temp);
        }
    }

    public void toggleA(View view) {
        LinearLayout lio = (LinearLayout) findViewById(R.id.ainesKontti);
        ViewGroup.LayoutParams lap = lio.getLayoutParams();

        if (lap.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            lap.height = 0;
            lio.setLayoutParams(lap);
        } else {
            lap.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lio.setLayoutParams(lap);
        }
    }

    public void toggleO(View view) {
        RelativeLayout lio = (RelativeLayout) findViewById(R.id.ohjeKontti);
        ViewGroup.LayoutParams lap = lio.getLayoutParams();

        if (lap.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            lap.height = 0;
            lio.setLayoutParams(lap);
        } else {
            lap.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lio.setLayoutParams(lap);
        }
    }
}
