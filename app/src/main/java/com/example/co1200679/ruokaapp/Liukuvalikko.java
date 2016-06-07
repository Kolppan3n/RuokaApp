package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        Log.d("asdasdasd", DatabaseUtils.dumpCursorToString(tiedot));


        while(tiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (ItemInfo) temp.findViewById(R.id.itemView);
            item.setNimi(tiedot.getString(0));
            item.setID(tiedot.getInt(1));
            item.setMoodi(moodi);
            item.setText(item.getNimi());
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whatthesht((ItemInfo) v);
                }
            });


            item.setOnTouchListener(new OnSwipeTouchListener() {
                public boolean onSwipeRight() {
                    Toast.makeText(Liukuvalikko.this, "right", Toast.LENGTH_SHORT).show();
                    return true;
                }
                public boolean onSwipeLeft() {
                    Toast.makeText(Liukuvalikko.this, "left", Toast.LENGTH_SHORT).show();
                    return true;
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
            Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku FROM AineKanta WHERE edellinenID IS " + v.getID());
            lasku.moveToNext();
            //Log.d("asdasdasd",DatabaseUtils.dumpCursorToString(lasku));

            if(lasku.getInt(0)==0)
            {
                Intent intent = new Intent(this, Liukuvalikko.class);
                String lause = ("SELECT * FROM RuokaKanta RU, ReseptiKanta RE WHERE RU.ruokaID IS RE.ruokaID AND RE.aineID IS "+ v.getID());
                intent.putExtra("sqlqry", lause);
                intent.putExtra("moodi", 1);
                startActivity(intent);

            }
            else {
                Intent intent = new Intent(this, Liukuvalikko.class);
                String lause = ("SELECT * FROM AineKanta WHERE edellinenID IS " + v.getID());
                intent.putExtra("sqlqry", lause);
                intent.putExtra("moodi", 0);
                startActivity(intent);
            }
        }
        if(v.getMoodi()==1)
        {
            Intent intent = new Intent(this, Kokkausohjeet.class);
            intent.putExtra("ruokaID",v.getID());
            startActivity(intent);
        }

    }
}
