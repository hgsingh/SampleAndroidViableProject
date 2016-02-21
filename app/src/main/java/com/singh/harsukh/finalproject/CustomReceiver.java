package com.singh.harsukh.finalproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harsukh on 2/19/16.
 */
public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getStringExtra("json");
        if(json != null)
        {
            Toast.makeText(context, "Intent Received", Toast.LENGTH_LONG).show();
            MainActivity.setJsonObject(json);
            MainActivity.setDBJsonWrapper();
        }
    }
}
