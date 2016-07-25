package com.example.co1200679.ruokaapp;

import android.app.Fragment;
import android.content.Intent;
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
import android.widget.RelativeLayout;

public class ToolbarFragment extends Fragment {

    private Toolbar mToolbar;
    private MenuInflater inflater;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.toolbar, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.tb);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu _menu, MenuInflater _inflater) {
        String kasa = "menu_";
        inflater = _inflater;
        menu = _menu;

        if (getActivity().getLocalClassName().toLowerCase().equals("liukuvalikko")) {
            Liukuvalikko akti = (Liukuvalikko) getActivity();
            switch (akti.moodi) {
                case 0: {
                    kasa += "aineet";
                    break;
                }
                case 1: {
                    kasa += "ruuat";
                    break;
                }
                case 2: {
                    kasa += "kaappi";
                    break;
                }
                case 3: {
                    kasa += "lista";
                    break;
                }
                default: {
                    kasa += "default";
                    break;
                }
            }
        } else
            kasa += getActivity().getLocalClassName().toLowerCase().toString();


        int menuID = getResources().getIdentifier(kasa, "menu", getActivity().getPackageName());
        if (menuID != 0)
            inflater.inflate(menuID, menu);
        else
            inflater.inflate(R.menu.menu_default, menu);

        super.onCreateOptionsMenu(menu, inflater);

        if (getActivity().getLocalClassName().toLowerCase().equals("liukuvalikko2")) {
            Liukuvalikko2 akti = (Liukuvalikko2) getActivity();
            akti.Toolbar = this;
            akti.muuttuvaValikko(akti.alkuvalinta());
        }
    }

    public void muutaToolbar(int numero) {
        Log.d("Testi",""+numero);
        menu.clear();
        String kasa = "menu_";
        switch (numero) {
            case 0: {
                kasa += "aineet";
                break;
            }
            case 1: {
                kasa += "ruuat";
                break;
            }
            case 2: {
                kasa += "kaappi";
                break;
            }
            case 3: {
                kasa += "lista";
                break;
            }
            default: {
                kasa += "default";
                break;
            }
        }
        int menuID = getResources().getIdentifier(kasa, "menu", getActivity().getPackageName());
        inflater.inflate(menuID, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.koti: {
                startActivity(new Intent(getActivity(), MainAct.class));
                break;
            }

            case R.id.asetukset: {
                startActivity(new Intent(getActivity(), Asetukset.class));
                break;
            }

            case R.id.resepti: {
                Valikko valikko = (Valikko) getActivity();
                valikko.avaaResepti();
                break;
            }

            case R.id.katsoKaappiin: {
                Valikko valikko = (Valikko) getActivity();
                valikko.katsoKaappiin();
                break;
            }

            case R.id.lisaaListaan: {
                Valikko valikko = (Valikko) getActivity();
                valikko.lisaaListaan();
                break;
            }

            case R.id.kassi: {
                Liukuvalikko Lvalikko = (Liukuvalikko) getActivity();
                Lvalikko.ostoksetKaappiin();
                break;
            }

            case R.id.ruokaMahdollisuudet: {
                Liukuvalikko Lvalikko = (Liukuvalikko) getActivity();
                Lvalikko.ruokaMahdollisuudetKaapinAineksista();
                break;
            }

            case R.id.tila: {
                Liukuvalikko Lvalikko = (Liukuvalikko) getActivity();
                RelativeLayout rela = (RelativeLayout) Lvalikko.findViewById(R.id.liukuRela);

                if (Lvalikko.moodi == 0 || Lvalikko.moodi == 1) {
                    if (Lvalikko.muutaPlussa() == 1) {
                        item.setIcon(R.drawable.menuselaustila);
                        rela.setBackgroundColor(getResources().getColor(R.color.colorBackground2));
                    } else {
                        item.setIcon(R.drawable.menulisaustila);
                        rela.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                    }
                } else {
                    if (Lvalikko.muutaPlussa() == 1) {
                        item.setIcon(R.drawable.menuselaustila);
                        rela.setBackgroundColor(getResources().getColor(R.color.colorBackground2));
                    } else {
                        item.setIcon(R.drawable.menupoistotila);
                        rela.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                    }
                }

                break;
            }
            case R.id.kokkaus: {
                Kokkausohjeet kokkausohjeet = (Kokkausohjeet) getActivity();
                kokkausohjeet.kokkaa();

                break;
            }

            case R.id.testi: {
                Intent intent = new Intent(this.getActivity(), Liukuvalikko2.class);
                intent.putExtra("MoodiVasen", 0);
                intent.putExtra("MoodiOikea", 1);
                intent.putExtra("valitseValikko",1);
                startActivity(intent);
                break;
            }

            default:
                Log.d("Herp", "Derp");
                break;
        }

        //SELECT ruoka nimi, ruokaID _id, kuva, 1 AS moodi  FROM RuokaKanta WHERE ruokaID NOT IN (SELECT ruokaID FROM ReseptiKanta WHERE aineID NOT IN (SELECT aineID FROM KaappiKanta))

        return super.onOptionsItemSelected(item);
    }
}