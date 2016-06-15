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
        db.execSQL("create table RuokaKanta (ruoka TEXT NOT NULL,ruokaID INTEGER PRIMARY KEY, resepti TEXT,aika INTEGER,taso INTEGER,tarvikkeet INTEGER,kuva TEXT)");
        db.execSQL("create table ReseptiKanta (kantaID INTEGER PRIMARY KEY,ruokaID INTEGER NOT NULL, aineID INTEGER NOT NULL,lkm FLOAT NOT NULL)");
        db.execSQL("create table AineKanta (aine TEXT NOT NULL, aineID INTEGER PRIMARY KEY,edellinenID INTEGER NOT NULL,mitta TEXT,kuva TEXT)");

        db.execSQL("create table KaappiKanta (aineID INTEGER UNIQUE)");
        db.execSQL("create table OstosKanta (aineID INTEGER UNIQUE)");
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
        Cursor tulos = db.rawQuery(haku,null);
        return tulos;
    }

    public void LaitaAine(String aine, int aineID, int edellinenID,String mitta,String kuva)
    {
        ContentValues tiedot = new ContentValues();
        tiedot.put("aine",aine);
        tiedot.put("aineID",aineID);
        tiedot.put("edellinenID",edellinenID);
        tiedot.put("mitta",mitta);
        tiedot.put("kuva",kuva);
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



}
