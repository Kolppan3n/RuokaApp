package com.example.co1200679.ruokaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Asetukset extends AppCompatActivity {

    Tietokanta TK;
    Kirjastonhoitaja kirja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset);
        TK = new Tietokanta(this);
        kirja = new Kirjastonhoitaja(this);
    }

    public void paivitaTietokanta(View view) {
        kirja.execute();
        Toast.makeText(this, "Tietokannat Päivitetty!", Toast.LENGTH_SHORT).show();
    }

    public void alustaTietokanta(View view) {
        TK.onUpgrade(TK.getReadableDatabase(), 1, 1); //Tällä saadaan tehtyä uusi versio tietokannasta, mutta tiedot poistu
        kirja.execute();
        Toast.makeText(this, "Tietokannat Alustettu!", Toast.LENGTH_SHORT).show();
    }
}
