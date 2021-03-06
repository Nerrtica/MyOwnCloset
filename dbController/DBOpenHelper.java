package com.example.caucse.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by caucse on 2018-04-03.
 */

public class DBOpenHelper extends SQLiteOpenHelper{
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //make table, execute once when app is installed
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE closet (_id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, pattern INTEGER, " +
                "color INTEGER, _long INTEGER, image TEXT, FOREIGN KEY (type) REFERENCES clothes_type(_id));");
        db.execSQL("CREATE TABLE clothes_type (_id INTEGER PRIMARY KEY AUTOINCREMENT, category CHAR(10),section CHAR(20));");
        db.execSQL("CREATE TABLE coordi  (_id INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(20) ,outerWear INTEGER REFERENCES closet(_id), top_clothes INTEGER REFERENCES closet(_id)," +
                " botton_clothes INTEGER REFERENCES closet(_id), shoes INTEGER REFERENCES closet(_id));");
        //Toast.makeText(context,"DB is opened",0).show();

        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'outerWear','jacket');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'outerWear','coat');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'outerWear','hoody');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','hoody');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','sweater');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','shirt');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','Tshirt');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'bottom','jeans');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'bottom','pants');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'shoes','sneaker');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'shoes','shoes');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','dress');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','blouse');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'bottom','skirt')");;
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'shoes','heel');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
