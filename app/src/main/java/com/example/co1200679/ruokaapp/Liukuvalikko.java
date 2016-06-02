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
    int moodi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TK = new Tietokanta(this);
        String lause = getIntent().getStringExtra("sqlqry");
        moodi = getIntent().getIntExtra("moodi",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liukuvalikko);
        fillScrollView(lause);
    }

    public void fillScrollView(String lause) {

        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        View temp;
        ItemInfo item;
        ImageView icon;
        RoundImage roi;

        if(content.getChildCount() > 0)
            content.removeAllViews();

        Cursor tiedot = TK.HaeTiedot(lause);

        while(tiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (ItemInfo) temp.findViewById(R.id.itemView);
            item.setNimi(tiedot.getString(0));
            item.setID(tiedot.getInt(1));
            item.setMoodi(moodi);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whatthesht((ItemInfo) v);
                }
            });
            icon = (ImageView) temp.findViewById(R.id.imageView);
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.tomaatti);
            roi = new RoundImage(bm);
            icon.setImageDrawable(roi);
            content.addView(temp);
        }
    }

    public void whatthesht(ItemInfo v){

        if(v.getMoodi()==0) {
            //Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS LUKU FROM AineKanta WHERE edellinenID IS " + v.getID());
            //if(lasku.getInt(0)==0)
            //{

            //}
            //else {
                Intent intent = new Intent(this, Liukuvalikko.class);
                String lause = ("SELECT * FROM AineKanta WHERE edellinenID IS " + v.getID());
                intent.putExtra("sqlqry", lause);
                startActivity(intent);
            //}
        }
        if(v.getMoodi()==1)
        {
            Intent intent = new Intent(this, Liukuvalikko.class);
            String lause = ("SELECT * FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.AineID AND RK.RuokaID IS " + v.getID());
            intent.putExtra("sqlqry", lause);
            startActivity(intent);
        }

    }
}
