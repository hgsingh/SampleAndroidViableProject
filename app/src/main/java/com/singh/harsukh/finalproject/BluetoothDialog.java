package com.singh.harsukh.finalproject;

import android.app.FragmentManager;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

/**
 * Created by harsukh on 2/29/16.
 */
public class BluetoothDialog extends DialogFragment {
    public BluetoothDialog() {
        super();
    }
    private Set<BluetoothDevice> pairedDevices = null;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bluetooth_dialog, container);
        setCancelable(true);
        getDialog().setTitle("Paired Devices");
        return v;
    }

    public void setPairedDevices(Set<BluetoothDevice> devices)
    {
        pairedDevices = devices;
    }
}
