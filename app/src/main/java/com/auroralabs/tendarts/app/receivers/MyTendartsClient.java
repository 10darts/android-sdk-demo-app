package com.auroralabs.tendarts.app.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.auroralabs.tendarts.BuildConfig;
import com.auroralabs.tendarts.app.activities.MainActivity;
import com.auroralabs.tendarts.domain.entities.LogEntity;
import com.tendarts.sdk.client.TendartsClient;

import java.lang.ref.WeakReference;

/**
 * Created by luisma on 6/2/18.
 */

public class MyTendartsClient extends TendartsClient {

    private static final String LOG_TAG = MyTendartsClient.class.getSimpleName();

    private WeakReference<Context> contextWeakReference;

    @Override
    public String mainActivityClassName() {
        return MainActivity.class.getName();
    }

    @Override
    public CharSequence getLocationExplanation(Context context) {
        // Returns the explanation of why your app uses location and why permission should be granted
        return "We need permissions to access your location so we can send you notifications depending on where you are";
    }

    @Override
    public void performSetup(Context context) {

        contextWeakReference = new WeakReference<Context>(context);

//        // The SDK configuration is done in the performSetup() function, just add your custom configuration here
//        TendartsSDK.instance().stackNotifications(true)
//                .alwaysShowLastNotification(true)
//                .limitNotificationSoundAndVibrationTime(true)
//                .setLargeIconResource(R.mipmap.ic_launcher);

    }

    @Override
    public void onUserLinkedToDevice() {

        // Called if 'linkDeviceWithUserIdentifier' fails and then the retry works

        if (contextWeakReference != null && contextWeakReference.get() != null) {
            Toast.makeText(contextWeakReference.get(), "Device linked after retry", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void logEvent(String category, String type, String message) {

        if (contextWeakReference != null && contextWeakReference.get() != null) {
            // Local broadcast to show log
            Intent intent = new Intent(MainActivity.LOG_BROADCAST_INTENT_FILTER);
            intent.putExtra(MainActivity.LOG_EXTRA, new LogEntity(category, type, message));
            LocalBroadcastManager.getInstance(contextWeakReference.get()).sendBroadcast(intent);
        }

        if (BuildConfig.DEBUG) {

            Log.d(LOG_TAG, "category: '" + category + "', type: '" + type + "', message: '" + message + "'");

        }

    }
}
