package com.example.webservis_16102020;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; //deyisdirmisem
    private static final String DATABASE_NAME = "istifadeci";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tblMusteriler ="create table musteriler (id integer primary key autoincrement,clCode text,clDefn text,clSpecode5 text)";
        db.execSQL(tblMusteriler);
      //  String MPClients="create table MPClients (id integer primary key autoincrement,Musteri_Kodu text,Musteri_Adi text,Musteri_Borcu text,Risk_Limiti text,Location_x text,Location_y text,Rut text,Endirim_Faizi text,Temsilci_Kodu text,Sale_Status text,Address text,Qiymet_Tipi text,Musteri_Tipi text,Aciqlama2 text,Portifel text,Standart text,Sevkiat_Addressleri text,Group_ text)";
      //  db.execSQL(MPClients);
        Log.e("DATABASE OPERATIONS", "Login DB yaradildi...");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS musteriler");
        onCreate(db);

    }

}