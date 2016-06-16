package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainAct extends AppCompatActivity {
    Tietokanta TK;
    Kirjastonhoitaja kirja;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TK = new Tietokanta(this);
        TK.onUpgrade(TK.getReadableDatabase(),1,1); //Tällä saadaan tehtyä uusi versio tietokannasta, mutta tiedot poistu
        kirja = new Kirjastonhoitaja(this);
        kirja.execute();
    }

    public void AineBtnClick(View view){
        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT aine, aineID, kuva FROM AineKanta WHERE edellinenID is 0";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 0);
        startActivity(intent);
    }

    public void RuokaBtnClick(View view){
        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT ruoka, ruokaID, kuva FROM RuokaKanta";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 1);
        startActivity(intent);
    }

    public void KaappiBtnClick(View view){
        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT aine, AK.aineID, kuva, prosentti FROM AineKanta AK, KaappiKanta KK WHERE AK.aineID IS KK.aineID";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 2);
        startActivity(intent);
    }

    public void ListaBtnClick(View view){
        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT aine, AK.aineID, kuva FROM AineKanta AK, OstosKanta OK WHERE AK.aineID IS OK.aineID";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 3);
        startActivity(intent);
    }


}
