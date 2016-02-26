package com.singh.harsukh.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.singh.harsukh.yelpapilibrary.Yelp;

import org.json.JSONArray;
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

public class YelpActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private EditText mEditText;
    private Button mButton;
    private Location current_location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (google_serv_check()) {
            setContentView(R.layout.activity_yelp);
            initMaps();
            mEditText = (EditText) findViewById(R.id.search_text);
            mEditText.setText("");
            mButton = (Button) findViewById(R.id.search_button);
        }
    }

    private void initMaps() //setting the map fragment and loading it
    {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.MapFragment); //find the fragment by id
        mapFragment.getMapAsync(this); //loads maps asynchronously then calls the onMapsReady
    }

    public boolean google_serv_check() //checks if service is available
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        if (apiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = apiAvailability.getErrorDialog(YelpActivity.this, isAvailable, 0);
            dialog.show();
        }
        return false;
    }

    public static Intent getActivity(Context context)
    {
        Intent activity_intent = new Intent(context, YelpActivity.class);
        return activity_intent;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if(mGoogleMap != null)
        {
            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    YelpActivity.this.setMarker(latLng.toString(), latLng.latitude, latLng.longitude);
                }
            });

            //custom window designed in here too
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null); //retrieves the info_window xml layout
                    //the root in the function is set to null because no root is needed
                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                    TextView dist = (TextView) v.findViewById(R.id.tv_dist);
                    //setting the textViews  use the marker to retrieve this data
                    LatLng ll = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    tvLat.setText("Latitude: " + ll.latitude);
                    tvLng.setText("Longitude: " + ll.longitude);
                    if(current_location != null)
                    {
                        Location set_ll = new Location(marker.getTitle());
                        set_ll.setLatitude(ll.latitude);
                        set_ll.setLongitude(ll.longitude);
                        float distance = current_location.distanceTo(set_ll);
                        dist.setText("Distance to: " + distance);
                    }
                    else
                    {
                        dist.setText("Distance to: ????");
                    }
                    return v;
                }
            });
        }
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API) //adds the api for location services
                .addConnectionCallbacks(this) // callback method for connection
                .addOnConnectionFailedListener(this) //checks if connection failed
                .build();
        mGoogleApiClient.connect();
    }

    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private ArrayList<Polyline> lines = new ArrayList<Polyline>();
    static final int POLYGON_POINTS = 100;

    private void setMarker(String locality, double x, double y) {
        //removing polygon if already drawn
        if(markers.size() == POLYGON_POINTS){
            removeEverything();}
        //creating marker options
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .draggable(false) //drag a marker
                .position(new LatLng(x, y)); //get position of marker
        if(locality.equals("current location")){
            markers.add(0,mGoogleMap.addMarker(options));
        }
        else{
            markers.add(mGoogleMap.addMarker(options));}
        if(markers.size() > 1) {
            drawline(markers.get(0), markers.get(markers.size() - 1));
        }
    }

    private void removeEverything() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
        for(Polyline line: lines)
        {
            line.remove();
        }
        lines.clear();
    }

    private void drawline(Marker marker1, Marker marker2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(marker1.getPosition().latitude);
        locationA.setLongitude(marker1.getPosition().longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(marker2.getPosition().latitude);
        locationB.setLongitude(marker2.getPosition().longitude);

        float distance = locationA.distanceTo(locationB);
        PolylineOptions options = new PolylineOptions()
                                  .add(marker1.getPosition())
                                  .add(marker2.getPosition())
                                  .color(Color.BLUE)
                                  .width(3);
        lines.add(mGoogleMap.addPolyline(options));
        Toast.makeText(YelpActivity.this, "Distance to: "+distance, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create(); //creates location request object
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //sets priority of request with high location accuract
        mLocationRequest.setInterval(1000); //location retrieved every second
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this); //updates location and listens for the location
        //calls  onLocationChanged();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) //gets location based on updated position
    {
        if(location == null)
        {
            Toast.makeText(getApplicationContext(), "Can't get location", Toast.LENGTH_LONG).show();
        }
        else
        {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());


            if(current_location == null)
            {
                current_location = location;
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
                mGoogleMap.animateCamera(update);
                setMarker("current location", location.getLatitude(), location.getLongitude());
            }
            else {
                float distance =  current_location.distanceTo(location);
                if(Math.round(distance) > 100)
                {
                    current_location = location;
                    if(markers.get(0) != null) {
                        markers.remove(0);
                    }
                    setMarker("current location", location.getLatitude(), location.getLongitude());
                }
            }
        }
    }

    public void geoLocate(View v)
    {
        String search_term = mEditText.getText().toString();
        if(!search_term.equals("") && current_location != null) {
            Thread print_thread = new Thread(new YelpRunnable(new Yelp(getResources().getString(R.string.yelp_consumer_key),getResources().getString(R.string.yelp_consumer_secret),getResources().getString(R.string.yelp_token),getResources().getString(R.string.yelp_token_secret)), search_term));
            print_thread.start();
        }
    }

    public void processJson(String jsonStuff) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        //ArrayList<String> businessNames = new ArrayList<String>(businesses.length());
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            JSONObject coordinates =  business.getJSONObject("location").getJSONObject("coordinate");
           // businessNames.add(business.getString("name"));
            setMarker(business.getString("name"), Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude")));

        }
        //return TextUtils.join("\n", businessNames);
    }

    private class YelpRunnable implements Runnable
    {
        private Yelp yelp_object = null;
        private String search_term;
        public YelpRunnable(Yelp param, String search_term)
        {
            yelp_object = param;
            this.search_term = search_term;
        }
        @Override
        public void run() {
            final String json = yelp_object.search(search_term, current_location.getLatitude(), current_location.getLongitude());
            //this is made because the UI thread needs to receive the information
            //so this data is passed between the threads using the method shown.

                    YelpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                processJson(json);
                            } catch (JSONException e) {
                                return;
                            }
                        }
                    });
        }
    }
}
