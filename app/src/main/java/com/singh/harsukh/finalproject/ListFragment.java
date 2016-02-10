package com.singh.harsukh.finalproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by harsukh on 2/7/16.
 */
public class ListFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private ListView listView;
    private ArrayList<SingleRow> arrayList;
    private static Context context;
    private View v;
    public ListFragment()
    {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = (View) inflater.inflate(R.layout.frag_tab_list, container, false);
        listView = (ListView) v.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        return v;
    }
    public static void setContext(Context local_context)
    {
        context = local_context;
    }

    public void setImages(ArrayList<SingleRow> images)
    {
        this.arrayList = images;
        listView = (ListView) v.findViewById(R.id.listView);
        if(images != null && context != null) {
            listView.setAdapter(new ListAdapter(context, images));
        }
        else
            Log.e("ListFragment", "null values");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String _id = arrayList.get(position).getId();
        Download downloader = new Download((StarActivity)getActivity());
        downloader.execute("recentImages", _id,getResources().getString(R.string.insta_client_id));
    }
}
