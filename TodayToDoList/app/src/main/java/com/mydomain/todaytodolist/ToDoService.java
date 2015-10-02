package com.mydomain.todaytodolist;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.Calendar;

public class ToDoService extends Service {
    private Context mContext;
    private long mTimeUntilNotification; //in millies
    private String mText;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;



    private final class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){

            while (System.currentTimeMillis() <mTimeUntilNotification) {
                synchronized (this) {
                    try {
                        wait(mTimeUntilNotification - System.currentTimeMillis());

                        Notification not=new Notification.Builder(mContext)
                                .setContentTitle("To do list")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentText(mText).build();

                        NotificationManager notMan=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        notMan.notify(0, not);
                    } catch (Exception e) {
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate(){
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimeUntilNotification=intent.getLongExtra(ToDoFragment.EXTRA_TIME,0);
        mText=intent.getStringExtra(ToDoFragment.EXTRA_REMAINDER);
        mContext=this;

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }
}
