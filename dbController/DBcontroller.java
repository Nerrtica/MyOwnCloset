package kacaumap.kacau.com.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Created by caucse on 2018-04-03.
 */

public class DBcontroller {
    private static String DBName;
    private DBOpenHelper openHelper;

    DBcontroller(String DBname,Context context){
        this.DBName=DBname;
        openHelper = new DBOpenHelper(context,DBname,null,1);
    }
    DBcontroller(Context context){
        this.DBName = "testDB.db";
        openHelper = new DBOpenHelper(context,DBName,null,1);
    }

    public void InsertType(String category,String section){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO clothes_type VALUES ('"+category+", "+section+");";
        db.execSQL(sql);
    }
    public void InsertCoordi(String name,String top,String bottom){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO coordi VALUES(NULL, '"+name+"', '"+top+"', '"+bottom+");";
        db.execSQL(sql);
    }
    public void InsertCloset(String type,String color,Drawable d){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        byte[] data = getByteArrayFromDrawble(d);
        SQLiteStatement statement = db.compileStatement("insert into Closet VALUES (NULL,?,?,?");
        statement.bindString(1,type);
        statement.bindString(2,color);
        statement.bindBlob(3,data);
    }
    public Cursor FindCoordi(String name){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from coordi where name='"+name+"';",null);
        return cursor;
    }
    public Cursor FindCloset(){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from closet",null);
        return cursor;
    }
    public byte[] getByteArrayFromDrawble(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte[] data = stream.toByteArray();

        return data;
    }
    public Bitmap getBitmap(Byte[] b){
        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
        return bitmap;
    }
}
