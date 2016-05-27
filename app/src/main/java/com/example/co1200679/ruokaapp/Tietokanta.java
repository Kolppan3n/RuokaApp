package com.example.co1200679.ruokaapp;



import android.content.ContentValues;
import android.content.Context;
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
        db.execSQL("create table RuokaKanta (ruokaID INTEGER PRIMARY KEY AUTOINCREMENT, nimi TEXT NOT NULL UNIQUE,resepti TEXT)");
        db.execSQL("create table ReseptiKanta (ruokaID INTEGER NOT NULL,kategoriaID INTEGER NOT NULL, aineID INTEGER NOT NULL)");
        db.execSQL("create table KategoriaKanta (tyyppi TEXT NOT NULL, kategoria TEXT UNIQUE NOT NULL,kategoriaID INTEGER PRIMARY KEY AUTOINCREMENT)");
        db.execSQL("create table AineKanta (aine TEXT UNIQUE NOT NULL,kategoriaID INTEGER NOT NULL, aineID INTEGER PRIMARY KEY AUTOINCREMENT)");

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

    public void TaytaKaappi() {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues tiedot = new ContentValues();
        tiedot.put("aine","makaroni");
        db.insert("KaappiKanta",null,tiedot);
        tiedot.put("aine","spagetti");
        db.insert("KaappiKanta",null,tiedot);
        tiedot.put("aine","jauheliha");
        db.insert("KaappiKanta",null,tiedot);
        tiedot.put("aine","muna");
        db.insert("KaappiKanta",null,tiedot);
        tiedot.put("aine","maito");
        db.insert("KaappiKanta",null,tiedot);
        tiedot.put("aine","tomaatti");
        db.insert("KaappiKanta",null,tiedot);
    }


}
