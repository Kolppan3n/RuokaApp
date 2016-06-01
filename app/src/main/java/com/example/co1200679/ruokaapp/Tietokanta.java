package com.example.co1200679.ruokaapp;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class Tietokanta extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AppiKanta.db";



    public Tietokanta(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table RuokaKanta (ruoka TEXT NOT NULL,ruokaID INTEGER PRIMARY KEY, resepti TEXT)");
        db.execSQL("create table ReseptiKanta (kantaID INTEGER PRIMARY KEY,ruokaID INTEGER NOT NULL, aineID INTEGER NOT NULL)");
        db.execSQL("create table AineKanta (aine TEXT NOT NULL, aineID INTEGER PRIMARY KEY,edellinenID INTEGER NOT NULL)");

        db.execSQL("create table KaappiKanta (aine TEXT UNIQUE)");
        db.execSQL("create table OstosKanta (aine TEXT UNIQUE)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RuokaKanta");
        db.execSQL("DROP TABLE IF EXISTS ReseptiKanta");
        db.execSQL("DROP TABLE IF EXISTS AineKanta");
        db.execSQL("DROP TABLE IF EXISTS KategoriaKanta");

        db.execSQL("DROP TABLE IF EXISTS OstosKanta");
        db.execSQL("DROP TABLE IF EXISTS KaappiKanta");
        onCreate(db);
    }

    public Cursor HaeTiedot(String haku){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor tulos = db.rawQuery(haku,null);
        return tulos;
    }

    public void LaitaAine(String aine, int aineID, int edellinenID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues tiedot = new ContentValues();
        tiedot.put("aine",aine);
        tiedot.put("aineID",aineID);
        tiedot.put("edellinenID",edellinenID);
        db.insert("AineKanta",null,tiedot);
    }

    public void LaitaResepti(int kantaID, int ruokaID, int aineID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues tiedot = new ContentValues();
        tiedot.put("kantaID",kantaID);
        tiedot.put("ruokaID",ruokaID);
        tiedot.put("aineID",aineID);
        db.insert("ReseptiKanta",null,tiedot);
    }

    public void LaitaRuoka(String ruoka, int ruokaID, String resepti)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues tiedot = new ContentValues();
        tiedot.put("ruoka",ruoka);
        tiedot.put("ruokaID",ruokaID);
        tiedot.put("resepti",resepti);
        db.insert("RuokaKanta",null,tiedot);
    }



}
