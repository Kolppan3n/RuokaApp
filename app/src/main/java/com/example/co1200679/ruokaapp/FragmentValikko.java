package com.example.co1200679.ruokaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class FragmentValikko extends Fragment {

    Liukuvalikko2 liukkari;
    ListView valikko;
    Tietokanta TK;
    int moodi;
    int plussa;
    LauseLista LL;


    public boolean takaisin() {
        if (LL.Viimeinen.edellinen == null) {
            return false;
        } else {
            LauseMoodi Temp = LL.Takaisin();
            moodi = Temp.getMoodi();
            populateList(Temp.getLause());
            return true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_valikko, container, false);
        int moodi = getArguments().getInt("moodi", 0);
        valikko = (ListView) view.findViewById(R.id.valikko);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        liukkari = (Liukuvalikko2) (getActivity());
        TK = liukkari.TK;
        String lause = getArguments().getString("sqlqry");
        moodi = getArguments().getInt("moodi", 0);

        LL = new LauseLista();
        LL.UusiLause(lause, moodi);

        plussa = 0;
        if (moodi == 3) varauksetOstoksiksi();
        populateList(lause);
    }

    public void populateList(String lause) {

        Cursor tiedot = TK.HaeTiedot(lause);

        String[] columns = new String[]{"nimi", "kuva"};
        int[] viewIDs = new int[]{R.id.teksti, R.id.ikoni};
        SimpleCursorAdapter filleri = new SimpleCursorAdapter(FragmentValikko.this.getActivity(), R.layout.valikko_item, tiedot, columns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

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
        valikko.setAdapter(filleri);


        valikko.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ItemInfo temp = (ItemInfo) view.findViewById(R.id.teksti);
                int moodi = temp.getMoodi();
                int ID = temp.getID();
                String nimi = temp.getNimi();

                switch (moodi) {
                    case 0: {

                        LinearLayout pika = (LinearLayout) view.findViewById(R.id.pikavalikko);
                        if (pika.getVisibility() == View.VISIBLE) {
                            Log.d("asdasdasd", "asdasdasd");
                            pika.setVisibility(View.GONE);
                        } else {
                            Cursor lasku = TK.HaeTiedot("SELECT count(aineID) AS luku FROM AineKanta WHERE edellinenID IS " + ID);
                            lasku.moveToNext();

                            if (lasku.getInt(0) == 0) {
                                lasku.close();
                                avaaRuuat(ID);
                            } else {
                                String lause = ("SELECT aine nimi, aineID _id, kuva, " + moodi + " as moodi FROM AineKanta WHERE edellinenID is " + ID);
                                lasku.close();
                                LL.UusiLause(lause, 0);
                                populateList(lause);
                            }
                        }
                        break;
                    }
                    case 1: {
                        if (plussa == 0) {
                            Intent intent = new Intent(liukkari, Valikko.class);
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
                            temp.setTextColor(ContextCompat.getColor(liukkari, R.color.colorTextSecondary));
                            temp.setPaintFlags(temp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            temp.setTag("Checked");
                        } else {
                            temp.setTextColor(ContextCompat.getColor(liukkari, R.color.colorTextPrimary));
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

        valikko.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ItemInfo temp = (ItemInfo) view.findViewById(R.id.teksti);
                LinearLayout pika = (LinearLayout) view.findViewById(R.id.pikavalikko);
                int moodi = temp.getMoodi();
                int ID = temp.getID();

                switch (moodi) {

                    case 0: {
                        pika.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(liukkari, Kokkausohjeet.class);
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

                return true;
            }
        });
    }

    public void avaaRuuat(int ID) {

        Cursor lasku = TK.HaeTiedot("SELECT count(ruokaID) AS luku FROM ReseptiKanta WHERE aineID IS " + ID);
        lasku.moveToNext();

        if (lasku.getInt(0) != 0) {
            String lause = ("SELECT ruoka nimi, RU.ruokaID _id, kuva, 1 AS moodi FROM RuokaKanta RU, ReseptiKanta RE WHERE RU.ruokaID IS RE.ruokaID AND RE.aineID IS " + ID);
            lasku.close();
            liukkari.avaaRuuat(lause);
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

        Toast.makeText(liukkari, nimi + " ainekset lisättiin ostoslistalle", Toast.LENGTH_SHORT).show();
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
        int color = getResources().getColor(R.color.colorRed);
        if (prosentti > 0.25) {
            if (prosentti > 0.5) {
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
        LL.UusiLause(lause, 1);
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

        Toast.makeText(liukkari, "Ostokset laitettu kaappiin ja poistettu listasta", Toast.LENGTH_SHORT).show();
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
