package com.singh.harsukh.finalproject;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by harsukh on 2/29/16.
 */
public class BluetoothDialog extends DialogFragment {
    public BluetoothDialog() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bluetooth_dialog, container);
        setCancelable(true);
        getDialog().setTitle("Paired Devices");
        return v;
    }
}
