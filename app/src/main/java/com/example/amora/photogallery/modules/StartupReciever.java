package com.example.amora.photogallery.Modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by amora on 12/3/2015.
 */
public class StartupReciever extends BroadcastReceiver {

    private static final String TAG = "StartupReciever";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Recieved broadcast intent: " + intent.getAction());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isOn = prefs.getBoolean(PollService.PREF_IS_ALARM_ON, false);
        PollService.setServiceAlarm(context, isOn);

    }
}
