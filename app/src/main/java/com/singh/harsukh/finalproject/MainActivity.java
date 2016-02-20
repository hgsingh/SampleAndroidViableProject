package com.singh.harsukh.finalproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    private static final String TAG = "MainActivity";
    private EditText mEditTextA;
    private EditText mEditTextB;
    private EditText mEditTextC;
    public static String query_code = "com.singh.harsukh.MainActivity";
    private ActionBarDrawerToggle drawerListener;
    private DrawerLayout drawerLayout;
    private ListView mDrawerList;
    private ArrayList<String> title = new ArrayList<>();
    private static JSONObject jObject = null;
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        title.add(0, "Find Food");
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, title));
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(MainActivity.this, "Drawer Open", Toast.LENGTH_LONG).show();
                getActionBar().setTitle("Drawer");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Toast.makeText(MainActivity.this, "Drawer Closed", Toast.LENGTH_LONG).show();
                getActionBar().setTitle("Drawer");
            }
        };
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position)
    {
        mDrawerList.setItemChecked(position, true);
        if(title.get(position).equals("Find Food"))
        {
            //// TODO: 2/17/16
        }
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
        if(view.getId() == R.id.button5)
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
        if(view.getId() == R.id.button3)
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

    private DBService mDBService;
    private boolean status;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DBService.LocalBinder binder =(DBService.LocalBinder) service;
            mDBService = binder.getService();
            status = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status = false;
        }
    };

//    public void bindService(View v)
//    {
//        if(v.getId() == R.id.button6)
//        {
//            Intent local_intent = new Intent(this, BindedService.class);
//            //the context flag is passed because if a service doesn't exist it is automatically created
//            bindService(local_intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//            status = true;
//            Toast.makeText(getApplicationContext(), "Service Binded", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void unbindService(View v)
//    {
//        if(v.getId() == R.id.button7)
//        {
//            if(status) {
//                unbindService(mServiceConnection);
//                status = false;
//                Toast.makeText(getApplicationContext(), "Service unbinded", Toast.LENGTH_LONG).show();
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "bind it first", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    }
//
//    public void add(View v)
//    {
//        if(v.getId() == R.id.button8)
//        {
//            if(status)
//            {
//                int x = Integer.parseInt(mEditTextA.getText().toString());
//                int y = Integer.parseInt(mEditTextB.getText().toString());
//                int result = mBindedService.addNumbers(x,y);
//                Toast.makeText(getApplicationContext(), "Result: "+ result, Toast.LENGTH_LONG).show();
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "bind service first", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private class DownloadRunnable implements Runnable
    {
        private String _url = null;
        public DownloadRunnable(Object param)
        {
            _url = (String)param;
        }
        @Override
        public void run() {
            URL downloadUrl = null; //initially set object to null
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            //JSONObject jsonObject = null;
            String json = null;
            if(_url != null) {
                try {
                    downloadUrl = new URL(_url);
                    conn = (HttpURLConnection) downloadUrl.openConnection();
                    inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
                    json = reader.readLine();
                    System.out.println(json);
                    //jsonObject = new JSONObject(json);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent();
                    String stopThread = getApplicationContext().toString();
                    intent.setAction("stopThread");
                    intent.putExtra("json", json);
                    sendBroadcast(intent);
                }
            }
        }
    }

    public static void setJsonObject(JSONObject Object)
    {
        jObject = Object;
    }
}
