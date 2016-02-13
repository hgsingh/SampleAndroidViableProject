package com.singh.harsukh.finalproject;

/**
 * Created by harsukh on 2/12/16.
 */
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieSearcher extends AppCompatActivity
{
    //private String query;
    public final static String DEBUG_TAG = "MovieSearcher";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent initialization_intent = getIntent();
        String query = null;
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
        if(query != null) {
            // Check if the NetworkConnection is active and connected.
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new QueryManager().execute(initializer, query, getResources().getString(R.string.tmdb_key));
            }
            else {
                TextView textView = new TextView(this);
                textView.setText("No network connection.");
                setContentView(textView);
            }
        }
    }

    public static Intent getActivity(Context context)
    {
        Intent activity_intent = new Intent(context, MovieSearcher.class);
        return activity_intent;
    }
    private class QueryManager extends AsyncTask<String, Void, ArrayList>
    {

        @Override
        protected ArrayList doInBackground(String... params)
        {
            if(params[0].equals("movie"))
            {
                try {
                    return searchTMDB(params[1], params[2]);
                } catch (IOException e) {
                    return null;
                }
            }
            if(params[0].equals("actor"))
            {

            }
            if(params[0].equals("genre"))
            {

            }
            return null;
        }
        /**
         * Searches IMDBs API for the given query
         * @param query The query to search.
         * @return A list of all hits.
         */
        public ArrayList<MovieResult> searchTMDB(String query, String TMDB_API_KEY) throws IOException {
            // Build URL
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.themoviedb.org/3/search/movie");
            stringBuilder.append("?api_key=" + TMDB_API_KEY);
            stringBuilder.append("&query=" + query);
            URL url = new URL(stringBuilder.toString());

            InputStream stream = null;
            try {
                // Establish a connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("Accept", "application/json"); // Required to get TMDB to play nicely.
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response code is: " + responseCode + " " + conn.getResponseMessage());

                stream = conn.getInputStream();
                return parseResult(stringify(stream));
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        private ArrayList<MovieResult> parseResult(String result) {
            String streamAsString = result;
            ArrayList<MovieResult> results = new ArrayList<MovieResult>();
            try {
                JSONObject jsonObject = new JSONObject(streamAsString);
                JSONArray array = (JSONArray) jsonObject.get("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonMovieObject = array.getJSONObject(i);
                    MovieResult.Builder movieBuilder = MovieResult.newBuilder(
                            Integer.parseInt(jsonMovieObject.getString("id")),
                            jsonMovieObject.getString("title"))
                            .setBackdropPath(jsonMovieObject.getString("backdrop_path"))
                            .setOriginalTitle(jsonMovieObject.getString("original_title"))
                            .setPopularity(jsonMovieObject.getString("popularity"))
                            .setPosterPath(jsonMovieObject.getString("poster_path"))
                            .setReleaseDate(jsonMovieObject.getString("release_date"));
                    results.add(movieBuilder.build());
                }
            } catch (JSONException e) {
                System.err.println(e);
                Log.d(DEBUG_TAG, "Error parsing JSON. String was: " + streamAsString);
            }
            return results;
        }

        public String stringify(InputStream stream) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }
}
