package com.singh.harsukh.finalproject;

import android.app.FragmentManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by harsukh on 2/29/16.
 */
public class BluetoothDialog extends DialogFragment {
    public BluetoothDialog() {
        super();
    }
    private Set<BluetoothDevice> pairedDevices = null;
    private Context context;
    private ArrayList<TextRow> textRows = null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bluetooth_dialog, container);
        ListView list = (ListView) v.findViewById(R.id.bluetooth_id);
        setCancelable(true);
        getDialog().setTitle("Paired Devices");
        if(pairedDevices != null)
        {
             //Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array list to show in a ListView
                String address  =device.getAddress();
                String name  = device.getName();
                int type = device.getBondState();
                TextRow row = new TextRow(name, type, address);
                textRows.add(row);
            }
        }
        if(context != null)
        {
            list.setAdapter(new TextArrayAdapter(context, textRows));
        }
        return v;
    }

    public void setPairedDevices(Set<BluetoothDevice> devices)
    {
        pairedDevices = devices;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }


    private class TextArrayAdapter extends ArrayAdapter
    {
        Context context;
        ArrayList<TextRow> rows;
        public TextArrayAdapter(Context context, ArrayList<TextRow> rows)
        {
            super(context, R.layout.text_layout, rows);
            this.context = context;
            this.rows = rows;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;
            if(row == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row  = inflater.inflate(R.layout.text_layout, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) row.getTag();
            }
            holder.myName.setText(rows.get(position).name);
            holder.myType.setText(rows.get(position).type);
            holder.myAddress.setText(rows.get(position).address);
            return row;
        }

        private class ViewHolder {
            protected TextView myName;
            protected TextView myType;
            protected TextView myAddress;

            public ViewHolder(View v){
                myName = (TextView) v.findViewById(R.id.text_name);
                myType = (TextView) v.findViewById(R.id.text_type);
                myAddress = (TextView) v.findViewById(R.id.text_address);
            }
        }

    }
}
