package com.example.co1200679.ruokaapp;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ToolbarFragment extends Fragment {

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.toolbar, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.tb);
        if (mToolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        String FUCK = "menu_";

        if (getActivity().getLocalClassName().toLowerCase().equals("liukuvalikko")){
            Liukuvalikko akti = (Liukuvalikko) getActivity();
            switch (akti.moodi){
                case 0: {FUCK += "aineet";break;}
                case 1: {FUCK += "ruuat";break;}
                case 2: {FUCK += "kaappi";break;}
                case 3: {FUCK += "lista";break;}
                default: {FUCK += "default"; break;}
            }
        }
        else
            FUCK += getActivity().getLocalClassName().toLowerCase().toString();

        int menuID = getResources().getIdentifier(FUCK, "menu", getActivity().getPackageName());
        if(menuID != 0)
            inflater.inflate(menuID, menu);
        else
            inflater.inflate(R.menu.menu_default, menu);

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.koti: { startActivity(new Intent(getActivity(), MainAct.class)); break;}

            case R.id.resepti: {
                Valikko valikko = (Valikko)getActivity();
                valikko.avaaResepti();
                break;
            }

            case R.id.lisaaListaan: {
                Valikko valikko = (Valikko)getActivity();
                valikko.lisaaListaan();
                break;
            }

            case R.id.kassi: {
                Liukuvalikko Lvalikko = (Liukuvalikko)getActivity();
                Lvalikko.ostoksetKaappiin();
                break;
            }

            default: Log.d("joo", "Derp"); break;

        }

        return super.onOptionsItemSelected(item);
    }
}
