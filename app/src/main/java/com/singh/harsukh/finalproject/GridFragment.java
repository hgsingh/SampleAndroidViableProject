package com.singh.harsukh.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by harsukh on 2/8/16.
 */
public class GridFragment extends Fragment
{
    private GridView gridView;
//    private static StarActivity activity;
    private static Context context;
    private ArrayList<Bitmap> bitmap;
    public GridFragment()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tab_grid, container, false);
        gridView = (GridView) v.findViewById(R.id.gridview);
//        activity = (StarActivity) getActivity();
        if(bitmap != null)
        {
            gridView.setAdapter(new ImageAdapter(context, bitmap));
        }
        else
        {
            Log.e("StarActivity", "Lord Freeza da fuck?");
        }
        return v;
    }
    public static void setContext(Context local_context)
    {
        context = local_context;
    }

    public void setBitmap(ArrayList arrayList)
    {
        this.bitmap = arrayList;
    }
    private class ImageAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<Bitmap> imageArray;

        public ImageAdapter(Context context, ArrayList imageArray)
        {
            this.context = context;
            this.imageArray = imageArray;

        }

        @Override
        public int getCount() {
            return imageArray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null)
            {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8,8,8,8);
            }
            else
            {
                imageView = (ImageView) convertView;
            }
            imageView.setImageBitmap(imageArray.get(position));
            return imageView;
        }
    }
}
