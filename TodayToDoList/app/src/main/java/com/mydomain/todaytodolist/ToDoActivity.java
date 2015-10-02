package com.mydomain.todaytodolist;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class ToDoActivity extends SingleFragmentActivity{

    public Fragment createFragment(){

        UUID id=(UUID) getIntent().getSerializableExtra(TodayToDoListFragment.EXTRA_ID);

        return ToDoFragment.newInstance(id);
    }
}
