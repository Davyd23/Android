package com.mydomain.todaytodolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by David on 9/30/2015.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_MONTH="month";
    public static final String EXTRA_YEAR="year";
    public static final String EXTRA_DAY="day";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);

    }

    public void onDateSet(DatePicker view ,int year,int month,int day){

        Intent i=new Intent();

        i.putExtra(EXTRA_DAY,day);
        i.putExtra(EXTRA_MONTH,month);
        i.putExtra(EXTRA_YEAR,year);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

    }
}
