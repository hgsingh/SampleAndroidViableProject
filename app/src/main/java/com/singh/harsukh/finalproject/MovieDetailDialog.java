package com.singh.harsukh.finalproject;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by harsukh on 2/9/16.
 */
public class MovieDetailDialog extends DialogFragment
{
    private ImageView imageView;
    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            View v = inflater.inflate(R.layout.movie_detail, container);
            imageView = (ImageView) v.findViewById(R.id.popularMovieView);
            textView = (TextView) v.findViewById(R.id.popularMovieDesc);
            String description = getArguments().getString("description");
            textView.setText(description);
            Bitmap thumb = getArguments().getParcelable("image");
            imageView.setImageBitmap(thumb);
            setCancelable(true);
            getDialog().setTitle("Popular Movies");
            return v;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
