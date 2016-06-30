package com.example.co1200679.ruokaapp;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by co1300608 on 30.5.2016.
 */
public class Kirjastonhoitaja extends AsyncTask <Void,Void,Void> {
    Tietokanta TK;

    Context context;

    public Kirjastonhoitaja(Context context) {
        this.context = context;
        TK = new Tietokanta(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try
        {
            String osoite = "http://atlantis.passoja.fi/~niiranen/ruokaapp/hae_tiedot.php";
            URL url = new URL(osoite);
            HttpURLConnection Yhteys = (HttpURLConnection)url.openConnection();
            InputStream Tietovuoto = Yhteys.getInputStream();
            BufferedReader Lukija = new BufferedReader(new InputStreamReader(Tietovuoto));

            StringBuilder StringiRaksa = new StringBuilder();
            String Rivi;

            while((Rivi=Lukija.readLine())!=null)
            {
                StringiRaksa.append(Rivi+"\n");
            }

            Yhteys.disconnect();

            String json_string = StringiRaksa.toString().trim();

            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray tiedot = jsonObject.getJSONArray("tieto response");

            JSONArray aineet = tiedot.getJSONObject(0).getJSONArray("aine response");
            JSONArray ruuat = tiedot.getJSONObject(1).getJSONArray("ruoka response");
            JSONArray reseptit = tiedot.getJSONObject(2).getJSONArray("resepti response");

            for(int k = 0; k<aineet.length();k++)
            {
                JSONObject JO = aineet.getJSONObject(k);
                TK.LaitaAine(JO.getString("aine"),JO.getInt("aineID"),JO.getInt("edellinenID"),JO.getString("mitta"),JO.getString("kuva"),(float)JO.getDouble("pakkauskoko"));
            }

            for(int k = 0; k<ruuat.length();k++)
            {
                JSONObject JO = ruuat.getJSONObject(k);
                TK.LaitaRuoka(JO.getString("ruoka"), JO.getInt("ruokaID"), JO.getString ("resepti"),JO.getInt("aika"),JO.getInt("taso"),JO.getInt("tarvikkeet"),JO.getString("kuva"));
            }

            for(int k = 0; k<reseptit.length();k++)
            {
                JSONObject JO = reseptit.getJSONObject(k);
                TK.LaitaResepti(JO.getInt("kantaID"), JO.getInt("ruokaID"), JO.getInt("aineID"),(float)JO.getDouble("kpl"));
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
