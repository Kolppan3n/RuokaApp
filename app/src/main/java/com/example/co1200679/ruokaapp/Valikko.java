package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Valikko extends AppCompatActivity {

    Tietokanta TK;
    int ruokaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);

        //Tietokannan ja kursorin luominen
        TK = new Tietokanta(this);
        ruokaID = getIntent().getIntExtra("ruokaID", 0);
        String lause = ("SELECT ruoka, tarvikkeet, aika, taso FROM RuokaKanta WHERE RuokaID IS " + ruokaID);
        Cursor ruokatiedot = TK.HaeTiedot(lause);
        ruokatiedot.moveToNext();

        //Aineen otsikko
        TextView otsikko;
        otsikko = (TextView) findViewById(R.id.otsikko);
        otsikko.setText(ruokatiedot.getString(0));

        //Alustaa sisällön
        alustus(ruokatiedot);
    }

    public void alustus(Cursor ruokatiedot) {

        String lause = ("SELECT kuva FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);
        Cursor ainetiedot = TK.HaeTiedot(lause);

        //Muuttujien alustaminen
        View temp;
        ImageView kaappiBtn;
        ImageView listBtn;
        ImageView reseptiBtn;
        ImageButton ibu;
        TextView teksti;
        LinearLayout rulla1 = (LinearLayout) findViewById(R.id.rullakontti1);
        LinearLayout rulla2 = (LinearLayout) findViewById(R.id.rullakontti2);

        //Tyhjentää aineet ja välineet
        if(rulla1.getChildCount() > 0)
            rulla1.removeAllViews();
        if(rulla2.getChildCount() > 0)
            rulla2.removeAllViews();

        //Ainesten täyttäminen

        while(ainetiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.ikoni, rulla1, false);
            ibu = (ImageButton) temp.findViewById(R.id.imageButton);
            ibu.setImageResource(getResources().getIdentifier(ainetiedot.getString(0),"drawable",getPackageName()));
            rulla1.addView(temp);
        }

        //Välineitten täyttäminen
        int testiarvo = 1;
        int tarvikearvo = ruokatiedot.getInt(1);
        String tarvikenimet[] = {"paistinpannu", "kattila", "uuni", "kulho", "puukko", "uunivuoka", "piirakkavuoka", "kakkuvuoka", "irtopohjavuoka", "sauvasekoitin", "sahkovatkain", "tehosekoitin", "yleiskone", "vispila", "kaulin", "siivila", "raastinrauta", "grilli", "lihanuija", "avotuli", "mehustin", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};


        for (int k = 0; k < 33; k++) {
            if ((testiarvo & tarvikearvo) != 0) {
                temp = getLayoutInflater().inflate(R.layout.ikoni, rulla2, false);
                ibu = (ImageButton) temp.findViewById(R.id.imageButton);
                ibu.setImageResource(getResources().getIdentifier(tarvikenimet[k],"drawable",getPackageName()));
                rulla2.addView(temp);
            }
            testiarvo *= 2;
        }

        //Nippelitiedon täyttäminen

        TextView aika = (TextView) findViewById(R.id.aikaTxt);

        int Valmistusminuutit = ruokatiedot.getInt(2) % 60;
        int Valmistustunnit = ruokatiedot.getInt(2) / 60;
        String kokoaika = "";
        if (Valmistustunnit > 0) kokoaika = Valmistustunnit + " Tuntia ";
        if (Valmistusminuutit > 0) kokoaika += Valmistusminuutit + " min";

        aika.setText(kokoaika);

        TextView vaikeusaste = (TextView) findViewById(R.id.vaikeusTxt);
        String vaikeusmerkit = "";
        for (int M = 0; M < ruokatiedot.getInt(3); M++) vaikeusmerkit += "\uD83C\uDF5D";
        vaikeusaste.setText(vaikeusmerkit);



    }

    public void avaaReseptiBtnClick(View view) {
        Intent intent = new Intent(this, Kokkausohjeet.class);
        intent.putExtra("ruokaID", ruokaID);
        startActivity(intent);
    }

    public void KatsoKaappiinBtnClick(View view) {
    }

    public void LisaaListaanBtnClick(View view){
        String lause = ("SELECT aineID, ruokaID FROM ReseptiKanta WHERE aineID NOT IN (SELECT aineID FROM KaappiKanta WHERE maara > 10) AND  RuokaID IS " + ruokaID);
        Cursor listatiedot = TK.HaeTiedot(lause);
        Log.d("popopopo",lause);
        Log.d("pappapa", DatabaseUtils.dumpCursorToString(listatiedot));
        while(listatiedot.moveToNext())
        {
         TK.LaitaListaan(listatiedot.getInt(0));
        }

        String toasti = "Tarvittavat aineet\nlisätty Ostoslistaan";
        Toast.makeText(Valikko.this, toasti, Toast.LENGTH_SHORT).show();

    }

    public void palaaKotiin(View view){
        startActivity(new Intent(this, MainAct.class));
    }



}