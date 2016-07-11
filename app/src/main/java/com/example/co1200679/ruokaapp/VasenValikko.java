package com.example.co1200679.ruokaapp;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class VasenValikko extends Fragment {

    Liukuvalikko2 liukkari = (Liukuvalikko2)getActivity();
    /*Tietokanta TK = liukkari.TK;
    String lause = liukkari.lause;
    int moodi = liukkari.moodi;
    int plussa = liukkari.plussa;
    LauseLista LL = liukkari.LL;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_valikko, container, false);
        view.findViewById(R.id.rela).setBackgroundColor(Color.BLUE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateList(/*liukkari.lause, liukkari.TK, VasenValikko.this.getActivity()*/);
    }

    public void populateList(/*String lause, Tietokanta TK, FragmentActivity temp*/) {

        FragmentActivity temp = (FragmentActivity) this.getActivity();
        Tietokanta TK = new Tietokanta(VasenValikko.this.getActivity());
        String lause = "SELECT ruoka nimi, ruokaID _id, kuva, 1 AS moodi  FROM RuokaKanta";
        Cursor tiedot = TK.HaeTiedot(lause);

        String[] columns = new String[]{"nimi", "kuva"};
        int[] viewIDs = new int[]{R.id.teksti, R.id.ikoni};
        SimpleCursorAdapter filleri = new SimpleCursorAdapter(VasenValikko.this.getActivity(), R.layout.valikko_item, tiedot, columns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView valikko = (ListView) temp.findViewById(R.id.valikko);
        valikko.setAdapter(filleri);
    }

}
