package com.singh.harsukh.finalproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by harsukh on 2/19/16.
 */
public class DBService extends Service {

    private final IBinder mBinder = new LocalBinder();
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
}
