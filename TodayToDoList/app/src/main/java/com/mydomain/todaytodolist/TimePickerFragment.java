package com.mydomain.todaytodolist;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by David on 9/30/2015.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String EXTRA_HOUR="hour";
    public static final String EXTRA_MINUTE="minute";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c=Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view ,int hourOfDay,int minute){

        Intent i=new Intent();
        i.putExtra(EXTRA_HOUR,hourOfDay);
        i.putExtra(EXTRA_MINUTE,minute);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,i);
    }

}
