package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Liukuvalikko extends AppCompatActivity {

    Tietokanta TK;
    int moodi;// AINE = 0 RUUAT = 1 KAAPPI = 2 LISTA = 3
    int plussa;
    LauseLista LL;


    @Override
    public void onBackPressed() {
        if (LL.Viimeinen.edellinen == null) {
            this.finish();
        }
        else{
            LauseMoodi Temp = LL.Takaisin();
            moodi=Temp.getMoodi();
            populateList(Temp.getLause());
        }

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liukuvalikko);

        TK = new Tietokanta(this);
        String lause = getIntent().getStringExtra("sqlqry");
        moodi = getIntent().getIntExtra("moodi", 0);
        LL = new LauseLista();
        LL.UusiLause(lause,moodi);

        plussa = 0;
        if (moodi == 3) varauksetOstoksiksi();
        populateList(lause);


        ImageView img = (ImageView) this.findViewById(R.id.tilaIkoni);
        if (moodi == 2) {
            img.setImageResource(R.drawable.poistotila);
        } else
            img.setImageResource(R.drawable.lisaustila);
    }

    public void populateList(String lause) {

        Cursor tiedot = TK.HaeTiedot(lause);
        startManagingCursor(tiedot);
        Log.d("Tietoa", DatabaseUtils.dumpCursorToString(tiedot));

        String[] columns = new String[]{"nimi", "kuva"};
        int[] viewIDs = new int[]{R.id.teksti, R.id.ikoni};
        SimpleCursorAdapter filleri = new SimpleCursorAdapter(this, R.layout.valikko_item, tiedot, columns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        filleri.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if (view.getId() == R.id.teksti) {
                    ItemInfo temp = (ItemInfo) view.findViewById(R.id.teksti);
                    String nimi = cursor.getString(cursor.getColumnIndex("nimi"));
                    int ruokaID = cursor.getInt(cursor.getColumnIndex("_id"));
                    int moodi = cursor.getInt(cursor.getColumnIndex("moodi"));
                    temp.setNimi(nimi);
                    temp.setID(ruokaID);
                    temp.setMoodi(moodi);
                    if (moodi == 2) {
                        float prosentti = cursor.getFloat(cursor.getColumnIndex("prosentti"));
                        LinearLayout hermanni = (LinearLayout) temp.getParent();
                        hermanni.setBackgroundColor(KaappiVari(prosentti));
                    }
                }

                return false;
            }
        });

        ListView valikko = (ListView) findViewById(R.id.valikko);
        valikko.setAdapter(filleri);
        valikko.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ItemInfo temp = (ItemInfo) view.findViewById(R.id.teksti);
                int moodi = temp.getMoodi();
                int ID = temp.getID();

                switch (moodi) {

                    case 0: {
                        avaaRuuat(ID);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(Liukuvalikko.this, Kokkausohjeet.class);
                        intent.putExtra("ruokaID", ID);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        break;
                    }

                    case 3: {
                        TK.PoistaListasta(ID);
                        populateList("SELECT kpl|| ' X '||aine  AS nimi, AK.aineID _id, kuva,3 AS moodi FROM AineKanta AK, OstosKanta OK WHERE AK.aineID IS OK.aineID");
                        break;
                    }


                    default:
                        Log.d("Herp", "Derp");
                }

                return false;
            }
        });

        valikko.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ItemInfo temp = (ItemInfo) view.findViewById(R.id.teksti);
                int moodi = temp.getMoodi();
                int ID = temp.getID();
                String nimi = temp.getNimi();

                switch (moodi) {
                    case 0: {
                        if (plussa == 0) {
                            Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku FROM AineKanta WHERE edellinenID IS " + ID);
                            lasku.moveToNext();

                            if (lasku.getInt(0) == 0) {
                                lasku.close();
                                avaaRuuat(ID);
                            } else {
                                String lause = ("SELECT aine nimi, aineID _id, kuva, " + moodi + " as moodi FROM AineKanta WHERE edellinenID is " + ID);
                                lasku.close();
                                LL.UusiLause(lause,0);
                                populateList(lause);
                            }
                        } else {
                            laitaListaan(ID, 1);
                            Toast.makeText(Liukuvalikko.this, nimi + " lisättiin ostoslistalle", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case 1: {
                        if (plussa == 0) {
                            Intent intent = new Intent(Liukuvalikko.this, Valikko.class);
                            intent.putExtra("ruokaID", ID);
                            startActivity(intent);
                        } else {
                            laitaRuokaListaan(ID, nimi);
                        }
                        break;
                    }
                    case 2: {
                        break;
                    }

                    case 3: {
                        if (temp.getTag().equals("")) {
                            temp.setTextColor(ContextCompat.getColor(Liukuvalikko.this, R.color.colorTextSecondary));
                            temp.setPaintFlags(temp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            temp.setTag("Checked");
                        } else {
                            temp.setTextColor(ContextCompat.getColor(Liukuvalikko.this, R.color.colorTextPrimary));
                            temp.setPaintFlags(temp.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            temp.setTag("");
                        }
                        break;
                    }
                    default:
                        Log.d("Herp", "Derp");
                }
            }
        });
    }

/*          item.setOnTouchListener(new OnSwipeTouchListener(item) {
                public boolean onSwipeRight() {
                        Toast.makeText(Liukuvalikko.this, "Ateriat joissa tarvitaan " + i.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                public boolean onSwipeLeft() {
                    Log.d("swipeleft","toimii");
                    }return true;}
            });*/


    public void avaaRuuat(int ID) {

        Cursor lasku = TK.HaeTiedot("SELECT count(ruokaID) AS luku FROM ReseptiKanta WHERE aineID IS " + ID);
        lasku.moveToNext();

        if (lasku.getInt(0) != 0) {
            String lause = ("SELECT ruoka nimi, RU.ruokaID _id, kuva, 1 AS moodi FROM RuokaKanta RU, ReseptiKanta RE WHERE RU.ruokaID IS RE.ruokaID AND RE.aineID IS " + ID);
            lasku.close();
            LL.UusiLause(lause,1);
            populateList(lause);
        }
    }


    public int muutaPlussa() {
        plussa ^= 1;
        return plussa;
    }

    public void laitaListaan(int aineID, int maara) {
        Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku, kpl FROM OstosKanta WHERE aineID IS " + aineID);
        lasku.moveToNext();
        if (maara > 0) {
            if (lasku.getInt(0) == 0) {
                TK.LaitaListaan(aineID, maara);
            } else {
                TK.LisaaListaan(aineID, lasku.getInt(1) + maara);
            }
        }
        lasku.close();
    }

    public void laitaRuokaListaan(int ID, String nimi) {

        String lause = ("SELECT aineID, ruokaID,lkm , toiminta FROM ReseptiKanta WHERE NOT toiminta & 6 AND RuokaID IS " + ID);
        Cursor listatiedot = TK.HaeTiedot(lause);

        while (listatiedot.moveToNext()) {
            laitaVaraus(listatiedot.getInt(0), listatiedot.getFloat(2));
        }

        Toast.makeText(Liukuvalikko.this, nimi + " ainekset lisättiin ostoslistalle", Toast.LENGTH_SHORT).show();
        listatiedot.close();

    }

    public void laitaVaraus(int aineID, float maara) {
        Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku, maara FROM VarausKanta WHERE aineID IS " + aineID);
        lasku.moveToNext();

        if (lasku.getInt(0) == 0) {
            TK.LaitaVaraus(aineID, maara);
        } else {
            TK.VaraaLisaa(aineID, lasku.getFloat(1) + maara);
        }
        lasku.close();
    }


    public int KaappiVari(float prosentti) {
        Log.d("prosetnt",prosentti+"");
        int color = getResources().getColor(R.color.colorRed);
        if (prosentti > 0.25) {
            if (prosentti > 0.50) {
                if (prosentti > 0.75) {
                    if (prosentti > 0.99) {
                        color = getResources().getColor(R.color.colorGreen);
                    } else color = getResources().getColor(R.color.colorYellowGreen);
                } else color = getResources().getColor(R.color.colorYellow);
            } else color = getResources().getColor(R.color.colorOrange);
        }
        return color;
    }

    public void ruokaMahdollisuudetKaapinAineksista() {
        String lause = ("SELECT ruoka nimi, ruokaID _id, kuva, 1 AS moodi  FROM RuokaKanta WHERE ruokaID NOT IN (SELECT ruokaID FROM ReseptiKanta WHERE NOT toiminta & 6 AND aineID NOT IN (SELECT aineID FROM KaappiKanta))");
        LL.UusiLause(lause,1);
        populateList(lause);
    }


    public void ostoksetKaappiin() {
        String lause = ("SELECT AK.aineID, maara,kpl, pakkauskoko FROM KaappiKanta KK, AineKanta AK , OstosKanta OK WHERE AK.aineID IS KK.aineID AND AK.aineID IS OK.aineID AND OK.aineID IN (SELECT aineID FROM OstosKanta)");
        Cursor listatiedot = TK.HaeTiedot(lause);

        Log.d("varauslaise", lause);
        Log.d("varauslista", DatabaseUtils.dumpCursorToString(listatiedot));
        while (listatiedot.moveToNext()) {
            TK.MuutaKaappia(listatiedot.getInt(0), (listatiedot.getFloat(1) + (listatiedot.getInt(2) * listatiedot.getFloat(3))));
        }

        lause = ("SELECT AK.aineID, kpl, pakkauskoko FROM OstosKanta OK, AineKanta AK WHERE OK.aineID is AK.aineID AND OK.aineID NOT IN (SELECT aineID FROM KaappiKanta)");
        listatiedot = TK.HaeTiedot(lause);

        Log.d("varauslaise", lause);
        Log.d("varauslista", DatabaseUtils.dumpCursorToString(listatiedot));

        while (listatiedot.moveToNext()) {
            TK.LaitaKaappiin(listatiedot.getInt(0), listatiedot.getInt(1) * listatiedot.getFloat(2));
        }

        lause = ("SELECT aineID FROM OstosKanta");
        listatiedot = TK.HaeTiedot(lause);

        while (listatiedot.moveToNext()) {
            TK.PoistaListasta(listatiedot.getInt(0));
        }

        Toast.makeText(Liukuvalikko.this, "Ostokset laitettu kaappiin ja poistettu listasta", Toast.LENGTH_SHORT).show();
        populateList("SELECT kpl|| ' X '||aine  AS nimi, AK.aineID _id, kuva,3 AS moodi FROM AineKanta AK, OstosKanta OK WHERE AK.aineID IS OK.aineID");
        listatiedot.close();
    }

    public void varauksetOstoksiksi() {
        //Ostoslistalle sopiva määrä aineita, joita ei ole valmiiksi kaapissa
        String lause = ("SELECT VK.aineID, VK.maara, pakkauskoko FROM VarausKanta VK, aineKanta AK WHERE VK.aineID IS AK.aineID AND VK.aineID NOT IN (select aineID FROM KaappiKanta)");
        Cursor listatiedot = TK.HaeTiedot(lause);

        while (listatiedot.moveToNext()) {
            float pakkauskoko = listatiedot.getFloat(2);
            float tarve = listatiedot.getFloat(1);

            int osto = (int) (tarve / pakkauskoko);
            if (tarve % pakkauskoko > 0) osto++;

            laitaListaan(listatiedot.getInt(0), osto);
            TK.PoistaVaraus(listatiedot.getInt(0));
        }

        //Ostoslistalle sopiva märää aineita, joita löytyy jo valmiiksi kaapista
        lause = ("SELECT VK.aineID, VK.maara, KK.maara, pakkauskoko FROM VarausKanta VK, aineKanta AK, KaappiKanta KK WHERE VK.aineID IS AK.aineID AND VK.aineID IS KK.aineID AND VK.aineID IN (select aineID FROM KaappiKanta)");
        listatiedot = TK.HaeTiedot(lause);

        while (listatiedot.moveToNext()) {
            float pakkauskoko = listatiedot.getFloat(3);
            float tarve = listatiedot.getFloat(1) - listatiedot.getFloat(2);

            int osto = (int) (tarve / pakkauskoko);
            if (tarve % pakkauskoko > 0) osto++;
            laitaListaan(listatiedot.getInt(0), osto);
            TK.PoistaVaraus(listatiedot.getInt(0));
        }
        listatiedot.close();

    }


}
