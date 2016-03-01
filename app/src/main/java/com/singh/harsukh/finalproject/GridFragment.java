package com.singh.harsukh.finalproject;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private ArrayList<TextRow> textRows = new ArrayList<>();
    private Bitmap bitmap_to_send;
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
        context.registerReceiver(mBroadcastReceiver, filter); // Don't forget to unregister during onDestroy
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
        Bundle outBundle = new Bundle();
        if(mBluetoothAdapter != null)
        {
//            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices(); //get a set of bluetooth devices
//            if (pairedDevices.size() > 0) {
//                FragmentManager manager = getFragmentManager();
//                BluetoothDialog dialog = new BluetoothDialog();
//                dialog.setPairedDevices(pairedDevices);
//                dialog.setContext(context);
//                dialog.show(manager, "dialog");
//            }
            bitmap_to_send = (Bitmap)gridView.getItemAtPosition(position);
            if(textRows != null) {
                FragmentManager manager = getFragmentManager();
                BluetoothDialog dialog = new BluetoothDialog();
                dialog.setContext(context);
                //dialog.setTextRows(textRows);
                outBundle.putParcelableArrayList("textrows", textRows);
                outBundle.putParcelable("image", bitmap_to_send);
                dialog.setBluetoothAdapter(mBluetoothAdapter);
                dialog.setArguments(outBundle);
                dialog.setCancelable(false);
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
            if(imageArray != null)
                return imageArray.get(position);
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

    //create a receiver action found intent
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //when discovery finds a device
            if(BluetoothDevice.ACTION_FOUND == action)
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                TextRow row = new TextRow(device.getName(), device.getBondState(), device.getAddress());
                textRows.add(row);
            }
        }
    };
    // Register the BroadcastReceiver
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
}
