package com.example.caucse.db;


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

    //생성자 따로 불러올 DB가 있으면 그 DB를 불러오고, 아니면 미리 있는 DB를 불러온다.

    DBcontroller(String DBname,Context context){
        this.DBName=DBname;
        openHelper = new DBOpenHelper(context,DBname,null,1);
    }
    DBcontroller(Context context){
        this.DBName = "testDB.db";
        openHelper = new DBOpenHelper(context,DBName,null,1);
    }

    //Type를 입력하는 구문이다. sql문을 사용하여 입력
    public void InsertType(String category,String section){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO clothes_type VALUES ('"+category+"', '"+section+"');";
        db.execSQL(sql);
    }

    //코디를 입력하는 함수이다. 인자로 name , top,bottom이 있고, id는 autoincrement라 null을 넣는다.
    public void InsertCoordi(String name,String top,String bottom){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String sql = "INSERT INTO coordi VALUES(NULL, '"+name+"', '"+top+"', '"+bottom+"');";
        db.execSQL(sql);
    }

    //옷장에 옷을 넣는 함수이다. 마찬가지로 인자로 type, color, image d가 있고 id는 autoincrement이다.
    public void InsertCloset(String type,String color,Bitmap B){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        B.compress(Bitmap.CompressFormat.PNG,10,stream);
        byte[] data = stream.toByteArray();
        SQLiteStatement statement = db.compileStatement("insert into closet VALUES (NULL,?,?,?)");
        statement.bindString(1,type);
        statement.bindString(2,color);
        statement.bindBlob(3,data);
        statement.execute();
        System.out.println("DB ACESS IS SUCCESS");
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
            myCoordi[i] = new Coordi(id,name,top,bottom);
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
            myCoordi[i] = new Coordi(id,name,top,bottom);
            i++;
        }
        cursor.close();
        return myCoordi;
    }

    //옷장에서 옷을 찾는 함수이다. 마찬가지로 옷장의 옷 데이터를 모조리 긁어온다.
    public Closet[] FindCloset(){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from closet",null);
        Closet myCloset[] = new Closet[100];
        int i =0;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String color = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            Bitmap bm = getBitmap(image);
            myCloset[i] = new Closet(id,type,color,bm);
            i++;
        }
        cursor.close();
        return myCloset;
    }
    // 이미지를 byte 형태로 바꿀 때 사용한다.
    public byte[] getByteArrayFromDrawble(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
        byte[] data = stream.toByteArray();

        return data;
    }

    //byte를 bitmap형식으로 바꿀 때 사용한다.
    public Bitmap getBitmap(byte[] b){
        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
        return bitmap;
    }
}

//closet 구조체이다.
class Closet{
    private int id;
    private String type;
    private String color;
    private Bitmap image;
    public Closet(int id,String type,String color,Bitmap image){
        this.id = id;
        this.type = type;
        this.color = color;
        this.image = image;
    }
    public int getId(){
        return id;
    }
    public String getType(){
        return type;
    }
    public String getColor(){
        return color;
    }
    public Bitmap getImage(){
        return image;
    }
}

//coordi 구조체이다.
class Coordi{
    private int id;
    private String name;
    private String top;
    private String bottom;
    public Coordi(int id,String name,String top,String bottom){
        this.id = id;
        this.name = name;
        this.top = top;
        this.bottom = bottom;
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
}
