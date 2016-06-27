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
        String FUCK = "menu_" + getActivity().getLocalClassName().toLowerCase();
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
        Valikko akti = (Valikko)getActivity();

        switch (id){

            case R.id.koti: { startActivity(new Intent(getActivity(), MainAct.class)); break;}

            case R.id.resepti: {
                Intent intent = new Intent(getActivity(), Kokkausohjeet.class);
                intent.putExtra("ruokaID", akti.ruokaID);
                startActivity(intent);
                break;
            }

            case R.id.lisaaListaan: {
                String lause = ("SELECT aineID, ruokaID FROM ReseptiKanta WHERE aineID NOT IN (SELECT aineID FROM KaappiKanta WHERE maara > 10) AND  RuokaID IS " + akti.ruokaID);
                Cursor listatiedot = akti.TK.HaeTiedot(lause);
                Log.d("popopopo",lause);
                Log.d("pappapa", DatabaseUtils.dumpCursorToString(listatiedot));
                while(listatiedot.moveToNext()) {
                    akti.TK.LaitaListaan(listatiedot.getInt(0));
                }

                String toasti = "Tarvittavat aineet\nlisätty Ostoslistaan";
                Toast.makeText(getActivity(), toasti, Toast.LENGTH_SHORT).show();
                break;
            }

            default: Log.d("joo", "Derp"); break;

        }

        return super.onOptionsItemSelected(item);
    }
}
