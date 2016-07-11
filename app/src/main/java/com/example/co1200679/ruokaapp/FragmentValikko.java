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


public class FragmentValikko extends Fragment {

    Liukuvalikko2 liukkari;
    ListView valikko;
    Tietokanta TK;
    int moodi;
    int plussa;
    LauseLista LL;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_valikko, container, false);
        int moodi = getArguments().getInt("moodi",0);
        if(moodi==1)view.findViewById(R.id.rela).setBackgroundColor(Color.BLUE);
        else view.findViewById(R.id.rela).setBackgroundColor(Color.GREEN);
        valikko = (ListView) view.findViewById(R.id.valikko);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TK = new Tietokanta(getContext());
        String lause = getArguments().getString("sqlqry");
        populateList(lause, TK);
    }

    public void populateList(String lause, Tietokanta TK) {

        Cursor tiedot = TK.HaeTiedot(lause);

        String[] columns = new String[]{"nimi", "kuva"};
        int[] viewIDs = new int[]{R.id.teksti, R.id.ikoni};
        SimpleCursorAdapter filleri = new SimpleCursorAdapter(FragmentValikko.this.getActivity(), R.layout.valikko_item, tiedot, columns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        valikko.setAdapter(filleri);
    }

}
