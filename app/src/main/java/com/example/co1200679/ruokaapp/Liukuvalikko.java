package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Liukuvalikko extends AppCompatActivity {

    Tietokanta TK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TK = new Tietokanta(this);
        String lause = getIntent().getStringExtra("sqlqry");
        getIntent().getIntExtra("moodi",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liukuvalikko);
        fillScrollView(lause);
    }
    //int seuraavat[] =
   // int lauseet[] = {"SELECT * FROM AineKanta WHERE edellinenID is "};



    public void fillScrollView(String lause) {

        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        View temp;
        itemInfo item;
        ImageView icon;
        RoundImage roi;

        if(content.getChildCount() > 0)
            content.removeAllViews();

        Cursor tiedot = TK.HaeTiedot(lause);

        while(tiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (itemInfo) temp.findViewById(R.id.itemView);
            item.setNimi(tiedot.getString(0));
            item.setID(tiedot.getInt(1));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whatthesht((itemInfo) v);
                }
            });
            icon = (ImageView) temp.findViewById(R.id.imageView);
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.tomaatti);
            roi = new RoundImage(bm);
            icon.setImageDrawable(roi);
            content.addView(temp);
        }
    }

    public void whatthesht(itemInfo v){

        Intent intent = new Intent(this, Liukuvalikko.class);
        String lause = ("SELECT * FROM AineKanta WHERE edellinenID IS " + v.getID());
        intent.putExtra("sqlqry", lause);
        startActivity(intent);
    }
}
