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
    private Bitmap bitmap;
    private String description;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.description = getArguments().getString("description");
        System.out.println(description);
        this.bitmap= getArguments().getParcelable("image");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.movie_detail, container);
        imageView = (ImageView) v.findViewById(R.id.popularMovieView);
        textView = (TextView) v.findViewById(R.id.popularMovieDesc);
        textView.setText(description);
        imageView.setImageBitmap(bitmap);
        setCancelable(true);
        getDialog().setTitle("Popular Movies");
        return v;
    }
}
