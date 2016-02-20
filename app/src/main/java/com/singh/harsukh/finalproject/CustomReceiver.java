package com.singh.harsukh.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by harsukh on 2/19/16.
 */
public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getStringExtra("json");

    }
}
