package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Liukuvalikko extends AppCompatActivity {

    Tietokanta TK;
    int moodi;// AINE = 0 RUUAT = 1 KAAPPI = 2 LISTA = 3

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
        long viive;

        if(content.getChildCount() > 0)
            content.removeAllViews();

        viive = System.currentTimeMillis();
        Cursor tiedot = TK.HaeTiedot(lause);
        Log.d("Kursorin luominen", (System.currentTimeMillis() - viive) + " millisekunttia");

        viive = System.currentTimeMillis();
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
                RuokaaKuluu(item); //vähentää 10% ruuan määrästä ja laittaa värin, joten lisätään 10% ennen tätä niin ruuan määrä pysyy ennallaan
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
                        laitaListaan(i);
                        Toast.makeText(Liukuvalikko.this, i.getText() + " lisättiin ostoslistalle", Toast.LENGTH_SHORT).show();
                    }
                    if(i.getMoodi()==1)
                    {
                        laitaRuokaListaan(i);
                    }
                    if(i.getMoodi()==2)
                    {
                        RuokaaKuluu(i);//vähentää 10% ruuan määrästä ja laittaa värin
                    }
                    return true;
                }
            });

            ImageView img = (ImageView) temp.findViewById(R.id.imageView);
            img.setImageResource(getResources().getIdentifier(tiedot.getString(2), "drawable", getPackageName()));
            content.addView(temp);
        }
        Log.d("Taulukon täyttäminen", (System.currentTimeMillis() - viive) + " millisekunttia");
        if(moodi == 3)
        {
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (ItemInfo) temp.findViewById(R.id.itemView);
            item.setNimi("Laita kaikki ostokset kaappiin");
            item.setMoodi(moodi);
            item.setText(item.getNimi());

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ostoksetKaappiin();
                }
            });
            ImageView img = (ImageView) temp.findViewById(R.id.imageView);
            img.setImageResource(getResources().getIdentifier(tiedot.getString(2), "drawable", getPackageName()));
            content.addView(temp);

        }

        tiedot.close();
    }

    public void avaaRuuat(ItemInfo v){

        if(v.getMoodi()==0) {
            Cursor lasku = TK.HaeTiedot("SELECT count(ruokaID) AS luku FROM ReseptiKanta WHERE aineID IS " + v.getID());
            lasku.moveToNext();

            if (lasku.getInt(0) != 0) {
                Intent intent = new Intent(this, Liukuvalikko.class);
                String lause = ("SELECT ruoka, RU.ruokaID, kuva FROM RuokaKanta RU, ReseptiKanta RE WHERE RU.ruokaID IS RE.ruokaID AND RE.aineID IS " + v.getID());
                intent.putExtra("sqlqry", lause);
                intent.putExtra("moodi", 1);
                lasku.close();
                startActivity(intent);
            }
        }
    }

    public void laitaListaan(ItemInfo v){
        TK.LaitaListaan(v.getID());
    }

    public void laitaRuokaListaan(ItemInfo v){
        String lause = ("SELECT aineID, ruokaID FROM ReseptiKanta WHERE aineID NOT IN (SELECT aineID FROM KaappiKanta WHERE maara > 10) AND  RuokaID IS " + v.getID());
        Cursor listatiedot = TK.HaeTiedot(lause);
        Log.d("popopopo",lause);
        Log.d("pappapa", DatabaseUtils.dumpCursorToString(listatiedot));
        while(listatiedot.moveToNext())
        {
            TK.LaitaListaan(listatiedot.getInt(0));
        }

        Toast.makeText(Liukuvalikko.this,v.getText() + " ainekset lisättiin ostoslistalle", Toast.LENGTH_SHORT).show();
        listatiedot.close();
    }

    public void whatthesht(ItemInfo v){

        if(v.getMoodi()==0) {
            Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku FROM AineKanta WHERE edellinenID IS " + v.getID());
            lasku.moveToNext();

            if(lasku.getInt(0)==0)
            {
                lasku.close();
                avaaRuuat(v);
            }
            else {
                Intent intent = new Intent(this, Liukuvalikko.class);
                String lause = ("SELECT aine, aineID, kuva FROM AineKanta WHERE edellinenID is " + v.getID());
                intent.putExtra("sqlqry", lause);
                intent.putExtra("moodi", 0);
                lasku.close();
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

    public void RuokaaKuluu(ItemInfo v)
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

    public void ostoksetKaappiin()
    {

        String lause = ("SELECT aineID, maara FROM KaappiKanta WHERE aineID IN (SELECT aineID FROM OstosKanta)");
        Cursor listatiedot = TK.HaeTiedot(lause);
        while(listatiedot.moveToNext())
        {
            TK.LisaaKaappiin(listatiedot.getInt(0),listatiedot.getInt(1)+100);
        }

        lause = ("SELECT aineID FROM OstosKanta WHERE aineID NOT IN (SELECT aineID FROM KaappiKanta)");
        listatiedot = TK.HaeTiedot(lause);


        while(listatiedot.moveToNext())
        {
            TK.LaitaKaappiin(listatiedot.getInt(0));
        }

        lause = ("SELECT aineID FROM OstosKanta");
        listatiedot = TK.HaeTiedot(lause);


        while(listatiedot.moveToNext())
        {
            TK.PoistaListasta(listatiedot.getInt(0));
        }

        Toast.makeText(Liukuvalikko.this,"Ostokset laitettu kaappiin ja poistettu listasta" , Toast.LENGTH_SHORT).show();
        fillScrollView("SELECT aine, AK.aineID, kuva FROM AineKanta AK, OstosKanta OK WHERE AK.aineID IS OK.aineID");
        listatiedot.close();

    }

}
