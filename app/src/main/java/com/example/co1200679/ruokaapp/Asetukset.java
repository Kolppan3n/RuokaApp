package com.example.co1200679.ruokaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Asetukset extends AppCompatActivity {

    Tietokanta TK;
    Kirjastonhoitaja kirja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset);
        TK = new Tietokanta(this);
    }

    public void paivitaTietokanta(View view) {
        kirja = new Kirjastonhoitaja(this);
        kirja.execute();
        Toast.makeText(this, "Tietokannat Päivitetty!", Toast.LENGTH_SHORT).show();
    }

    public void SQLNappi(View view) {
        TextView hermanni = (TextView) findViewById(R.id.SQLKysely);
        String lause =  hermanni.getText().toString();
        Intent intent = new Intent(this, Liukuvalikko.class);
        intent.putExtra("sqlqry", lause);
        intent.putExtra("moodi", 1);
        startActivity(intent);
    }

    public void alustaTietokanta(View view) {
        kirja = new Kirjastonhoitaja(this);
        new AlertDialog.Builder(this)
                .setTitle("Alustetaanko tietokannat")
                .setMessage("Alustaminen hävittää tiedot myös kaapista ja ostoslistasta! \nOletko aivan varma?")
                .setIcon(R.drawable.errori)
                .setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TK.onUpgrade(TK.getReadableDatabase(), 1, 1); //Tällä saadaan tehtyä uusi versio tietokannasta, mutta tiedot poistu
                        kirja.execute();
                        Toast.makeText(Asetukset.this, "Tietokannat Alustettu!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("EI", null).show();

    }
}
