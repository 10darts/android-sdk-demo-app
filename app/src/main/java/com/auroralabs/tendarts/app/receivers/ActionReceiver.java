package com.auroralabs.tendarts.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ActionReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = ActionReceiver.class.getSimpleName();

    public static final String ACTION_REPLY = "com.tendarts.demo.ACTION_REPLY";
    public static final String ACTION_TEST = "com.tendarts.demo.ACTION_TEST";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {

            String action = intent.getAction();

            Bundle extras = intent.getExtras();
            if (extras == null) {
                Log.e(LOG_TAG, "onReceive: no extras");
                return;
            }

            // TODO: luisma: parse intent and do something
        }

    }

}
