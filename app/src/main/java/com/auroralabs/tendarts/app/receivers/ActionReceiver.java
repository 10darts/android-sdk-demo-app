package com.auroralabs.tendarts.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ActionReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = ActionReceiver.class.getSimpleName();

    public static final String ACTION_REPLY = "com.tendarts.demo.ACTION_REPLY";
    public static final String ACTION_TEST = "com.tendarts.demo.ACTION_TEST";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {

            Bundle extras = intent.getExtras();
            if (extras == null) {
                Log.e(LOG_TAG, "onReceive: no extras");
                return;
            }

            String intentAction = intent.getAction();
            String actionId = extras.getString("action_id");
            String actionCommand = extras.getString("action_command");

            String message = "Notification action clicked: " + actionCommand;
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

        }

    }

}
