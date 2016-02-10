package com.singh.harsukh.finalproject;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DiscoverActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView = null;
    private ArrayList<SingleRow> arrayList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        listView = (ListView)findViewById(R.id.listView2);
        if(isConnected())
        {
            Download discover_download = new Download(this);
            discover_download.execute("moviedb", getResources().getString(R.string.tmdb_key));
            Log.d("DiscoverActivity", "Task Completed");
            if(arrayList != null)
                listView.setAdapter(new ListAdapter( DiscoverActivity.this, arrayList));
            listView.setOnItemClickListener(this);
        }
    }

    protected void setArrayList(ArrayList arrayList)
    {
        this.arrayList = arrayList;
    }

    public static Intent getActivity(Context context)
    {
        Intent activity_intent = new Intent(context, DiscoverActivity.class);
        return activity_intent;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        if(isConnected())
        {
            Uri.Builder builder = new Uri.Builder();
            URL download_url = null;
            Bundle args = new Bundle();
            String _id = arrayList.get(position).getId();
            String desc = arrayList.get(position).description;
            builder.scheme("https")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w150")
                    .appendPath(_id);
            String thumbnail_url = builder.build().toString();
            try {
                download_url = new URL(thumbnail_url);
                HttpURLConnection conn = (HttpURLConnection) download_url.openConnection();
                InputStream inputStream = conn.getInputStream();
                Bitmap thumb = BitmapFactory.decodeStream(inputStream);
                FragmentManager manager = getFragmentManager();
                MovieDetailDialog detailDialog = new MovieDetailDialog();
                args.putString("description", desc);
                args.putParcelable("image", thumb);
                detailDialog.setArguments(args);
                detailDialog.show(manager,"MovieDetailDialog");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
}
