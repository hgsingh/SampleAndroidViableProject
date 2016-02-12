package com.singh.harsukh.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by harsukh on 2/4/16.
 */
public class Download extends AsyncTask<String, Integer, ArrayList> {

    private ArrayList<SingleRow> rows;
    private MainActivity activity;
    private StarActivity star_activity;
    private DiscoverActivity discover_activity;
    private String tag = null;
    public Download(MainActivity activity)
    {
        this.activity = activity;
        rows = new ArrayList<>();
    }

    public Download(StarActivity activity)
    {
        this.star_activity = activity;
    }

    public Download(DiscoverActivity activity)
    {
        discover_activity = activity;
        rows = new ArrayList<>();
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        ArrayList rows = null;
        Uri.Builder builder = new Uri.Builder();
        URL download_url = null;
        if(params[0].equals("instagram"))
        {
            rows = this.rows;
            tag = params[0];
            builder.scheme("https")
                .authority("api.instagram.com")
                .appendPath("v1")
                .appendPath("users")
                .appendPath("search")
                .appendQueryParameter("q", params[1])
                .appendQueryParameter("client_id", params[2]);
            String url = builder.build().toString(); //convert url to string
            try {
                download_url = new URL(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(download_url.openConnection().getInputStream(), "UTF-8"), 1024);
                String json = reader.readLine();
                //System.out.println(json);
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jArray = jsonObject.getJSONArray("data");
                //System.out.println(jArray.toString());
                for(int i = 0; i<jArray.length(); ++i)
                {
                    JSONObject object = jArray.getJSONObject(i);
                    System.out.println(object.toString());
                    String username = object.getString("username");
                    String thumbnail_url = object.getString("profile_picture");
                    String id = object.getString("id");
                    String name = object.getString("full_name");
                    URL downloadURL = new URL(thumbnail_url);
                    HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                    InputStream inputStream = conn.getInputStream();
                    Bitmap thumb = BitmapFactory.decodeStream(inputStream);
                    thumb = Bitmap.createScaledBitmap(thumb,50,50,false);
                    SingleRow s = new SingleRow(username, name,thumb);
                    s.setId(id);
                    rows.add(i, s);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(params[0].equals("recentImages"))
        {
            rows = new ArrayList<Bitmap>();
            tag = params[0];
            builder.scheme("https")
                    .authority("api.instagram.com")
                    .appendPath("v1")
                    .appendPath("users")
                    .appendPath(params[1])
                    .appendPath("media")
                    .appendPath("recent")
                    .appendPath("")
                    .appendQueryParameter("client_id", params[2]);
            String url = builder.build().toString(); //convert url to string
            try {
                download_url = new URL(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(download_url.openConnection().getInputStream(), "UTF-8"), 1024);
                String json = reader.readLine();
                //System.out.println(json);
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jArray = jsonObject.getJSONArray("data");
                //System.out.println(jArray.toString());
                for(int i = 0; i<jArray.length(); ++i)
                {
                    JSONObject object = jArray.getJSONObject(i);
                    String thumbnail_url = object.getJSONObject("images").getJSONObject("thumbnail").getString("url");
                    URL downloadURL = new URL(thumbnail_url);
                    HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                    InputStream inputStream = conn.getInputStream();
                    Bitmap thumb = BitmapFactory.decodeStream(inputStream);
                    rows.add(thumb);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(params[0].equals("moviedb"))
        {
            rows = this.rows;
            tag = params[0];
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", "popularity.desc")
                    .appendQueryParameter("api_key",params[1]);
            String url = builder.build().toString();
            try
            {
                download_url = new URL(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(download_url.openConnection().getInputStream(), "UTF-8"), 2048);
                String json = reader.readLine();
                System.out.print(json);
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jArray = jsonObject.getJSONArray("results");
                System.out.println(jArray.toString());
                for(int i = 0; i< jArray.length(); ++i)
                {
                    JSONObject object = jArray.getJSONObject(i);
                    String image_path = object.getString("poster_path");
                    String title = object.getString("original_title");
                    String desc = object.getString("overview");
                    String thumbnail_url = "https://image.tmdb.org/t/p/w45"+image_path;
                    URL downloadURL = new URL(thumbnail_url);
                    HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                    InputStream inputStream = conn.getInputStream();
                    Bitmap thumb = BitmapFactory.decodeStream(inputStream);
                    thumb = Bitmap.createScaledBitmap(thumb,50,50,false);
                    SingleRow singleRow = new SingleRow(title, desc, thumb);
                    singleRow.setId(image_path);
                    rows.add(singleRow);
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        super.onPostExecute(arrayList);
        if(arrayList != null && arrayList.get(0) instanceof SingleRow ) {//bad monkey no banana for you (really bad programming)
            if (tag.equals("instagram")) {
                activity.startAfterDownload("instagram", arrayList);
                tag = "";
            }
            if (tag.equals("moviedb")) {
                discover_activity.setArrayList(arrayList);
                tag = "";
            }
        }
        if(arrayList != null && arrayList.get(0) instanceof Bitmap)
        {
            star_activity.setUserImages(arrayList);
            tag = "";
        }
    }

    private static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;
        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));
        photo=Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }

}
