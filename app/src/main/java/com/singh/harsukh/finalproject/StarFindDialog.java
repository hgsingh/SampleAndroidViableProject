package com.singh.harsukh.finalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by harsukh on 2/4/16.
 */
public class StarFindDialog extends DialogFragment implements View.OnClickListener {

    private Button mButton;
    private EditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.download_dialog, container);
        mButton = (Button) v.findViewById(R.id.button2);
        mEditText = (EditText) v.findViewById(R.id.editText4);
        mEditText.setText("");
        mButton.setOnClickListener(this);
        setCancelable(false);
        getDialog().setTitle("Enter A Stars Name!");
        return v;
    }


    @Override
    public void onClick(View v) throws NullPointerException
    {
        if(v.getId() == R.id.button2)
        {
            String str_check = mEditText.getText().toString();
            if(str_check == null)
                throw new NullPointerException();
            if(str_check.equals(""))
            {
                MainActivity m = (MainActivity) getActivity();
                m.onDialogMessage("Please Enter A Valid Name!!!!");
            }
            else
            {
                Download insta_search = new Download((MainActivity) getActivity());
                insta_search.execute("instagram", str_check, getResources().getString(R.string.insta_client_id));
            }
        }
        dismiss();
    }
}
