package com.auroralabs.tendarts.app.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.auroralabs.tendarts.R;
import com.auroralabs.tendarts.app.adapters.LogAdapter;
import com.auroralabs.tendarts.app.fragments.SetUserDialogFragment;
import com.auroralabs.tendarts.domain.entities.LogEntity;
import com.crashlytics.android.Crashlytics;
import com.tendarts.sdk.TendartsSDK;
import com.tendarts.sdk.common.Configuration;
import com.tendarts.sdk.common.ConstantsBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_EXTRA = "log_extra";
    public static final String LOG_BROADCAST_INTENT_FILTER = "log_broadcast_intent_filter";

    @BindView(R.id.environment_text)
    TextView environmentText;

    @BindView(R.id.access_token_text)
    TextView accessTokenText;

    @BindView(R.id.sender_id_text)
    TextView senderIdText;

    @BindView(R.id.model_text)
    TextView modelText;

    @BindView(R.id.android_version_text)
    TextView androidVersionText;

    @BindView(R.id.link_device_with_user_button)
    Button linkDeviceWithUserButton;

    @BindView(R.id.log_recycler_view)
    RecyclerView logRecyclerView;

    private LogAdapter logAdapter;
    private List<LogEntity> logEntityList = new ArrayList<>();
    private boolean shouldScrollToTop;
    private String userName = "";

    private BroadcastReceiver logBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Do logout
            LogEntity logEntity = intent.getParcelableExtra(LOG_EXTRA);

            updateLog(logEntity);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);

        configureView();

        // Set up SDK
        TendartsSDK.onCreate(savedInstanceState, this, new TendartsSDK.ILocationAlerter() {
            @Override
            public void alertNotEnabled(Activity activity) {

                // Handler needed to fix error "sending message to a Handler on a dead thread"
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        showMessage("Location not enabled");

                    }
                });


            }
        });

        // To get notified when a new location is available, you should register your listener
        TendartsSDK.registerGeoLocationReceiver(new TendartsSDK.IGeoLocationReceiver() {
            @Override
            public void onNewLocation(TendartsSDK.GeoLocation geoLocation) {
                // New location available
            }
        });

        // Enable geolocation updates (enabled by default)
        //TendartsSDK.enableGeolocationUpdates();

        // Disable geolocation updates:
        //TendartsSDK.disableGeolocationUpdates();

        LocalBroadcastManager.getInstance(this).registerReceiver(logBroadcastReceiver, new IntentFilter(LOG_BROADCAST_INTENT_FILTER));

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Needed for Geolocation
        TendartsSDK.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set up SDK
        TendartsSDK.onResume(getApplicationContext());

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Needed for Geolocation
        TendartsSDK.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Needed for Geolocation
        TendartsSDK.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Needed for Geolocation
        TendartsSDK.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(logBroadcastReceiver);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Needed to pass location permissions request result to SDK
        TendartsSDK.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults,
                getApplicationContext());

    }

    private void configureView() {

        ButterKnife.bind(this);

        String environment = String.format("Server base URL: %s", ConstantsBase.baseUrl);
        environmentText.setText(environment);

        String accessToken = Configuration.getAccessToken(this);
        if (!TextUtils.isEmpty(accessToken)) {
            accessTokenText.setText(String.format("10darts SDK Access Token: %s", accessToken));
        }

        String senderId = Configuration.instance(this).getGCMDefaultSenderId(this, getApplicationInfo().packageName);
        if (!TextUtils.isEmpty(senderId)) {
            senderIdText.setText(String.format("GCM SenderId: %s", senderId));
        }

        String model = Build.MANUFACTURER+" | "+Build.MODEL;
        modelText.setText(String.format("Model: %s", model));

        String androidVersion = currentAndroidVersion();
        androidVersionText.setText(String.format("Android version: %s", androidVersion));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        logRecyclerView.setLayoutManager(linearLayoutManager);

        logAdapter = new LogAdapter(logEntityList, new LogAdapter.LogAdapterInterface() {
            @Override
            public void onLongClick() {
                clearLog();
            }
        });

        logRecyclerView.setAdapter(logAdapter);

        logRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //check for scroll down

                shouldScrollToTop = dy <= 0;

            }
        });

        linkDeviceWithUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askForUserName();
            }
        });

    }

    private void updateLog(LogEntity logEntity) {

        if (logEntity != null) {

            logEntityList.add(0, logEntity);

            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {

                    logAdapter.notifyDataSetChanged();

                    if (shouldScrollToTop) {
                        logRecyclerView.scrollToPosition(0);
                    }
                }
            });
        }

    }

    private void clearLog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Clear log?");
        builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logEntityList.clear();
                logAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    private void askForUserName() {

        SetUserDialogFragment setUserDialogFragment = SetUserDialogFragment.newInstance(userName);
        setUserDialogFragment.setListener(new SetUserDialogFragment.SetUserDialogListener() {
            @Override
            public void onUserNameSaved(String userName) {

                if (!TextUtils.isEmpty(userName)) {
                    linkDeviceWithUserIdentifier(userName);
                }

            }
        });
        setUserDialogFragment.show(getSupportFragmentManager(), "set_user_name");

    }

    /**
     * Associates the device with your own user
     *
     * As the doc says:
     * 'From version 1.22, if this function fails, it will be automatically retried and when
     * succesfull your client class’s onUserLinkedToDevice function will be called'
     */
    private void linkDeviceWithUserIdentifier(final String user) {

        TendartsSDK.linkDeviceWithUserIdentifier(new TendartsSDK.IResponseObserver() {
            @Override
            public void onOk() {
                // Device linked, save it to not re-link again
                MainActivity.this.userName = user;

                showMessage("Device linked with user " + user);
            }

            @Override
            public void onFail(String errorString) {
                // Something failed, try again later, more info on errorString
                showMessage("Failed to link device with user " + user);
            }
        },this, user);

    }

    private void showMessage(String message) {

        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }

    /**
     * Optionally, you can add user data,
     * to do so, call modifyUser providing the fields you want to add or modify,
     * if any parameter is null it will remain untouched.
     */
    private void addUserData() {

        TendartsSDK.modifyUser("email", "first name", "last name", "password", new TendartsSDK.IResponseObserver() {
            @Override
            public void onOk() {
                // succeeded
            }

            @Override
            public void onFail(String errorString) {
                // failed
            }
        }, this);

    }

    // Current Android version data
    public static String currentAndroidVersion() {
        double release = Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName = "Unsupported";//below Jelly bean OR above Oreo
        if(release >= 4.1 && release < 4.4) {
            codeName="Jelly Bean";
        } else if (release<5) {
            codeName="Kit Kat";
        } else if (release<6) {
            codeName="Lollipop";
        } else if (release<7) {
            codeName="Marshmallow";
        } else if (release<8) {
            codeName="Nougat";
        } else if (release<9) {
            codeName="Oreo";
        }
        return codeName+" v"+release+", API Level: "+Build.VERSION.SDK_INT;
    }

}
