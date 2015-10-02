package com.mydomain.todaytodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;


public class ToDoFragment extends Fragment {

    private static final String EXTRA_ID="extra_id";
    private static final int EXTRA_PIKER_TIME=1;
    private static final int EXTRA_PICKER_DATE=0;

    public static final String EXTRA_TIME="com.mydomain.todaytodolist.EXTRA_TIME";
    public static final String EXTRA_REMAINDER="com.mydomain.todaytodolist.EXTRA_REMAINDER";

    private int timesCalled=0;

    private Calendar mCallendar;

    private ToDoLab mLab;
    private ToDo mToDo;
    ToDoCallback mCallBack;

    private TextView mTitle;
    private CheckBox mCheckBox;
    private Button mRemainderButton;
    private EditText mExtraInfo;

    private boolean mChecked;

    private ToDoFragment(){};

    public static Fragment newInstance(UUID id){

        ToDoFragment fragment=new ToDoFragment();

        Bundle args= new Bundle();
        args.putSerializable(EXTRA_ID,id);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        mCallBack=new ToDoCallback() {
            @Override
            public void UpdateToDo() {

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){

        View v=inflater.inflate(R.layout.to_do_view, parent, false);

        mLab=ToDoLab.newInstance(getActivity());

        ArrayList<ToDo> toDoS=mLab.getItems();

        for(int i=0;i< toDoS.size();i++){
            if((toDoS.get(i)).getId().equals((UUID)getArguments().getSerializable(EXTRA_ID))){
                mToDo=toDoS.get(i);
                break;
            }
        }

        mTitle=(TextView)v.findViewById(R.id.to_do_view_textView);
        mTitle.setText(mToDo.getToDo());

        mCheckBox=(CheckBox) v.findViewById(R.id.to_do_view_solvedCheckBox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mChecked = isChecked;
            }
        });

        mRemainderButton=(Button)v.findViewById(R.id.remainder_button);
        if(mToDo.getRemainder()!=null){
            mRemainderButton.setText(mToDo.getRemainder());
        }
        mRemainderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();

                dialog.setTargetFragment(ToDoFragment.this, EXTRA_PICKER_DATE);

                dialog.show(fm, "dialog");
            }
        });

        mExtraInfo=(EditText)v.findViewById(R.id.to_do_view_editText);
        mExtraInfo.setText(mToDo.getExtraInfo());
        mExtraInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mToDo.setExtraInfo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int request_code,int resultCode,Intent data){
        if(resultCode!= Activity.RESULT_OK) return;

        if(request_code==EXTRA_PICKER_DATE){
            if(timesCalled==1) {
                timesCalled=0;
                return;
            }
            timesCalled++;

            int year=data.getIntExtra(DatePickerFragment.EXTRA_YEAR,0);
            int month=data.getIntExtra(DatePickerFragment.EXTRA_MONTH,0);
            int day=data.getIntExtra(DatePickerFragment.EXTRA_DAY,0);
            mCallendar=new GregorianCalendar(year,month,day);


            TimePickerFragment dialog= new TimePickerFragment();
            FragmentManager fm=getActivity().getSupportFragmentManager();

            dialog.setTargetFragment(ToDoFragment.this, EXTRA_PIKER_TIME);

            dialog.show(fm, "dialog");
        }

        if(request_code==EXTRA_PIKER_TIME){
            if(timesCalled==1) {
                timesCalled=0;
                return;
            }

            int hour =data.getIntExtra(TimePickerFragment.EXTRA_HOUR,0);
            int minute=data.getIntExtra(TimePickerFragment.EXTRA_MINUTE,0);

            mCallendar.set(Calendar.HOUR_OF_DAY,hour);
            mCallendar.set(Calendar.MINUTE, minute);

            Intent i=new Intent(getActivity(),ToDoService.class);
            i.putExtra(EXTRA_TIME,mCallendar.getTimeInMillis());
            i.putExtra(EXTRA_REMAINDER,"Remember to do "+mToDo.getToDo());
            getActivity().startService(i);

            String remainder="Alarm set on "+mCallendar.get(Calendar.DAY_OF_MONTH)+"."+(mCallendar.get(Calendar.MONTH)+1) //first month is set as 0
                    +"."+mCallendar.get(Calendar.YEAR)+" at "+hour+":"+minute;
            mToDo.setRemainder(remainder);

            mRemainderButton.setText(remainder);
        }



    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case android.R.id.home :

                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    interface ToDoCallback{

        public void UpdateToDo();
    }

    @Override
    public void onPause(){

        if(mChecked==true)
        {
            mLab.removeToDo(mToDo);
        }
        mLab.saveAll();
        mCallBack.UpdateToDo();

        super.onPause();
    }

}
