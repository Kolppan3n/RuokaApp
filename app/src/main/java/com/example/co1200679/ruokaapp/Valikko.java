package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Valikko extends AppCompatActivity {

    Tietokanta TK;
    int ruokaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);

        //Tietokannan ja kursorin luominen
        TK = new Tietokanta(this);
        ruokaID = getIntent().getIntExtra("ruokaID",0);
        String lause = ("SELECT * FROM RuokaKanta WHERE RuokaID IS " + ruokaID);
        Cursor tiedot = TK.HaeTiedot(lause);
        tiedot.moveToNext();

        //Aineen otsikko
        TextView otsikko;
        otsikko = (TextView) findViewById(R.id.otsikko);
        otsikko.setText(tiedot.getString(0));

        //Alustaa sisällön
        alustus();
    }

    public void alustus(){

        String lause = ("SELECT * FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);
        Cursor tiedot = TK.HaeTiedot(lause);

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
        for(int k = 0; k < 3; k++){
            temp = getLayoutInflater().inflate(R.layout.ikoni, rulla1, false);
            ibu = (ImageButton) temp.findViewById(R.id.imageButton);
            ibu.setImageResource(R.drawable.jauheliha);
            rulla1.addView(temp);
        }

        //Välineitten täyttäminen
        for(int k = 0; k < 42; k++){
            temp = getLayoutInflater().inflate(R.layout.ikoni, rulla2, false);
            ibu = (ImageButton) temp.findViewById(R.id.imageButton);
            ibu.setImageResource(R.drawable.makaroni);
            rulla2.addView(temp);
        }

        //Nippelitiedon täyttäminen

        //Nappien alustaminen
        reseptiBtn = (ImageView) findViewById(R.id.navicon3);
        reseptiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avaaResepti();
            }
        });

    }

    public void avaaResepti(){
        Intent intent = new Intent(this, Kokkausohjeet.class);
        intent.putExtra("ruokaID",ruokaID);
        startActivity(intent);
    }
}
