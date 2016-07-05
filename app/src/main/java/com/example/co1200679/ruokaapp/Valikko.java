package com.example.co1200679.ruokaapp;


import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Valikko extends AppCompatActivity {

    String lause;
    Tietokanta TK;
    int ruokaID;
    long viive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);

        viive = System.currentTimeMillis();
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
        Log.d("otsikon viiveet", (System.currentTimeMillis() - viive) + "");
        //Alustaa sisällön
        alustus(ruokatiedot);

    }

    public void alustus(Cursor ruokatiedot) {

        HorizontalListView rulla1 = (HorizontalListView) findViewById(R.id.rullakontti1);
        HorizontalListView rulla2 = (HorizontalListView) findViewById(R.id.rullakontti2);

        //Ainesten täyttäminen
        String lause = ("SELECT kuva _id FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);
        Cursor ainetiedot = TK.HaeTiedot(lause);
        startManagingCursor(ainetiedot);

        String[] columns = new String[]{"_id"};
        int[] viewIDs = new int[]{R.id.ikoni};
        SimpleCursorAdapter filleri = new SimpleCursorAdapter(this, R.layout.ikoni, ainetiedot, columns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        rulla1.setAdapter(filleri);

        Log.d("aineviive", (System.currentTimeMillis() - viive) + "");

        int tarvikearvo = ruokatiedot.getInt(1);
        String lause2 = ("SELECT kuva _id FROM ValineKuvaKanta WHERE valineID & " + tarvikearvo);
        Cursor valinekuvat = TK.HaeTiedot(lause2);
        startManagingCursor(valinekuvat);

        String[] columns2 = new String[]{"_id"};
        int[] viewIDs2 = new int[]{R.id.ikoni};
        SimpleCursorAdapter filleri2 = new SimpleCursorAdapter(this, R.layout.ikoni, valinekuvat, columns2, viewIDs2, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        rulla2.setAdapter(filleri2);

        Log.d("välineviive", (System.currentTimeMillis() - viive) + "");

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

        Log.d("kokoviive", (System.currentTimeMillis() - viive) + "");
    }

    public void avaaResepti() {
        Intent intent = new Intent(this, Kokkausohjeet.class);
        intent.putExtra("ruokaID", ruokaID);
        startActivity(intent);
    }

    public void katsoKaappiin() {
        Cursor kaapissaON = TK.HaeTiedot("SELECT aine, Ak.aineID, RK.aineID FROM AineKanta AK, ReseptiKanta RK WHERE RK.aineID IS AK.aineID AND RK.aineID IN (SELECT aineID FROM KaappiKanta) AND RuokaID IS " + ruokaID);
        Cursor kaapissaEIOLE = TK.HaeTiedot("SELECT aine, Ak.aineID, RK.aineID FROM AineKanta AK, ReseptiKanta RK WHERE RK.aineID IS AK.aineID AND RK.aineID NOT IN (SELECT aineID FROM KaappiKanta) AND RuokaID IS " + ruokaID);
        String viesti = "";
        String on = "";
        String eiOle = "";

        while (kaapissaON.moveToNext()) {
            on += "\n -" + kaapissaON.getString(0);
        }
        if (on != "")
            viesti = "Kaapista löytyy:" + on;

        while (kaapissaEIOLE.moveToNext()) {
            eiOle += "\n -" + kaapissaEIOLE.getString(0);
        }
        if (eiOle != "") {
            if (on != "")
                viesti += "\n\n";
            viesti += "Sinulta puuttuu:" + eiOle;
        } else viesti = "Kaikki aineet löytyy!";

        Toast.makeText(Valikko.this, viesti, Toast.LENGTH_LONG).show();
    }

    public void kulutaAineet() {
        String lause = ("SELECT aine _id, lkm, mitta FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);
    }

    public void lisaaListaan() {
        String lause = ("SELECT aineID, ruokaID,lkm FROM ReseptiKanta WHERE RuokaID IS " + ruokaID);
        Cursor listatiedot = TK.HaeTiedot(lause);
        while (listatiedot.moveToNext()) {
            laitaVaraus(listatiedot.getInt(0), listatiedot.getFloat(2));
        }
        String toasti = "Tarvittavat aineet\nlisätty Ostoslistaan";
        Toast.makeText(Valikko.this, toasti, Toast.LENGTH_SHORT).show();
        listatiedot.close();
    }

    public void laitaVaraus(int aineID, float maara) {
        Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku, maara FROM VarausKanta WHERE aineID IS " + aineID);
        lasku.moveToNext();
        if (lasku.getInt(0) == 0) {
            TK.LaitaVaraus(aineID, maara);
        } else {
            TK.VaraaLisaa(aineID, lasku.getFloat(1) + maara);
        }
        lasku.close();
    }

}