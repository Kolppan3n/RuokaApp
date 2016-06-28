package com.example.co1200679.ruokaapp;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Tietokanta extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AppiKanta.db";
    SQLiteDatabase db;


    public Tietokanta(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table RuokaKanta (ruoka TEXT NOT NULL,ruokaID INTEGER PRIMARY KEY, resepti TEXT ,kuva TEXT,aika INTEGER,taso INTEGER,tarvikkeet INTEGER)");
        db.execSQL("create table ReseptiKanta (kantaID INTEGER PRIMARY KEY,ruokaID INTEGER NOT NULL, aineID INTEGER NOT NULL,lkm FLOAT NOT NULL)");
        db.execSQL("create table AineKanta (aine TEXT NOT NULL, aineID INTEGER PRIMARY KEY,edellinenID INTEGER NOT NULL ,kuva TEXT, mitta TEXT, pakkauskoko FLOAT)");

        db.execSQL("create table KaappiKanta (aineID INTEGER UNIQUE, maara FLOAT)");
        db.execSQL("create table OstosKanta (aineID INTEGER UNIQUE, kpl INTEGER)");
        db.execSQL("create table VarausKanta (aineID INTEGER UNIQUE, maara FLOAT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RuokaKanta");
        db.execSQL("DROP TABLE IF EXISTS ReseptiKanta");
        db.execSQL("DROP TABLE IF EXISTS AineKanta");
        db.execSQL("DROP TABLE IF EXISTS KategoriaKanta");

        db.execSQL("DROP TABLE IF EXISTS OstosKanta");
        db.execSQL("DROP TABLE IF EXISTS KaappiKanta");
        db.execSQL("DROP TABLE IF EXISTS VarausKanta");
        onCreate(db);
    }

    public Cursor HaeTiedot(String haku){
        Cursor tulos = db.rawQuery(haku,null);
        return tulos;
    }

    //tietokannan päivitystä netistä
    public void LaitaAine(String aine, int aineID, int edellinenID,String mitta,String kuva,Float pakkauskoko)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("aine",aine);
        tiedot.put("aineID",aineID);
        tiedot.put("edellinenID",edellinenID);
        tiedot.put("mitta",mitta);
        tiedot.put("kuva",kuva);
        tiedot.put("pakkauskoko",pakkauskoko);
        db.insert("AineKanta",null,tiedot);
        db.update("AineKanta",tiedot,("aineID is "+aineID),null);
    }

    public void LaitaResepti(int kantaID, int ruokaID, int aineID,float lkm)
    {
        if(lkm==0.0F) {
            db.delete("ReseptiKanta",("kantaID is "+kantaID),null);
        }
        ContentValues tiedot = new ContentValues();
        tiedot.put("kantaID",kantaID);
        tiedot.put("ruokaID",ruokaID);
        tiedot.put("aineID",aineID);
        tiedot.put("lkm",lkm);
        db.insert("ReseptiKanta",null,tiedot);
        db.update("ReseptiKanta",tiedot,("kantaID is "+kantaID),null);
        if(lkm==0.0F)
        {
            db.delete("ReseptiKanta",("kantaID is "+kantaID),null);
        }
    }

    public void LaitaRuoka(String ruoka, int ruokaID, String resepti,int aika ,int taso ,int tarvikkeet, String kuva)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("ruoka",ruoka);
        tiedot.put("ruokaID",ruokaID);
        tiedot.put("resepti",resepti);
        tiedot.put("aika",aika);
        tiedot.put("taso",taso);
        tiedot.put("tarvikkeet",tarvikkeet);
        tiedot.put("kuva",kuva);
        db.insert("RuokaKanta",null,tiedot);
        db.update("RuokaKanta",tiedot,("ruokaID is "+ruokaID),null);
    }


    //kaapin funktiot
    public void LaitaKaappiin(int aineID, float maara)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("aineID",aineID);
        tiedot.put("maara",maara);
        db.insert("KaappiKanta",null,tiedot);
    }

    public void LisaaKaappiin(int aineID, float maara)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("aineID",aineID);
        tiedot.put("maara",maara);
        db.update("KaappiKanta",tiedot,("aineID is "+aineID),null);
    }

    public void MuutaAinetta(int aineID,float uusi)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("maara",uusi);
        db.update("KaappiKanta",tiedot,("aineID is "+aineID),null);
    }

    public void PoistaKaapista(int aineID)
    {
        db.delete("KaappiKanta",("aineID is "+aineID),null);
    }

    //listan funktiot
    public void LaitaListaan(int aineID, int kpl)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("aineID",aineID);
        tiedot.put("kpl",kpl);
        db.insert("OstosKanta",null,tiedot);
    }

    public void LisaaListaan(int aineID, int kpl)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("kpl",kpl);
        db.update("OstosKanta",tiedot,("aineID is "+aineID),null);
    }

    public void PoistaListasta(int aineID)
    {
        db.delete("OstosKanta",("aineID is "+aineID),null);
    }

    //varauslistan funktiot
    public void LaitaVaraus(int aineID,float maara)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("aineID",aineID);
        tiedot.put("maara",maara);
        db.insert("VarausKanta",null,tiedot);
        Log.d("varasu","" + aineID + " " + maara);
    }

    public void VaraaLisaa(int aineID,float uusi)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("maara",uusi);
        db.update("VarausKanta",tiedot,("aineID is "+aineID),null);
    }


    public void PoistaVaraus(int aineID)
    {
        db.delete("VarausKanta",("aineID is "+aineID),null);
    }


}
