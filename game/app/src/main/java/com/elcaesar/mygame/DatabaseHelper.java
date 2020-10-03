package com.elcaesar.mygame;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.icu.text.Transliterator.REVERSE;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "game.db";     /////
    public static final String DBLOCATION = "/data/data/com.elcaesar.mygame/databases/";
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

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
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


}
