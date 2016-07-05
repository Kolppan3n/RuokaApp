package com.example.co1200679.ruokaapp;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Kokkausohjeet extends AppCompatActivity {

    Tietokanta TK;
    int ruokaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kokkausohjeet);

        TK = new Tietokanta(this);
        ruokaID = getIntent().getIntExtra("ruokaID", 0);
        String lause = ("SELECT ruoka, ruokaID, resepti FROM RuokaKanta WHERE RuokaID IS " + ruokaID);
        Cursor tiedot = TK.HaeTiedot(lause);
        tiedot.moveToNext();
        loadRecipe(tiedot);
        fillScrollView();
    }

    public void loadRecipe(Cursor tiedot) {
        TextView otsikko = (TextView) findViewById(R.id.ruuanNimi);
        otsikko.setText(tiedot.getString(0));
        TextView resepti = (TextView) findViewById(R.id.ohjeKontti);
        resepti.setText(tiedot.getString(2));
    }

    public void setListViewHeight(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public void fillScrollView() {

        String lause = ("SELECT aine _id, lkm || ' ' || mitta AS ainemitta FROM AineKanta AK, ReseptiKanta RK WHERE AK.aineID IS RK.aineID AND RK.RuokaID IS " + ruokaID);
        final Cursor tiedot = TK.HaeTiedot(lause);

        ListView kontti = (ListView) findViewById(R.id.ainesKontti);

        startManagingCursor(tiedot);

        String[] columns = new String[]{"_id", "ainemitta"};
        int[] viewIDs = new int[]{R.id.aine, R.id.lkm};
        SimpleCursorAdapter filleri = new SimpleCursorAdapter(this, R.layout.ainesosa, tiedot, columns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        kontti.setAdapter(filleri);
        setListViewHeight(kontti);

    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /*public void toggleA(View view) {
        LinearLayout lio = (LinearLayout) findViewById(R.id.ainesKontti);
        LinearLayout.LayoutParams lap = (LinearLayout.LayoutParams)lio.getLayoutParams();

        if (lap.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            lap.height = 0;
            lap.setMargins(0, 0, 0, 0);
            lio.setLayoutParams(lap);
        } else {
            lap.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lap.setMargins(0, 0, 0, 30);
            lio.setLayoutParams(lap);
        }
    }

    public void toggleO(View view) {
        RelativeLayout lio = (RelativeLayout) findViewById(R.id.ohjeKontti);
        ViewGroup.LayoutParams lap = lio.getLayoutParams();

        if (lap.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            lap.height = 0;
            lio.setLayoutParams(lap);
        } else {
            lap.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lio.setLayoutParams(lap);
        }
    }*/
}
