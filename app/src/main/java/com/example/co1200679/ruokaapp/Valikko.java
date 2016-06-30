package com.example.co1200679.ruokaapp;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Valikko extends AppCompatActivity {

    String lause;
    Tietokanta TK;
    int ruokaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);

        //Tietokannan ja kursorin luominen
        TK = new Tietokanta(this);
        ruokaID = getIntent().getIntExtra("ruokaID", 0);
        lause = ("SELECT ruoka, tarvikkeet, aika, taso FROM RuokaKanta WHERE RuokaID IS " + ruokaID);
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
        //Kursorin alustaminen
        String lause = ("SELECT kuva FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);
        Cursor ainetiedot = TK.HaeTiedot(lause);


        //Muuttujien alustaminen
        View temp;
        ImageView kaappiBtn;
        ImageView listBtn;
        ImageView reseptiBtn;
        ImageView ibu;
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
            ibu = (ImageView) temp.findViewById(R.id.ikoni);
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
                ibu = (ImageView) temp.findViewById(R.id.ikoni);
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

    public void avaaResepti() {
        Intent intent = new Intent(this, Kokkausohjeet.class);
        intent.putExtra("ruokaID", ruokaID);
        startActivity(intent);
    }

    public void katsoKaappiin() {
    }

    public void lisaaListaan(){
        String lause = ("SELECT aineID, ruokaID,lkm FROM ReseptiKanta WHERE RuokaID IS " + ruokaID);
        Cursor listatiedot = TK.HaeTiedot(lause);
        while(listatiedot.moveToNext())
        {
            laitaVaraus(listatiedot.getInt(0),listatiedot.getFloat(2));
        }
        String toasti = "Tarvittavat aineet\nlisätty Ostoslistaan";
        Toast.makeText(Valikko.this, toasti, Toast.LENGTH_SHORT).show();
        listatiedot.close();
    }

    public void laitaVaraus(int aineID,float maara){
        Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku, maara FROM VarausKanta WHERE aineID IS " + aineID);
        lasku.moveToNext();
        if(lasku.getInt(0)==0)
        {
            TK.LaitaVaraus(aineID,maara);
        }
        else
        {
            TK.VaraaLisaa(aineID,lasku.getFloat(1)+maara);
        }
        lasku.close();
    }

}