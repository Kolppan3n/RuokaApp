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
        db.execSQL("create table RuokaKanta (nimi TEXT NOT NULL UNIQUE,ruokaID INTEGER PRIMARY KEY AUTOINCREMENT, resepti TEXT)");
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


}
