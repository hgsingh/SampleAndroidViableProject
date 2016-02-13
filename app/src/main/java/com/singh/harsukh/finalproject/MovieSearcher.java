package com.singh.harsukh.finalproject;

/**
 * Created by harsukh on 2/12/16.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieSearcher extends AppCompatActivity
{
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent initialization_intent = getIntent();
        String initializer = initialization_intent.getStringExtra(MainActivity.query_code);
        if(initializer.equals("actor"))
        {
            query = initialization_intent.getStringExtra("actor_name");

        }
        if(initializer.equals("movie"))
        {
            query = initialization_intent.getStringExtra("movie_name");

        }
        if(initializer.equals("genre"))
        {
            query = initialization_intent.getStringExtra("genre_name");
        }
    }

    public static Intent getActivity(Context context)
    {
        Intent activity_intent = new Intent(context, MovieSearcher.class);
        return activity_intent;
    }

}
