package com.singh.harsukh.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class YelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);
    }

    public static Intent getActivity(Context context)
    {
        Intent activity_intent = new Intent(context, YelpActivity.class);
        return activity_intent;
    }
}
