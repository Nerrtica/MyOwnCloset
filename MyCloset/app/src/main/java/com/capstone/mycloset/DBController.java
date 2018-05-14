package com.capstone.mycloset;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
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
        this.DBName = "testDB.db";
        openHelper = new DBOpenHelper(context, DBName,null,1);
    }

    //Type를 입력하는 구문이다. sql문을 사용하여 입력
    public void InsertType(String category,String section){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO clothes_type VALUES (NULL, '"+category+"', '"+section+"');";
        db.execSQL(sql);
    }

    //코디를 입력하는 함수이다. 인자로 name , top,bottom이 있고, id는 autoincrement라 null을 넣는다.
    public void InsertCoordi(String name,String top,String bottom,String shoes){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO coordi VALUES(NULL, '"+name+"', '"+top+"', '"+bottom+"','" + shoes + "');";
        db.execSQL(sql);
    }

    //옷장에 옷을 넣는 함수이다. 마찬가지로 인자로 type, color, image d가 있고 id는 autoincrement이다.
    public void InsertCloset(int type, String color, String pattern, String imagePath){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        try{
            db.beginTransaction();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            B.compress(Bitmap.CompressFormat.PNG,10, stream);
//            byte[] data = stream.toByteArray();
            SQLiteStatement statement = db.compileStatement("insert into closet VALUES(NULL,?,?,?,?);");
            statement.bindString(1,String.valueOf(type));
            statement.bindString(2,color);
            statement.bindString(3,pattern);
            statement.bindString(4, imagePath);
            statement.execute();
            db.setTransactionSuccessful();
        } catch (SQLException e){

        } finally {
            db.endTransaction();
        }
    }

    //DB에서 저장한 코디를 찾는 함수이다. 원하는 코디의 이름이 주어졌을때 사용한다. db의 데이터를 cursor형태로 받은 다음, 재정렬한다. 옷장의 최대치는 100
    public Coordi[] FindCoordi(String name){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from coordi where name='"+name+"';",null);
        Coordi myCoordi[] = new Coordi[100];
        int i=0;
        while (cursor.moveToNext()){
            if(i>99) break;
            int id = cursor.getInt(0);
            String top = cursor.getString(2);
            String bottom = cursor.getString(3);
            String shoes = cursor.getString(4);
            myCoordi[i] = new Coordi(id,name,top,bottom,shoes);
            i++;
        }
        cursor.close();
        return myCoordi;
    }

    //DB에서 저장한 코디를 찾는 함수이다. 원하는 코디의 이름이 없고, 코디의 모든 데이터를 싹 긁어올때 사용한다. db의 데이터를 cursor형태로 받은 다음, 재정렬한다. 옷장의 최대치는 100
    public Coordi[] FindCoordi(){
        SQLiteDatabase db =openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from coordi",null);
        Coordi myCoordi[] = new Coordi[100];
        int i=0;
        while (cursor.moveToNext()){
            if(i>99) break;
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String top = cursor.getString(2);
            String bottom = cursor.getString(3);
            String shoes = cursor.getString(4);
            myCoordi[i] = new Coordi(id,name,top,bottom,shoes);
            i++;
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
            String pattern = cursor.getString(2);
            String color = cursor.getString(3);
            String image = cursor.getString(4);
//            Bitmap bm = getBitmap(image);
            myCloset.add(new Closet(id, type, pattern, color, image));
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
            String pattern = cursor.getString(2);
            String color = cursor.getString(3);
            String image = cursor.getString(4);
//            Bitmap bm = getBitmap(image);
            myCloset.add(new Closet(id, type, pattern, color, image));
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

//closet 구조체이다.
class Closet{
    private int id;
    private int type;
    private String color;
    private String pattern;
    private String imagePath;
    public Closet(int id, int type, String pattern, String color, String imagePath){
        this.id = id;
        this.type = type;
        this.pattern = pattern;
        this.color = color;
        this.imagePath = imagePath;
    }
    public int getId(){
        return id;
    }
    public int getType(){
        return type;
    }
    public String getPattern(){
        return pattern;
    }
    public String getColor(){
        return color;
    }
    public String getImagePath(){
        return imagePath;
    }
}

//coordi 구조체이다.
class Coordi{
    private int id;
    private String name;
    private String top;
    private String bottom;
    private String shoes;
    public Coordi(int id,String name,String top,String bottom,String shoes){
        this.id = id;
        this.name = name;
        this.top = top;
        this.bottom = bottom;
        this.shoes = shoes;
    }
    public int id(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getTop (){
        return top;
    }
    public String getBottom(){
        return bottom;
    }
    public String getShoes(){
        return  shoes;
    }
}
