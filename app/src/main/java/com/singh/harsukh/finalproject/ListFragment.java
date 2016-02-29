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
    //private View v;
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
        View v = (View) inflater.inflate(R.layout.frag_tab_list, container, false);
        listView = (ListView) v.findViewById(R.id.listView);
        if(arrayList != null && context != null) {
            if(savedInstanceState == null)
                listView.setAdapter(new ListAdapter(context, arrayList));
        }
        if(savedInstanceState != null)
        {
            arrayList = savedInstanceState.getParcelableArrayList("SERIAL_KEY");
            listView.setAdapter(new ListAdapter(getContext(), arrayList));
        }
        else
            Log.e("ListFragment", "null values");
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.e("ListFragment", "Item clicked at position: "+position);
        String _id = arrayList.get(position).getId();
        Download downloader = new Download((StarActivity)getActivity());
        downloader.execute("recentImages", _id,getResources().getString(R.string.insta_client_id));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("SERIAL_KEY", arrayList);
    }
}
