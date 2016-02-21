package com.singh.harsukh.finalproject;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DiscoverActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView = null;
    private ArrayList<SingleRow> arrayList = null;
    private static Handler message_handler = null;
    private Bitmap thumb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        listView = (ListView)findViewById(R.id.listView2);
        if(isConnected() && savedInstanceState == null)
        {
            Download discover_download = new Download(this);
            discover_download.execute("moviedb", getResources().getString(R.string.tmdb_key));
            Log.d("DiscoverActivity", "Task Completed");
//            if(arrayList != null)
//                listView.setAdapter(new ListAdapter( DiscoverActivity.this, arrayList));
        }
        listView.setOnItemClickListener(this);
        message_handler = new Handler();
    }

    protected void setArrayList(ArrayList arrayList)
    {
        this.arrayList = arrayList;
        listView.setAdapter(new ListAdapter( DiscoverActivity.this, arrayList));

    }

    public static Intent getActivity(Context context)
    {
        Intent activity_intent = new Intent(context, DiscoverActivity.class);
        return activity_intent;
    }

    protected void setBitmap(Bitmap bm)
    {
        thumb = bm;
    }

    protected Bitmap getBitmap()
    {
        return thumb;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        if(isConnected())
        {
            Bundle args = new Bundle();
            String _id = arrayList.get(position).getId();
            String desc = arrayList.get(position).description;
            String thumbnail_url = "https://image.tmdb.org/t/p/w500"+_id;
            synchronized(this) {
                Thread download_thread = new Thread(new DownloadThread(thumbnail_url));
                download_thread.start();
                thumb = getBitmap();
                try {
                    download_thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentManager manager = getFragmentManager();
                MovieDetailDialog detailDialog = new MovieDetailDialog();
//            message_handler = new Handler(Looper.getMainLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    //get bitmap object from the handler
//                    thumb = (Bitmap) msg.obj;
//                }
//            };
//            Message message = message_handler.obtainMessage();
//            thumb = (Bitmap) message.obj;
                if (thumb != null) {
                    Log.d("DiscoverActivity", "Here I am");
                    args.putString("description", desc);
                    args.putParcelable("image", thumb);
                    detailDialog.setArguments(args);
                    detailDialog.show(manager, "MovieDetailDialog");
                }
            }
        }
    }

    private boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    private class DownloadThread implements Runnable
    {
        private String _url = null;
        private Bitmap thumb = null;
        public DownloadThread(Object param)
        {
            _url = (String)param;
        }
        @Override
        public void run() {
            URL downloadUrl = null; //initially set object to null
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            if(_url != null) {
                try {
                    downloadUrl = new URL(_url);
                    conn = (HttpURLConnection) downloadUrl.openConnection();
                    inputStream = conn.getInputStream();
                    thumb = BitmapFactory.decodeStream(inputStream);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    message_handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setBitmap(thumb);
                        }
                    });
                    //message_handler.notifyAll();
//                    if(thumb != null) {
//                        Message message = Message.obtain();
//                        message.obj = thumb;
//                        message_handler.sendMessage(message);
//                    }
                }
            }
        }
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        arrayList = savedInstanceState.getParcelableArrayList("SERIAL_KEY");
        if(arrayList != null){
            listView.setAdapter(new ListAdapter(getApplicationContext(), arrayList));
            Toast.makeText(getApplicationContext(), "arrayList has a value", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "nothing was saved", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("SERIAL_KEY", arrayList);
    }
}
