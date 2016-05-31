package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainAct extends AppCompatActivity {
    Tietokanta TK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TK = new Tietokanta(this);
        //TK.onUpgrade(TK.getReadableDatabase(),1,1); //Tällä saadaan tehtyä uusi versio tietokannasta, mutta tiedot poistuu
    }

    public void LiukuvalikkoBtnClick(View view){
        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = "SELECT * FROM KaappiKanta";
        intent.putExtra("sqlqry", lause);
        startActivity(intent);
    }

    public void KaappiBtnClick(View view){
        TK.TaytaKaappi();
    }

}
