package com.singh.harsukh.finalproject;

/**
 * Created by harsukh on 2/12/16.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieSearcher extends AppCompatActivity
{
    //private String query;
    public final static String DEBUG_TAG = "MovieSearcher";
    private static String query_tag = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_search_layout);
        Intent initialization_intent = getIntent();
        String query = null;
        String initializer = initialization_intent.getStringExtra(MainActivity.query_code);
        query_tag = initializer;
        if(initializer.equals("actor"))
        {
            query = initialization_intent.getStringExtra("actor_name");
        }
        if(initializer.equals("movie"))
        {
            query = initialization_intent.getStringExtra("movie_name");
        }
//        if(initializer.equals("genre"))
//        {
//            query = initialization_intent.getStringExtra("genre_name");
//        }
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
        private String tag = null;
        @Override
        protected ArrayList doInBackground(String... params)
        {
            tag = params[0];
            if(tag != null) {
                if (tag.equals("movie")) {
                    try {
                        return searchTMDB(params[0],params[1], params[2]);
                    } catch (IOException e) {
                        return null;
                    }
                }
                if(tag.equals("actor"))
                {
                    try {
                        return searchActors(params[0], params[1], params[2]);
                    }
                    catch(IOException e)
                    {
                        return null;
                    }
                }
//                if (tag.equals("genre")) {
//                    //// TODO: 2/13/16
//                }
            }
            return null;
        }

        public ArrayList searchActors(String query_type, String query, String TMDB_API_KEY) throws IOException {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("search")
                    .appendPath("person")
                    .appendQueryParameter("api_key", TMDB_API_KEY)
                    .appendQueryParameter("query", query);

            URL url = new URL(builder.build().toString());
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
                return parseActor(stringify(stream));
            }
            finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }
        /**
         * Searches IMDBs API for the given query
         * @param query The query to search.
         * @return A list of all hits.
         */
        public ArrayList<MovieResult> searchTMDB(String query_type, String query, String TMDB_API_KEY) throws IOException {
            // Build URL

//            StringBuilder stringBuilder = new StringBuilder();
//            if (query_type.equals("movie")) {
//                stringBuilder.append("http://api.themoviedb.org/3/search/movie");
//                stringBuilder.append("?api_key=" + TMDB_API_KEY);
//                stringBuilder.append("&query=" + query);
//            }
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("search")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", TMDB_API_KEY)
                    .appendQueryParameter("query", query);

            URL url = new URL(builder.build().toString());

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

        private ArrayList<SingleRow> parseActor(String stream)
        {
            String streamAsString = stream;
            ArrayList<SingleRow> results = new ArrayList<>();
            try{
                JSONObject jsonObject = new JSONObject(streamAsString);
                JSONArray array = (JSONArray) jsonObject.get("results");
                for(int i = 0; i < array.length(); ++i)
                {
                    JSONObject object = array.getJSONObject(i);
                    String image_path = object.getString("profile_path");
                    String title = object.getString("name");
                    String desc = object.getString("popularity");
                    String thumbnail_url = "https://image.tmdb.org/t/p/w45"+image_path;
                    URL downloadURL = new URL(thumbnail_url);
                    HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                    InputStream inputStream = conn.getInputStream();
                    Bitmap thumb = BitmapFactory.decodeStream(inputStream);
                    thumb = Bitmap.createScaledBitmap(thumb,50,50,false);
                    SingleRow singleRow = new SingleRow(title, desc, thumb);
                    singleRow.setId(image_path);
                    results.add(singleRow);
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        private ArrayList<MovieResult> parseResult(String result)
        {
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

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            updateViewWithResults(arrayList, tag);
        }
    }
    /**
     * Updates the View with the results. This is called asynchronously
     * when the results are ready.
     * @param result The results to be presented to the user.
     */
    public void updateViewWithResults(ArrayList result, String tag) {
        ListView listView = new ListView(this);
        Log.d("updateViewWithResults", result.toString());
        // Add results to listView.
        if(tag.equals("movie")) {
            ArrayAdapter<MovieResult> adapter =
                    new ArrayAdapter<MovieResult>(this, android.R.layout.simple_list_item_1, result);
            listView.setAdapter(adapter);
        }
        if(tag.equals("actor"))
        {
            listView.setAdapter(new ListAdapter(this, result));
        }
        // Update Activity to show listView
        setContentView(listView);
    }

}
