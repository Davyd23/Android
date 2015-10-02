package com.mydomain.todaytodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mDatabase;

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="Todo.sql";
    private static final String TABLE_NAME="ToDo";
    private static final String COLUMN_ID="_ID";
    private static final String COLUMN_TITLE="title";
    private static final String COLUMN_INFO="info";
    private static final String COLUMN_REMAINDER="remainder";
    private static final String TEXT_TYPE=" TEXT";

    private static final String SQL_CREATE_ENTRIES=
            "CREATE TABLE "+TABLE_NAME+"("+COLUMN_ID+" INTEGER PRIMARY KEY,"+
                    COLUMN_TITLE+TEXT_TYPE+","+
                    COLUMN_INFO+TEXT_TYPE+","+
                    COLUMN_REMAINDER+TEXT_TYPE+")";


    private DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static DatabaseHelper newInstance(Context context){

        if(mDatabase==null){
            mDatabase=new DatabaseHelper(context);
        }

        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertToDo(ToDo toDo){

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE,toDo.getToDo());
        cv.put(COLUMN_INFO,toDo.getExtraInfo());
        cv.put(COLUMN_REMAINDER, toDo.getRemainder());
        db.insert(TABLE_NAME,null,cv);
    }

    public ArrayList<ToDo> loadToDoS(){

        ArrayList<ToDo> ToDoS=new ArrayList<>();

        Cursor cursor=getReadableDatabase().query(TABLE_NAME,null,null,null,null,null,COLUMN_ID+" ASC");

        if(cursor.moveToFirst()){
            do{
                String title=cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String extra_info=cursor.getString(cursor.getColumnIndex(COLUMN_INFO));
                String remainder=cursor.getString(cursor.getColumnIndex(COLUMN_REMAINDER));

                ToDo todo=new ToDo(title);
                todo.setExtraInfo(extra_info);
                todo.setRemainder(remainder);

                ToDoS.add(todo);
            }while (cursor.moveToNext());
        }
        return ToDoS;
    }

    public void deleteToDo(String toDo){

        String selection=COLUMN_TITLE+"=?";
        String[] values={toDo};

        getReadableDatabase().delete(TABLE_NAME,selection,values);
    }

    public void resetDatabase(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME); //delete all rows in a table
        db.close();
    }
}
