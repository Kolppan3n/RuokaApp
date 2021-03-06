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
        TK = new Tietokanta(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //alustaKannat();
    }

    public void alustaKannat(){

        TK.onUpgrade(TK.getReadableDatabase(), 1, 1); //Tällä saadaan tehtyä uusi versio tietokannasta, mutta tiedot poistu
        kirja = new Kirjastonhoitaja(this);
        kirja.execute();
    }

    public void AineBtnClick(View view) {
       /* Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT aine nimi, aineID _id, kuva, 0 AS moodi FROM AineKanta WHERE edellinenID is 0";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 0);
        startActivity(intent);*/
        avaaLiukuvalikko2(0,1,0);
    }

    public void RuokaBtnClick(View view) {
      /*  Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT ruoka nimi, ruokaID _id, kuva, 1 AS moodi  FROM RuokaKanta";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 1);
        startActivity(intent);*/
        avaaLiukuvalikko2(0,1,1);
    }

    public void KaappiBtnClick(View view) {
       /* Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT aine nimi, KK.aineID _id, kuva, 2 AS moodi, maara / pakkauskoko AS prosentti FROM AineKanta AK, KaappiKanta KK WHERE AK.aineID IS KK.aineID";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 2);
        startActivity(intent);*/
        avaaLiukuvalikko2(2,3,0);
    }

    public void ListaBtnClick(View view) {
        /*Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT kpl|| ' X '||aine  AS nimi, AK.aineID _id, kuva,3 AS moodi FROM AineKanta AK, OstosKanta OK WHERE AK.aineID IS OK.aineID";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 3);
        startActivity(intent);*/
        avaaLiukuvalikko2(2,3,1);
    }


    public void avaaLiukuvalikko2(int MV, int MO, int VV)
    {
        Intent intent = new Intent(this, Liukuvalikko2.class);
        intent.putExtra("MoodiVasen", MV);
        intent.putExtra("MoodiOikea", MO);
        intent.putExtra("valitseValikko",VV);
        startActivity(intent);
    }



}
