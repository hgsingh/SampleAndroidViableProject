package com.singh.harsukh.finalproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    private static final String TAG = "MainActivity";
    private EditText mEditTextA;
    private EditText mEditTextB;
    private EditText mEditTextC;
    public static String query_code = "com.singh.harsukh.MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextA = (EditText) findViewById(R.id.editText);
        mEditTextC = (EditText) findViewById(R.id.editText3);
        mEditTextB = (EditText) findViewById(R.id.editText2);
        mEditTextA.setText("");
        mEditTextB.setText("");
        mEditTextC.setText("");
        mEditTextA.setOnFocusChangeListener(this);
        mEditTextB.setOnFocusChangeListener(this);
        mEditTextB.setOnFocusChangeListener(this);
    }

    public void startDiscoverActivity(View v)
    {
        if(v.getId() == R.id.button)
            startActivity(DiscoverActivity.getActivity(getApplicationContext()));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if(!hasFocus) {
            if (v.getId() == R.id.editText) {
                Toast.makeText(this, "Press Go!!!!", Toast.LENGTH_SHORT).show();
            }
            if (v.getId() == R.id.editText2) {
                Toast.makeText(this, "Press Go!!!!", Toast.LENGTH_SHORT).show();
            }
            if (v.getId() == R.id.editText3) {
                Toast.makeText(this, "Press Go!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //get the menu options and inflate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.final_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_star)
        {
            FragmentManager manager = getFragmentManager();
            StarFindDialog stalk = new StarFindDialog();
            stalk.show(manager, "StarFindDialog");
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDialogMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void  startAfterDownload(String arg, ArrayList<SingleRow> arrayList)
    {
        if(arg.equals("instagram") && arrayList != null)
        {
//            int i = 0;
//            SingleRow reference;
//            while(i < arrayList.size())
//            {
//                reference = arrayList.get(i);
//                System.out.println(reference.title);
//                ++i;
//            }
            Intent intent = StarActivity.getActivity(getApplicationContext());
            intent.putExtra("imageObjects", arrayList);
            startActivity(intent);
        }
    }

    public void goToSearch(View view)
    {
        if(view.getId() == R.id.button3)
        {
            String query = mEditTextA.getText().toString();
            if(!query.equals(""))
            {
                Intent movie_intent = MovieSearcher.getActivity(getApplicationContext());
                movie_intent.putExtra("movie_name", query);
                movie_intent.putExtra(query_code, "movie");
                startActivity(movie_intent);
            }
        }
        if(view.getId() == R.id.button4)
        {
            String query = mEditTextB.getText().toString();
            if(!query.equals(""))
            {
                Intent actor_intent = MovieSearcher.getActivity(getApplicationContext());
                actor_intent.putExtra("actor_name", query);
                actor_intent.putExtra(query_code, "actor");
                startActivity(actor_intent);
            }
        }
        if(view.getId() == R.id.button5)
        {
            String query = mEditTextC.getText().toString();
            if(!query.equals(""))
            {
                Intent genre_intent = MovieSearcher.getActivity(getApplicationContext());
                genre_intent.putExtra("genre_name", query);
                genre_intent.putExtra(query_code,"genre");
                startActivity(genre_intent);
            }
        }
    }

}
