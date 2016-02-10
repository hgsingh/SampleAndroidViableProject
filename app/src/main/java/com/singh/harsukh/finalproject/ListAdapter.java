package com.singh.harsukh.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter
{
    Context context;
    ArrayList<SingleRow> images;
    public ListAdapter(Context context, ArrayList<SingleRow> images)
    {
        super(context, R.layout.single_row, images);
        this.context = context;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row  = inflater.inflate(R.layout.single_row, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }
        holder.myImage.setImageBitmap(images.get(position).thumb);
        holder.myUser.setText(images.get(position).title);
        holder.myName.setText(images.get(position).description);
        return row;
    }

        //holds the views to reduce the function calls
    private class ViewHolder {
        protected ImageView myImage;
        protected TextView myUser;
        protected TextView myName;

        public ViewHolder(View v){
            myImage = (ImageView) v.findViewById(R.id.imageView);
            myUser = (TextView) v.findViewById(R.id.textView);
            myName = (TextView) v.findViewById(R.id.textView2);
        }
    }
}

///**
// * Created by harsukh on 2/4/16.
// */
//public class ListAdapter extends BaseAdapter {
//    ArrayList<SingleRow> list;
//    Context mContext;
//    int[] imgs;
//    Bitmap[] thumbs;
//    String[] userArray;
//    String[] nameArray;
//
//    public ListAdapter(Context c, String[] arg1, Bitmap[] arg2, String[] arg3)
//    {
//        this.mContext = c;
//        list = new ArrayList<SingleRow>();
//        this.thumbs = arg2;
//        this.userArray = arg1;
//        this.nameArray= arg3;
//        for (int i=0; i<userArray.length; i++){
//            list.add(new SingleRow(userArray[i], nameArray[i], thumbs[i]));
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//        MyViewHolder holder = null;
//        if(row == null)
//        {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row  = inflater.inflate(R.layout.single_row, parent, false);
//            holder = new MyViewHolder(row);
//            row.setTag(holder);
//        }
//        else
//        {
//            holder = (MyViewHolder) row.getTag();
//        }
//        holder.myImage.setImageBitmap(thumbs[position]);
//        holder.myUser.setText(userArray[position]);
//        holder.myName.setText(nameArray[position]);
//        return row;
//    }
//
//    //holds the views to reduce the function calls
//    class MyViewHolder {
//        protected ImageView myImage;
//        protected TextView myUser;
//        protected TextView myName;
//
//        MyViewHolder(View v){
//            myImage = (ImageView) v.findViewById(R.id.imageView);
//            myUser = (TextView) v.findViewById(R.id.textView);
//            myName = (TextView) v.findViewById(R.id.textView2);
//        }
//    }
//}

