package com.example.co1200679.ruokaapp;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class Liukuvalikko extends AppCompatActivity {
    Tietokanta TK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TK = new Tietokanta(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ainekset);
        fillScrollView();
    }

    //Täyttää listan
    public void fillScrollView(){

        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        TextView temp;

        Random rnd;
        int color;

        Cursor tiedot = TK.HaeTiedot();

        while(tiedot.moveToNext()) {
            rnd = new Random();
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            temp = new TextView(this);

                temp.setText(String.valueOf(tiedot.getString(0)));
                temp.setBackgroundColor(color);

            temp.setTextColor(Color.WHITE);
            temp.setGravity(Gravity.CENTER);
            temp.setTextSize(29);
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
            content.addView(temp);
        }

    }
}
