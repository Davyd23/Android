package com.mydomain.todaytodolist;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David on 9/24/2015.
 */
public class ToDoLab {

    private ArrayList<ToDo> mToDoS;
    private static ToDoLab mLab;
    private DatabaseHelper mHelper;

    private ToDoLab(DatabaseHelper helper){
        mToDoS=new  ArrayList<ToDo>();

        mHelper=helper;
    }

    public static ToDoLab newInstance(DatabaseHelper helper){
        if(mLab ==null){
            mLab=new ToDoLab(helper);
        }

        return mLab;
    }
    //for the single view
    public static ToDoLab newInstance(Context context){
        if(mLab ==null){
            DatabaseHelper helper=DatabaseHelper.newInstance(context);
            mLab=new ToDoLab(helper);
        }

        return mLab;
    }

    //when navigating up from task the lab class is alrady instantiated so it dosn't empty it's ArrayList , we need to empty it manually
    public void emptyToDoS(){
        mToDoS=new ArrayList<>();
    }

    public void addToDo(ToDo toDo){

        mToDoS.add(toDo);
    }

    public void removeToDo(ToDo toDo){
        for(ToDo t:mToDoS){
            if((t.getId())==(toDo.getId())){
                mToDoS.remove(t);
                break;
            }
        }
    }

    public ArrayList<ToDo> getItems(){
        return mToDoS;
    }


    public void saveAll(){
        mHelper.resetDatabase();

        for(ToDo todo:mToDoS){
            mHelper.insertToDo(todo);
        }
    }


}
