package com.singh.harsukh.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by harsukh on 2/15/16.
 */
public class DatabaseManager {
    //this class will get the data from the downloaded JSON file (implented as a service) once the file is there
    //the class will create a database table based on the parsed JSON file, the table is then supplied to the
    //main activity where the table will be read by checking the table

    public class DBHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "keywordDatabase";
        public DBHelper(Context context) {
            super(context,DATABASE_NAME , null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            //do nothing
        }
    }
}
