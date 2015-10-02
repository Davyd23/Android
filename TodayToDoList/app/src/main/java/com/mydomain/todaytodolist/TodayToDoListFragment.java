package com.mydomain.todaytodolist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 9/24/2015.
 */
public class TodayToDoListFragment extends ListFragment implements ToDoFragment.ToDoCallback {

    private ArrayList<ToDo> mToDoS;
    private ToDoLab mLab;
    private DatabaseHelper mHelper;

    public static final String EXTRA_ID="com.mydomain.todaytodolist.EXTRA_ID";
    private static final int EXTRA_CODE_TO_DO=0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        mHelper=DatabaseHelper.newInstance(getActivity());

        mLab=ToDoLab.newInstance(mHelper);
        mLab.emptyToDoS();
        ArrayList<ToDo> values=mHelper.loadToDoS();

        for(int i=0;i<values.size();i++){
            mLab.addToDo(values.get(i));
        }
        mToDoS=mLab.getItems();

        ToDoListAdapter adapter=new ToDoListAdapter(mToDoS);
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstaceState){
        View v=inflater.inflate(R.layout.activities_empty_view,parent,false);

        Button addButton=(Button)v.findViewById(R.id.empty_view_addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogToDo dialog = new DialogToDo();

                FragmentManager fm = getActivity().getSupportFragmentManager();

                dialog.setTargetFragment(TodayToDoListFragment.this, EXTRA_CODE_TO_DO);

                dialog.show(fm, "dialog");
            }
        });
        return v;
    }

    public void onActivityResult(int request_code,int result_code,Intent data){
        if(result_code!= Activity.RESULT_OK) return;

        if(request_code==EXTRA_CODE_TO_DO){

            ToDo toDo=new ToDo(data.getStringExtra(DialogToDo.EXTRA_TO_DO));
            mLab.addToDo(toDo);
            updateUI();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu,menuInflater);

        menuInflater.inflate(R.menu.menu_today_to_do_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.menu_add:

                DialogToDo dialog=new DialogToDo();

                FragmentManager fm=getActivity().getSupportFragmentManager();

                dialog.setTargetFragment(TodayToDoListFragment.this, EXTRA_CODE_TO_DO);

                dialog.show(fm,"dialog");

                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void UpdateToDo() {
        updateUI();
    }

    private class ToDoListAdapter extends ArrayAdapter<ToDo>{

        public ToDoListAdapter(ArrayList<ToDo> toDo){
            super(getActivity(),0,toDo);
        }

        public View getView(int position,View convertView,ViewGroup parent){
            if(convertView==null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.activities_list_view,parent,false);
            }

            ToDo item=getItem(position);
            TextView tv=(TextView)convertView.findViewById(R.id.list_view_textView);
            tv.setText(item.getToDo());

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l,View v,int position,long id ){

        ToDo toDo=((ToDoListAdapter)getListAdapter()).getItem(position);

        Intent i=new Intent(getActivity(),ToDoActivity.class);
        i.putExtra(EXTRA_ID,toDo.getId());

        startActivity(i);
    }

    public void updateUI(){
        ((ToDoListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onPause(){

        mLab.saveAll();

        super.onPause();
    }
}
