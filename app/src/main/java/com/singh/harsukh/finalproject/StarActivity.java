package com.singh.harsukh.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class StarActivity extends AppCompatActivity {

    //private static ListView listView;
    private static ArrayList<SingleRow> list_images;
    private static String TAG = "StarActivity";
    private ViewPager viewPager;
    private ArrayList<Bitmap> user_images = null;
    private ListGridAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        Log.e(TAG, "Star Activitiy Created", new Exception());
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ListGridAdapter(getSupportFragmentManager(), StarActivity.this);
        viewPager.setAdapter(adapter);

        //listView = (ListView) findViewById(R.id.listView);
        list_images = getIntent().getParcelableArrayListExtra("imageObjects");
//        if(list_images != null)
//            setImages(list_images);
    }

//    public void setImages(ArrayList<SingleRow> images)
//    {
//        listView.setAdapter(new ListAdapter(this, images));
//    }

    public static Intent getActivity(Context context)
    {
        Intent activity_intent = new Intent(context, StarActivity.class);
        return activity_intent;
    }

    public void setUserImages(ArrayList<Bitmap> user_images)
    {
        this.user_images = user_images;
        System.out.print(user_images.get(0));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1, true);
    }

//    public ArrayList<Bitmap> getUser_images()
//    {
//        if(user_images != null)
//        {
//            return user_images;
//        }
//        return null;
//    }

    class ListGridAdapter extends FragmentStatePagerAdapter
    {
        private Context context;
        public ListGridAdapter(FragmentManager fm, Context context) {

            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) 
        {
            //position = position % 2;
            Fragment reference = null;
            if(position == 0)
            {
                Log.e("StarActivity","Fragment at position: "+ position);
                //// TODO: 2/7/16
                ListFragment.setContext(context);
                reference = new ListFragment();
                //pointer.instantiate(StarActivity.this, ListFragment.class.toString());
                if(list_images != null)
                {
                    ((ListFragment) reference).setImages(list_images);
                }
            }
            if(position == 1)
            {
                Log.e("StarActivity", "Fragment at position: " + position);
                //// TODO: 2/7/16
                GridFragment.setContext(context);

                reference = new GridFragment();
                if(user_images != null)
                {
                    ((GridFragment) reference).setBitmap(user_images);
                }
            }
            return reference;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
