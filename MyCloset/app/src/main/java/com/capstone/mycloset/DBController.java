package com.capstone.mycloset;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

/**
 * Created by caucse on 2018-04-03.
 */

public class DBController {
    private static String DBName;
    private DBOpenHelper openHelper;

    //생성자 따로 불러올 DB가 있으면 그 DB를 불러오고, 아니면 미리 있는 DB를 불러온다.

    DBController(String DBname, Context context){
        this.DBName = DBname;
        openHelper = new DBOpenHelper(context, DBname,null,1);
    }
    DBController(Context context){
        this.DBName = "closetDB.db";
        openHelper = new DBOpenHelper(context, DBName,null,1);
    }


    //코디를 입력하는 함수이다. 인자로 name , top,bottom이 있고, id는 autoincrement라 null을 넣는다. 겉옷이 없는 함수이다.
    public void InsertCoordi(String name,String top,String bottom,String shoes){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO coordi VALUES(NULL, '"+name+"',NULL, '"+top+"', '"+bottom+"','" + shoes + "');";
        db.execSQL(sql);
    }

    //코디를 입력하는 함수이다. 인자로 name , top,bottom이 있고, id는 autoincrement라 null을 넣는다.
    public void InsertCoordi(String name,String outerWear,String top,String bottom,String shoes){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO coordi VALUES(NULL, '"+name+"', '"+outerWear+"','"+top+"', '"+bottom+"','" + shoes + "');";
        db.execSQL(sql);
    }

    //옷장에 옷을 넣는 함수이다. 마찬가지로 인자로 type, color, image d가 있고 id는 autoincrement이다.
    public void InsertCloset(int type, int color, int pattern, int isLong, String imagePath){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        try{
            db.beginTransaction();
            SQLiteStatement statement = db.compileStatement("insert into closet VALUES(NULL,?,?,?,?,?);");
            statement.bindString(1, String.valueOf(type));
            statement.bindString(2, String.valueOf(pattern));
            statement.bindString(3, String.valueOf(color));
            statement.bindString(4, String.valueOf(isLong));
            statement.bindString(5, imagePath);
            statement.execute();
            db.setTransactionSuccessful();
        } catch (SQLException e){

        } finally {
            db.endTransaction();
        }
    }

    //DB에서 저장한 코디를 찾는 함수이다. 원하는 코디의 이름이 주어졌을때 사용한다. db의 데이터를 cursor형태로 받은 다음, 재정렬한다. 옷장의 최대치는 100
    public Coordi FindCoordi(String name){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from coordi where name='"+name+"';",null);
        Coordi myCoordi = null;

        while (!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            int outerWear = cursor.getInt(2);
            int top = cursor.getInt(3);
            int bottom = cursor.getInt(4);
            int shoes = cursor.getInt(5);
            myCoordi = new Coordi(id,name,top,bottom,shoes);
            cursor.moveToNext();
        }
        cursor.close();
        return myCoordi;
    }

    //DB에서 저장한 코디를 찾는 함수이다. 원하는 코디의 이름이 없고, 코디의 모든 데이터를 싹 긁어올때 사용한다. db의 데이터를 cursor형태로 받은 다음, 재정렬한다. 옷장의 최대치는 100
    public ArrayList<Coordi> FindCoordi(){
        SQLiteDatabase db =openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from coordi",null);
        ArrayList<Coordi> myCoordi = new ArrayList<>();

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int outerWear = cursor.getInt(2);
            int top = cursor.getInt(3);
            int bottom = cursor.getInt(4);
            int shoes = cursor.getInt(5);
            myCoordi.add(new Coordi(id,name,outerWear,top,bottom,shoes));
            cursor.moveToNext();
        }
        cursor.close();
        return myCoordi;
    }

    //옷장에서 옷을 찾는 함수이다. 마찬가지로 옷장의 옷 데이터를 모조리 긁어온다.
    public ArrayList<Closet> FindCloset(){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from closet;",null);
        cursor.moveToFirst();
        ArrayList<Closet> myCloset = new ArrayList<>();

        while(!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            int type = cursor.getInt(1);
            int pattern = cursor.getInt(2);
            int color = cursor.getInt(3);
            int isLong = cursor.getInt(4);
            String image = cursor.getString(5);
//            Bitmap bm = getBitmap(image);
            myCloset.add(new Closet(id, type, pattern, color, isLong, image));
            cursor.moveToNext();
        }
        cursor.close();
        return myCloset;
    }

    //Type에 맞는 옷을 찾는 함수
    public ArrayList<Closet> FindCloset(int closetType){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from closet where type ==" + closetType + ";",null);
        ArrayList<Closet> myCloset = new ArrayList<>();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            int type = cursor.getInt(1);
            int pattern = cursor.getInt(2);
            int color = cursor.getInt(3);
            int isLong = cursor.getInt(4);
            String image = cursor.getString(5);
//            Bitmap bm = getBitmap(image);
            myCloset.add(new Closet(id, type, pattern, color, isLong, image));
        }
        cursor.close();
        return myCloset;
    }

    //모든 코디를 지우는 함수이다.
    public void deleteAllCoordi(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "DELETE FROM coordi";
        db.execSQL(sql);
    }

    //모든 옷을 지우는 함수이다.
    public void deleteCloset(int id) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "DELETE FROM closet where _id=" + id + ";";
        db.execSQL(sql);
    }

    //모든 코디와 옷장의 옷들을 지우는 함수이다.
    public void deleteAllCloset(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        deleteAllCoordi();
        String sql = "DELETE FROM closet";
        db.execSQL(sql);
    }

//    // 이미지를 byte 형태로 바꿀 때 사용한다.
//    public byte[] getByteArrayFromDrawble(Drawable d){
//        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
//        byte[] data = stream.toByteArray();
//
//        return data;
//    }
//
//    //byte를 bitmap형식으로 바꿀 때 사용한다.
//    public Bitmap getBitmap(byte[] b){
//        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
//        return bitmap;
//    }
}