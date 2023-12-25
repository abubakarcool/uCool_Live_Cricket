package com.example.samplestickerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MY_TAG", "Bootup : OnRecieve, is called for run after boot "+Thread.currentThread().getName());
//        intent = new Intent( context, anyService_to_run_after_boot.class );
//        context.startService(intent);
    }
}