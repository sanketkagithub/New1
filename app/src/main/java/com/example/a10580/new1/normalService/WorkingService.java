package com.example.a10580.new1.normalService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkingService extends Service
{

ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("startedFromAlarm"," startedFromAlarm");

        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {

                //comntinuos task
            }
        },5000, TimeUnit.MILLISECONDS);

        return super.onStartCommand(intent, flags, startId);
    }
}
