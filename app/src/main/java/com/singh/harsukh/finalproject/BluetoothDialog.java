package com.singh.harsukh.finalproject;

import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by harsukh on 2/29/16.
 */
public class BluetoothDialog extends DialogFragment implements AdapterView.OnItemClickListener {
    public BluetoothDialog() {
        super();
    }
    //private Set<BluetoothDevice> pairedDevices = null;
    private Context context;
    private ArrayList<TextRow> textRows = null;
    private Bitmap bm;
    private static BluetoothAdapter mBluetoothAdapter;
    private static final UUID MY_UUID =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static ListView list;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bluetooth_dialog, container);
        list = (ListView) v.findViewById(R.id.bluetooth_id);
        getDialog().setTitle("Found Devices");
//        if(pairedDevices != null)
//        {
//             //Loop through paired devices
//            for (BluetoothDevice device : pairedDevices) {
//                // Add the name and address to an array list to show in a ListView
//                String address  =device.getAddress();
//                String name  = device.getName();
//                int type = device.getBondState();
//                TextRow row = new TextRow(name, type, address);
//                textRows.add(row);
//            }
//        }
        textRows = getArguments().getParcelableArrayList("textrows");
        bm = getArguments().getParcelable("image");
        //set the bluetooth to send here
        if(context != null && textRows != null)
        {
            list.setAdapter(new TextArrayAdapter(context, textRows));
        }
        return v;
    }

//    public void setPairedDevices(Set<BluetoothDevice> devices)
//    {
//        pairedDevices = devices;
//    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        TextRow row  = (TextRow)list.getItemAtPosition(position);
        SendData thread = new SendData(row.address, mBluetoothAdapter, bm);
        thread.sendMessage();
        dismiss();
    }

//    public void setTextRows(ArrayList arrayList)
//    {
//        textRows = arrayList;
//    }

    public static void setBluetoothAdapter(BluetoothAdapter adpater)
    {
       mBluetoothAdapter = adpater;
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
    class SendData extends Thread {
        private BluetoothDevice device = null;
        private BluetoothSocket btSocket = null;
        private OutputStream outStream = null;
        private BluetoothAdapter mBluetoothAdapter;
        private Bitmap bm;
        public SendData(String address, BluetoothAdapter adapter, Bitmap bm){
            mBluetoothAdapter = adapter;
            device = mBluetoothAdapter.getRemoteDevice(address);
            this.bm = bm;
            try
            {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            mBluetoothAdapter.cancelDiscovery();
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            Toast.makeText(context, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show();
            try {
                outStream = btSocket.getOutputStream();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage()
        {
            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100,baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                Toast.makeText(context, String.valueOf(b.length), Toast.LENGTH_SHORT).show();
                outStream.write(b);
                outStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
GOOGLES EXAMPLE OF MAKING A CONNECTION
private class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        manageConnectedSocket(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket */
//public void cancel() {
//    try {
//        mmSocket.close();
//    } catch (IOException e) { }
//}
//}

/*
GOOGLES EXAMPLE OF MANAGING A CONNECTION
private class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

//    /* Call this from the main activity to send data to the remote device */
//public void write(byte[] bytes) {
//    try {
//        mmOutStream.write(bytes);
//    } catch (IOException e) { }
//}
//
//    /* Call this from the main activity to shutdown the connection */
//    public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) { }
//    }
//}
