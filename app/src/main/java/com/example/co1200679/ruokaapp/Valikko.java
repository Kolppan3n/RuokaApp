package com.example.co1200679.ruokaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Valikko extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);
        alustus();
    }

    public void alustus(){

        //Muuttujien alustaminen
        View temp;
        ImageButton ibu;
        TextView teksti;
        LinearLayout rulla1 = (LinearLayout) findViewById(R.id.rullakontti1);
        LinearLayout rulla2 = (LinearLayout) findViewById(R.id.rullakontti2);

        //Ainesten täyttäminen
        for(int k = 0; k < 4; k++){
            temp = getLayoutInflater().inflate(R.layout.ikoni, rulla1, false);
            ibu = (ImageButton) temp.findViewById(R.id.imageButton);
            ibu.setImageResource(R.drawable.tomaatti);
            rulla1.addView(temp);
        }

        //Välineitten täyttäminen
        for(int k = 0; k < 7; k++){
            temp = getLayoutInflater().inflate(R.layout.ikoni, rulla2, false);
            rulla2.addView(temp);
        }

        //Nippelitiedon täyttäminen
        teksti = (TextView) findViewById(R.id.nippelitieto);
        teksti.setText("TLDR;");
    }
}
