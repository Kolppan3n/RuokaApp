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

    public void LiukuvalikkoBtnClick(View view){
        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT * FROM AineKanta WHERE edellinenID is 0";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 0);
        startActivity(intent);
    }

    public void RuokaBtnClick(View view){
        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT * FROM RuokaKanta";
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 1);
        startActivity(intent);
    }

    public void KaappiBtnClick(View view){
    }

    public void Testi(View view){
        /*startActivity(new Intent(this, Valikko.class));*/
    }

}
