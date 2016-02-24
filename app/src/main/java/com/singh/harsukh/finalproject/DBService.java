package com.singh.harsukh.finalproject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by harsukh on 2/19/16.
 */
public class DBService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private static DBHelper helper;
    private boolean db_check = false;
    public void setHelper(Context context)
    {
        helper = new DBHelper(context);
        db_check = doesDatabaseExist(context, helper.DATABASE_NAME);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder
    {
        public DBService getService()
        {
            return DBService.this;
        }
    }

    public void insertJSON(JSONObject json)
    {
        if(db_check == false) {
            if (json != null) {
                try {
                    JSONArray array = json.getJSONArray("genres");
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject object = array.getJSONObject(i);
                        String genre_name = object.getString("name");
                        String genre_id = object.getString("id");
                        insertData(genre_name, genre_id);
                        System.out.println(genre_id + ":" + genre_name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("DBService", "null object passed");
            }
        }
    }


    //this function will insert data into the database table
    public long insertData(String genre_name, String genre_id)
    {
        //returns a database object using the helper class
        SQLiteDatabase db = helper.getWritableDatabase();
        //content values are objects which can supply data
        //to the database as a set of key/value pairs in an object
        //this abstracts the function of the object
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.GENRE_NAME, genre_name);
        contentValues.put(DBHelper.GENRE_ID, genre_id);
        //the insert function will take the table name as a parameter
        // The second parameter is the null column hack which will insert null values to specified columns
        // The contentValues param is a map. This map contains the initial column values for the row.
        // The keys should be the column names and the values the column values
        long id = db.insert(DBHelper.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    public class DBHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "keywordDatabase";
        private static final String TABLE_NAME = "genre_table";
        private static final String GENRE_NAME = "genre";
        private static final String GENRE_ID = "genre_id";
        private static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + " ("+ GENRE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ GENRE_NAME +" VARCHAR(255));";
        private Context context;
        public DBHelper(Context context) {
            super(context,DATABASE_NAME , null, 1);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) //creates table when the constructor is called
        {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "onCreate called", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            //do nothing
        }
    }

    //put sql functions here

    public String getGenre(String genre_name)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] selection_args = {genre_name};
        String[] column_args = {"genre_id"};
        Cursor cursor = db.query(DBHelper.TABLE_NAME, column_args, DBHelper.GENRE_NAME + " = ?", selection_args, null, null, null);
        StringBuffer buffer = new StringBuffer();
        Log.e("DBService", "service called");
        while(cursor.moveToNext()){
            int indexId = cursor.getColumnIndex(DBHelper.GENRE_ID);
            int id = cursor.getInt(indexId);
            buffer.append("ID:"+id+"\n");
        }

        return buffer.toString();
    }
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
