package com.singh.harsukh.finalproject;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by harsukh on 2/8/16.
 */
public class GridFragment extends Fragment implements AdapterView.OnItemClickListener
{
    private GridView gridView;
//    private static StarActivity activity;
    private static Context context;
    private ArrayList<Bitmap> bitmap;
    private static BluetoothAdapter mBluetoothAdapter = null;
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
        if(savedInstanceState != null && bitmap == null)
        {
            Log.d("StarActivity", "Lord Freeza da fuck?");
            bitmap = savedInstanceState.getParcelableArrayList("BKEY");
            if(bitmap != null)
                gridView.setAdapter(new ImageAdapter(getContext(), bitmap));
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

    public static void setBluetoothAdapter(BluetoothAdapter adapter)
    {
        if(mBluetoothAdapter == null)
            mBluetoothAdapter = adapter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("BKEY", bitmap);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mBluetoothAdapter != null)
        {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices(); //get a set of bluetooth devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
//                for (BluetoothDevice device : pairedDevices) {
//                    // Add the name and address to an array adapter to show in a ListView
//                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                }
                FragmentManager manager = getFragmentManager();
                BluetoothDialog dialog = new BluetoothDialog();
                dialog.show(manager, "dialog");

            }
        }
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
