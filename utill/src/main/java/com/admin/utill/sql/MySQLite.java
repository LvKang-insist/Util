package com.admin.utill.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *  数据库
 */
public class MySQLite extends SQLiteOpenHelper {
    public MySQLite(Context context) {
        super(context, "TEST.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tab1(str1 text,str2 text,str3 text,str4 text,str5 text,str6 text)");
        db.execSQL("create table tab2(str1 text,str2 text,str3 text,str4 text,str5 text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void addTable1(Context context,String str4,String str5,String str6){
        //str4 操作类型  str5 操作对象 str6 日志
        MySQLite mySQLite=new MySQLite(context);
        SQLiteDatabase database = mySQLite.getReadableDatabase();
        ContentValues values=new ContentValues();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(System.currentTimeMillis());
        values.put("str1",date);
        values.put("str2","192.168.1.142");
        values.put("str3","admin");
        values.put("str4",str4);
        values.put("str5",str5);
        values.put("str6",str6);
       database.insert("tab1",null,values);
       values.clear();

    }


    public  static List<String[]> getTable1(Context context){

        List<String[]> list=new ArrayList<>();
        MySQLite mySQLite=new MySQLite(context);
        SQLiteDatabase database = mySQLite.getReadableDatabase();
        Cursor tab1 = database.query("tab1", null, null, null, null, null, null);
        while (tab1.moveToNext()){
            String str1 = tab1.getString(tab1.getColumnIndex("str1"));
            String str2 = tab1.getString(tab1.getColumnIndex("str2"));
            String str3 = tab1.getString(tab1.getColumnIndex("str3"));
            String str4 = tab1.getString(tab1.getColumnIndex("str4"));
            String str5 = tab1.getString(tab1.getColumnIndex("str5"));
            String str6 = tab1.getString(tab1.getColumnIndex("str6"));
          list.add(new String[]{str1,str2,str3,str4,str5,str6});
        }
        tab1.close();
        return list;
    }

    public static void addTable2(Context context,String str1,String str2,String str3){
        //str1 车牌号 str2充值金额  str3 充值后金额

        MySQLite mySQLite=new MySQLite(context);
        SQLiteDatabase database = mySQLite.getReadableDatabase();
        ContentValues values=new ContentValues();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(System.currentTimeMillis());
        values.put("str1",str1);
        values.put("str2",str2);
        values.put("str3",str3);
        values.put("str4","admin");
        values.put("str5",date  );

        database.insert("tab2",null,values);
        values.clear();

    }


}
