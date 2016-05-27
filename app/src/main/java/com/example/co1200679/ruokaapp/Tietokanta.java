package com.example.co1200679.ruokaapp;



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
        db.execSQL("create table RuokaKanta (ruokaID INTEGER PRIMARY KEY AUTOINCREMENT, nimi TEXT NOT NULL,resepti TEXT)");
        db.execSQL("create table ReseptiKanta (ruokaID INTEGER NOT NULL,kategoriaID INTEGER NOT NULL, aineID INTEGER NOT NULL)");
        db.execSQL("create table KategoriaKanta (tyyppi TEXT NOT NULL, kategoria TEXT NOT NULL,kategoriaID INTEGER PRIMARY KEY AUTOINCREMENT)");
        db.execSQL("create table AineKanta (aine TEXT NOT NULL,kategoriaID INTEGER NOT NULL, aineID INTEGER PRIMARY KEY AUTOINCREMENT)");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RuokaKanta");
        db.execSQL("DROP TABLE IF EXISTS ReseptiKanta");
        db.execSQL("DROP TABLE IF EXISTS AineKanta");
        db.execSQL("DROP TABLE IF EXISTS KategoriaKanta");
        onCreate(db);
    }
}
