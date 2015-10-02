package com.mydomain.todaytodolist;

import java.util.UUID;

/**
 * Created by David on 9/24/2015.
 */
public class ToDo {

    private String mToDo;
    private String mExtraInfo=null;
    private UUID mID;
    private String mRemainder;

    public ToDo(String toDo){
        mToDo=toDo;
        mID=UUID.randomUUID();
        mRemainder=null;
    }

    public String getExtraInfo() {
        return mExtraInfo;
    }

    public void setExtraInfo(String mExtraInfo) {
        this.mExtraInfo = mExtraInfo;
    }

    public String getToDo() {
        return mToDo;
    }

    public void setToDo(String mToDo) {
        this.mToDo = mToDo;
    }

    public UUID getId() {
        return mID;
    }

    public void setID(UUID mID) {
        this.mID = mID;
    }

    public String getRemainder() {
        return mRemainder;
    }

    public void setRemainder(String mRemainder) {
        this.mRemainder = mRemainder;
    }
}
