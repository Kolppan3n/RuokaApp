package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Liukuvalikko extends AppCompatActivity {

    Tietokanta TK;
    int moodi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liukuvalikko);

        TK = new Tietokanta(this);
        String lause = getIntent().getStringExtra("sqlqry");
        moodi = getIntent().getIntExtra("moodi",0);
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
            item.setText(item.getNimi());
            if(moodi==2)
            {
                item.setKpl("" + (tiedot.getInt(3)+ 10));
                VariAine(item);
            }
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    whatthesht((ItemInfo) v);
                }
            });


            item.setOnTouchListener(new OnSwipeTouchListener(item) {
                public boolean onSwipeRight() {
                    avaaRuuat(i);
                    if (moodi == 0)
                        Toast.makeText(Liukuvalikko.this, "Ateriat joissa tarvitaan " + i.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                public boolean onSwipeLeft() {
                    if(i.getMoodi()==0)
                    {
                        laitaKaappiin(i);
                    }
                    if(i.getMoodi()==2)
                    {
                        VariAine(i);
                    }
                    Toast.makeText(Liukuvalikko.this, i.getText() + " lisättiin kaappiin", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


            icon = (ImageView) temp.findViewById(R.id.imageView);
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.tomaatti);

            if(getResources().getIdentifier(tiedot.getString(2),"drawable",getPackageName())!=0)
            {
                    bm = BitmapFactory.decodeResource(getResources(),(getResources().getIdentifier(tiedot.getString(2),"drawable",getPackageName())));
            }

            roi = new RoundImage(bm);
            icon.setImageDrawable(roi);
            content.addView(temp);
        }
    }

    public void avaaRuuat(ItemInfo v){

        if(v.getMoodi()==0) {
            Cursor lasku = TK.HaeTiedot("SELECT count(ruokaID) AS luku FROM ReseptiKanta WHERE aineID IS " + v.getID());
            lasku.moveToNext();

            if (lasku.getInt(0) != 0) {
                Intent intent = new Intent(this, Liukuvalikko.class);
                Log.d("asdasdasd","ei toimi");
                String lause = ("SELECT ruoka, RU.ruokaID, kuva FROM RuokaKanta RU, ReseptiKanta RE WHERE RU.ruokaID IS RE.ruokaID AND RE.aineID IS " + v.getID());
                intent.putExtra("sqlqry", lause);
                intent.putExtra("moodi", 1);
                startActivity(intent);
            }
        }
    }

    public void laitaKaappiin(ItemInfo v){
        TK.LaitaKaappiin(v.getID());
    }

    public void whatthesht(ItemInfo v){

        if(v.getMoodi()==0) {
            Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku FROM AineKanta WHERE edellinenID IS " + v.getID());
            lasku.moveToNext();

            if(lasku.getInt(0)==0)
            {
                avaaRuuat(v);
            }
            else {
                Intent intent = new Intent(this, Liukuvalikko.class);
                String lause = ("SELECT aine, aineID, kuva FROM AineKanta WHERE edellinenID is " + v.getID());
                intent.putExtra("sqlqry", lause);
                intent.putExtra("moodi", 0);
                startActivity(intent);
            }
        }
        if(v.getMoodi()==1)
        {
            Intent intent = new Intent(this, Valikko.class);
            intent.putExtra("ruokaID", v.getID());
            startActivity(intent);
        }

    }

    public void VariAine(ItemInfo v)
    {
        if(v.getMoodi() == 2) {
            int color = getResources().getColor(R.color.colorRed);
            int prosentti = Integer.parseInt(v.getKpl());
            prosentti -=10;
            v.setKpl(""+ prosentti);
            TK.KulutaAinetta(v.getID(),prosentti);
            if (prosentti>25)
            {
                if (prosentti>50)
                {
                    if (prosentti>75)
                    {
                        if (prosentti>99)
                        {
                            color = getResources().getColor(R.color.colorGreen);
                        }
                        else color = getResources().getColor(R.color.colorYellowGreen);
                    }
                    else color = getResources().getColor(R.color.colorYellow);
                }
                else color = getResources().getColor(R.color.colorOrange);
            }
            v.setBackgroundColor(color);
        }

    }

}
