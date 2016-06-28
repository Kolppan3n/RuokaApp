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
    int moodi;// AINE = 0 RUUAT = 1 KAAPPI = 2 LISTA = 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liukuvalikko);

        TK = new Tietokanta(this);
        String lause = getIntent().getStringExtra("sqlqry");
        moodi = getIntent().getIntExtra("moodi",0);
        if (moodi==3)varauksetOstoksiksi();
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

        Log.d("pappapa", DatabaseUtils.dumpCursorToString(tiedot));

        while(tiedot.moveToNext()) {
            temp = getLayoutInflater().inflate(R.layout.item, content, false);
            item = (ItemInfo) temp.findViewById(R.id.itemView);
            item.setNimi(tiedot.getString(0));
            if(moodi==3) item.setNimi(tiedot.getInt(3) + " X " + tiedot.getString(0));
            item.setID(tiedot.getInt(1));
            item.setMoodi(moodi);
            item.setText(item.getNimi());
            if(moodi==2)
            {
                item.setKpl("" + (tiedot.getFloat(3)/tiedot.getFloat(4)));
                KaappiVari(item);
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
                    Log.d("swipeleft","toimii");
                    if(i.getMoodi()==0)
                    {
                        laitaListaan(i.getID(),1);
                        Toast.makeText(Liukuvalikko.this, i.getText() + " lisättiin ostoslistalle", Toast.LENGTH_SHORT).show();
                    }
                    if(i.getMoodi()==1)
                    {
                        laitaRuokaListaan(i);
                        Toast.makeText(Liukuvalikko.this,i.getText() + " ainekset lisättiin varauslistalle", Toast.LENGTH_SHORT).show();
                    }
                    if(i.getMoodi()==2)
                    {
                        //KaappiVari(i);//vähentää 10% ruuan määrästä ja laittaa värin ei toimi enää kunnolla, koska prosentit muuttui oikeiksi luvuiksi
                    }
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
            icon = (ImageView) temp.findViewById(R.id.imageView);
            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.tomaatti);//tähän joku hyvä kuva
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
                String lause = ("SELECT ruoka, RU.ruokaID, kuva FROM RuokaKanta RU, ReseptiKanta RE WHERE RU.ruokaID IS RE.ruokaID AND RE.aineID IS " + v.getID());
                intent.putExtra("sqlqry", lause);
                intent.putExtra("moodi", 1);
                startActivity(intent);
            }
        }
    }

    public void laitaListaan(int aineID,int maara){
        Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku, kpl FROM OstosKanta WHERE aineID IS " + aineID);
        lasku.moveToNext();

        if(lasku.getInt(0)==0)
        {
            TK.LaitaListaan(aineID,maara);
        }
        else
        {
            TK.LisaaListaan(aineID,lasku.getInt(1)+maara);
        }

    }

    public void laitaRuokaListaan(ItemInfo v){

        String lause = ("SELECT aineID, ruokaID,lkm FROM ReseptiKanta WHERE RuokaID IS " + v.getID());
        Cursor listatiedot = TK.HaeTiedot(lause);

        while(listatiedot.moveToNext())
        {
            laitaVaraus(listatiedot.getInt(0),listatiedot.getFloat(2));
        }

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

    public void KaappiVari(ItemInfo v)
    {
        if(v.getMoodi() == 2) {
            int color = getResources().getColor(R.color.colorRed);
            float prosentti = Float.parseFloat(v.getKpl());
            if (prosentti>0.25)
            {
                if (prosentti>0.5)
                {
                    if (prosentti>0.75)
                    {
                        if (prosentti>0.99)
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

        String lause = ("SELECT AK.aineID, maara,kpl, pakkauskoko FROM KaappiKanta KK, AineKanta AK , OstosKanta OK WHERE AK.aineID IS KK.aineID AND AK.aineID IS OK.aineID AND OK.aineID IN (SELECT aineID FROM OstosKanta)");
        Cursor listatiedot = TK.HaeTiedot(lause);

        Log.d("varauslaise",lause);
        Log.d("varauslista", DatabaseUtils.dumpCursorToString(listatiedot));
        while(listatiedot.moveToNext())
        {
            TK.LisaaKaappiin(listatiedot.getInt(0),(listatiedot.getFloat(1)+ (listatiedot.getInt(2)*listatiedot.getFloat(3))));
        }

        lause = ("SELECT AK.aineID, kpl, pakkauskoko FROM OstosKanta OK, AineKanta AK WHERE OK.aineID is AK.aineID AND OK.aineID NOT IN (SELECT aineID FROM KaappiKanta)");
        listatiedot = TK.HaeTiedot(lause);

        Log.d("varauslaise",lause);
        Log.d("varauslista", DatabaseUtils.dumpCursorToString(listatiedot));

        while(listatiedot.moveToNext())
        {
            TK.LaitaKaappiin(listatiedot.getInt(0),listatiedot.getInt(1)*listatiedot.getFloat(2));
        }




        lause = ("SELECT aineID FROM OstosKanta");
        listatiedot = TK.HaeTiedot(lause);

        while(listatiedot.moveToNext())
        {
            TK.PoistaListasta(listatiedot.getInt(0));
        }

        Toast.makeText(Liukuvalikko.this,"Ostokset laitettu kaappiin ja poistettu listasta" , Toast.LENGTH_SHORT).show();
        fillScrollView("SELECT aine, AK.aineID, kuva FROM AineKanta AK, OstosKanta OK WHERE AK.aineID IS OK.aineID");

    }

    public void varauksetOstoksiksi()
    {
        //Ostoslistalle sopiva määrä aineita, joita ei ole valmiiksi kaapissa
        String lause = ("SELECT VK.aineID, VK.maara, pakkauskoko FROM VarausKanta VK, aineKanta AK WHERE VK.aineID IS AK.aineID AND VK.aineID NOT IN (select aineID FROM KaappiKanta)");
        Cursor listatiedot = TK.HaeTiedot(lause);

        while(listatiedot.moveToNext())
        {
            float pakkauskoko = listatiedot.getFloat(2);
            float tarve = listatiedot.getFloat(1);

            int osto = (int) (tarve / pakkauskoko);
            if (tarve % pakkauskoko >0) osto++;
            laitaListaan(listatiedot.getInt(0),osto);
            TK.PoistaVaraus(listatiedot.getInt(0));
        }

        //Ostoslistalle sopiva märää aineita, joita löytyy jo valmiiksi kaapista
        lause = ("SELECT VK.aineID, VK.maara, KK.maara, pakkauskoko FROM VarausKanta VK, aineKanta AK, KaappiKanta KK WHERE VK.aineID IS AK.aineID AND VK.aineID IS KK.aineID AND VK.aineID IN (select aineID FROM KaappiKanta)");
        listatiedot = TK.HaeTiedot(lause);

        while(listatiedot.moveToNext())
        {
            float pakkauskoko = listatiedot.getFloat(3);
            float tarve = listatiedot.getFloat(1)-listatiedot.getFloat(2);

            int osto = (int) (tarve / pakkauskoko);
            if (tarve % pakkauskoko >0) osto++;
            laitaListaan(listatiedot.getInt(0),osto);
            TK.PoistaVaraus(listatiedot.getInt(0));
        }

    }

    public void laitaVaraus(int aineID,float maara){
        Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku, maara FROM VarausKanta WHERE aineID IS " + aineID);
        lasku.moveToNext();

        if(lasku.getInt(0)==0)
        {
            TK.LaitaVaraus(aineID,maara);
        }
        else
        {
            TK.VaraaLisaa(aineID,lasku.getFloat(1)+maara);
        }

    }

}
