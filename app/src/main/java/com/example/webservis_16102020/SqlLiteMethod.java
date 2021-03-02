package com.example.webservis_16102020;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SqlLiteMethod {
    DBHelper sqlLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    public SqlLiteMethod(Context context)
    {
        sqlLiteHelper=new DBHelper(context);
    }



    public String[][] ExecuteCommand(String _query)
    {
        sqLiteDatabase=sqlLiteHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(_query);
        sqLiteDatabase.close();
        return new String[0][];
    }
    public void ExecuteCommand1(String _query,SQLiteDatabase db)
    {
        db.execSQL(_query);
    }
    public  String ExecuteScalar(String _query)
    {
        Object returning="";
        sqLiteDatabase=sqlLiteHelper.getReadableDatabase();
        cursor=sqLiteDatabase.rawQuery(_query, null);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            returning=cursor.getString(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return returning==null?"":returning.toString();
    }
    public String[][] ExecuteSelect(String _query)
    {
        sqLiteDatabase=sqlLiteHelper.getWritableDatabase();
        String[][] _myTable;
        cursor=sqLiteDatabase.rawQuery(_query, null);
        _myTable=new String[cursor.getCount()][cursor.getColumnCount()];
        int _count=0;
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                _myTable[_count][i]=cursor.getString(i);

            }
            _count++;
        }
        cursor.close();
        sqLiteDatabase.close();
        return _myTable;

    }
    public String[] ExecuteSelectUni(String _query)
    {
        sqLiteDatabase=sqlLiteHelper.getWritableDatabase();
        String[] _myTable;
        cursor=sqLiteDatabase.rawQuery(_query, null);
        _myTable=new String[cursor.getCount()];
        int _count=0;
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){


                _myTable[_count]=cursor.getString(0);



            _count++;
        }
        cursor.close();
        sqLiteDatabase.close();
        return _myTable;

    }
    public ArrayList<String> ExecuteSelectList(String _query)
    {
        sqLiteDatabase=sqlLiteHelper.getWritableDatabase();
        ArrayList<String> _myTable;
        cursor=sqLiteDatabase.rawQuery(_query,null);
        _myTable=new ArrayList<String>();
        int _count=0;
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                _myTable.add(cursor.getString(i));
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return _myTable;

    }
    public  CharSequence[] ExecuteSelectChar(String _query)
    {
        sqLiteDatabase=sqlLiteHelper.getWritableDatabase();
        cursor=sqLiteDatabase.rawQuery(_query,null);
        CharSequence[] _charSquence=new CharSequence[cursor.getCount()];
        int _count=0;
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                _charSquence[_count]=cursor.getString(i);

            }
            _count++;
        }
        cursor.close();
        sqLiteDatabase.close();
        return _charSquence;
    }
}
