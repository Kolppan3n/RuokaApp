package com.example.co1200679.ruokaapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class Ostoslista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ostoslista);
        fillScrollView();
    }

    //Täyttää listan
    public void fillScrollView(){

        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        TextView temp;

        Random rnd;
        int color;

        for(int k = 1; k < 31; k++) {
            rnd = new Random();
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            temp = new TextView(this);
            if(k % 2 == 0) {
                temp.setText(String.valueOf(k));
                temp.setBackgroundColor(color);
            }
            else{
                temp.setText(String.valueOf(k));
                temp.setBackgroundColor(color);
            }
            temp.setTextColor(Color.WHITE);
            temp.setGravity(Gravity.CENTER);
            temp.setTextSize(29);
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
            content.addView(temp);
        }

    }
}
