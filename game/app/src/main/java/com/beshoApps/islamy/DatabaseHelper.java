package com.beshoApps.islamy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "mygame.db";     /////
    public static final String DBLOCATION = "/data/data/com.beshoApps.islamy/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }


    /*public boolean checkDataBase() {
        File dbFile = mContext.getDatabasePath(DBNAME);
        if (dbFile.exists()) return true;
        if (!Objects.requireNonNull(dbFile.getParentFile()).exists()) {
            dbFile.getParentFile().mkdirs();
        }
        return false;
    }*/


    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.ENABLE_WRITE_AHEAD_LOGGING);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }

    public List<Questions> getListProduct() {
        Questions product;
        List<Questions> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Questions", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Questions(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getString(6));
            productList.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }

    public List<QuesTrueFalse> getListProduct2() {
        QuesTrueFalse product;
        List<QuesTrueFalse> productList2 = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM TrueFalse", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new QuesTrueFalse(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            productList2.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList2;
    }

    public List<Names> getListProduct3() {
        Names product;
        List<Names> productList3 = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Names", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Names(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                    , cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)
                    , cursor.getString(9), cursor.getString(10), cursor.getString(11));
            productList3.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList3;
    }

    public List<myAhadith> getListProduct4() {
        myAhadith product;
        List<myAhadith> productList4 = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM hadith", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new myAhadith( cursor.getString(0), cursor.getString(1));
            productList4.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList4;
    }


}
