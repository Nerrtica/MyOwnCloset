package com.capstone.mycloset;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ladma on 2018-04-03.
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
<<<<<<< HEAD
        db.execSQL("CREATE TABLE coordi (_id INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(20), top_clothes ChAR(10), botton_clothes CHAR(10));");
        //Toast.makeText(context,"DB is opened",0).show();

=======
        db.execSQL("CREATE TABLE coordi  (_id INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(20), outerWear CHAR(20), top_clothes ChAR(10), botton_clothes CHAR(10));");

        //Toast.makeText(context,"DB is opened",0).show();
        
>>>>>>> 730d33606c1551b63ea48bcc8c8559d416696583
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
<<<<<<< HEAD
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','dress');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'top','blouse');");
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'bottom','skirt')");;
        db.execSQL("INSERT INTO clothes_type VALUES(NULL, 'shoes','heel');");
=======
        db.execSQL("INSERT INTO clothes-type VALUES(NULL, 'top','dress');");
        db.execSQL("INSERT INTO clothes-type VALUES(NULL, 'top','blouse');");
        db.execSQL("INSERT INTO clothes-type VALUES(NULL, 'bottom','skirt')");;
        db.execSQL("INSERT INTO clothes-type VALUES(NULL, 'shoes','heel');");
>>>>>>> 730d33606c1551b63ea48bcc8c8559d416696583
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
