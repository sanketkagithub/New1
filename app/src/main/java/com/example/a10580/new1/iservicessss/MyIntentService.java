package com.example.a10580.new1.iservicessss;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("gogogo");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

       Bundle bundle = intent.getExtras();

       if (bundle!=null)
       {
       String carName =    bundle.getString("carName");

           Toast.makeText(this, "carName " + carName, Toast.LENGTH_SHORT).show();
       }

    }




}
