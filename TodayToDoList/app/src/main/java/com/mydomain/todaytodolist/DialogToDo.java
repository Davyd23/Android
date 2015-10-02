package com.mydomain.todaytodolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by David on 9/25/2015.
 */
public class DialogToDo extends DialogFragment {

    private String mToDO;
    public static final String EXTRA_TO_DO="com.mydomain.todaytodolist.EXTRA_TO_DO";

    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreateDialog(savedInstanceState);

        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_view,null);
        EditText text=(EditText)v.findViewById(R.id.dialog_editText);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mToDO = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK);
            }
        });

        return builder.create();
    }

    public void sendResult(int result_code){

        if(getTargetFragment()==null) return;

        Intent i=new Intent();
        i.putExtra(EXTRA_TO_DO,mToDO);
        getTargetFragment().onActivityResult(getTargetRequestCode(),result_code ,i);
    }
}
