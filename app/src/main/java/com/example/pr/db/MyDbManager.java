package com.example.pr.db;

import static com.example.pr.db.MyConstants.TITLE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pr.adapter.ListItem;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;


    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }
    public void openDB() {
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String title, String disc, String uri){
        ContentValues cv= new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DISC, disc);
        cv.put(MyConstants.URI, uri);
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }
    public void delete(int id){
        String  selection = MyConstants._ID + "=" + id;
        db.delete(MyConstants.TABLE_NAME, selection, null );

    }
    public void update(String title, String disc, String uri, int id){
        String  selection = MyConstants._ID + "=" + id;
        ContentValues cv= new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DISC, disc);
        cv.put(MyConstants.URI, uri);
        db.update(MyConstants.TABLE_NAME, cv, selection, null );

    }
    public void getFromDb(String searchText, OnDataReceived onDataReceived){
        final List<ListItem> tempList = new ArrayList<>();
        String selection = MyConstants.TITLE + " like ?";
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, selection, new String[]{"%" + searchText + "%"}, null,
                null, null);


        while(cursor.moveToNext()) {
            ListItem item = new ListItem();
             @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            @SuppressLint("Range") String desc = cursor.getString(cursor.getColumnIndex(MyConstants.DISC));
            @SuppressLint("Range") String uri = cursor.getString(cursor.getColumnIndex(MyConstants.URI));
            @SuppressLint("Range") int _id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            item.setTitle(title);
            item.setDesc(desc);
            item.setUri(uri);
            item.setId(_id);
            tempList.add(item);
        }
        cursor.close();
        onDataReceived.onReceived(tempList);
    }
    public void closeDb(){
        myDbHelper.close();
    }
}
