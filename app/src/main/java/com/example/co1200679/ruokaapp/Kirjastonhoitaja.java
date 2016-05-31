package com.example.co1200679.ruokaapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

            String osoite = "http://atlantis.passoja.fi/~niiranen/ruokaapp/hae_ainekset.php";
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

            //Log.d("JSON_STRING",json_string);

            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray jsonArray = jsonObject.getJSONArray("server response");

            for(int k = 0; k<jsonArray.length();k++)
            {
                JSONObject JO = jsonArray.getJSONObject(k);
                TK.LaitaAine(JO.getString("aine"),JO.getInt("aineID"),JO.getInt("edellinenID"));
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
