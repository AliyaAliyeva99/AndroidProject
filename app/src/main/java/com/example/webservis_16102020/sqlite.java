package com.example.webservis_16102020;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class sqlite  {

    private DBHelper dbHelper;
    SqlLiteMethod sqlLiteMethod;
    Context context;


    public sqlite(Context context) {
        dbHelper = new DBHelper(context);
        sqlLiteMethod = new SqlLiteMethod(context);
        this.context = context;
    }
    public void insertData(String clCode,String clDefn,String clSpecode5){
        sqlLiteMethod.ExecuteCommand("insert into musteriler (clCode,clDefn,clSpecode5) values ('"+clCode+"','"+clDefn+"','"+clSpecode5+"')");
    }
    public void deleteData(String clCode){
        sqlLiteMethod.ExecuteCommand("delete from musteriler where clCode='"+clCode+"'");
    }

    public ArrayList<Model> deyisenMusteriler(){
        ArrayList<Model> siyahi = new ArrayList<Model>();
        SQLiteDatabase db_ = dbHelper.getWritableDatabase();
        String selectQuery = "select clCode,clDefn,clSpecode5 from musteriler";
        Cursor cursor = db_.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String kod=cursor.getString(0);
                String ad=cursor.getString(1);
                String specode=cursor.getString(2);
                Model m1 = new Model();
                m1.setAd(ad);
                m1.setKod(kod);
                m1.setTemsilci(specode);
                siyahi.add(m1);
            } while (cursor.moveToNext());
        }
        return siyahi;
    }

}
