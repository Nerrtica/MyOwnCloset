package kacaumap.kacau.com.db;

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
        db.execSQL("CREATE TABLE closet (_id INTEGER PRIMARY KEY AUTOINCREMENT, type CHAR(10), color CHAR(15), image BLOB, FOREIGN KEY (type) REFERENCES clothes_type(section));");
        db.execSQL("CREATE TABLE clothes_type (category CHAR(10),section CHAR(20), PRIMARY KEY(category,section));");
        db.execSQL("CREATE TABLE coordi  (_id INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(20), top_clothes ChAR(10), botton_clothes CHAR(10));");
        //Toast.makeText(context,"DB is opened",0).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
