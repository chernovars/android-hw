package com.example.arseniy.hw1_corecomponents;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    String LOG_TAG = "second activity";
    static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 98;
    TextView mTextView;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            Log.d("receiver", "got message: " + result);
            mTextView.setText("Calendar data: " + result);
            final Intent retIntent = new Intent();
            retIntent.putExtra("return", result);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    finishActivity(retIntent);
                }
            }, 2000);

        }
    };

    private void finishActivity(Intent intent){
        Log.d(LOG_TAG, "finish activity");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "on create");
        setContentView(R.layout.activity_second);
        mTextView = findViewById(R.id.textViewResult);
        Log.d(LOG_TAG, "get permissions for calendar read");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-service-handle"));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (getPermissions()) {
                    // eсли разрешение на чтения календаря уже есть, запустить сервис
                    // в противном случае сделается запрос пользователю, и сервис запустится в onRequestPermissionsResult
                    startCalendarService();
                }
            }
        }, 2000);

    }

    private void startCalendarService(){
        Intent startIntent = new Intent(this, MyService.class);
        this.startService(startIntent);
    }

    private boolean getPermissions() {
        Log.d(LOG_TAG, "get permissions");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "not granted");

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CALENDAR);
            }

            return false;
        }
        Log.d(LOG_TAG, "granted");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "user granted permission");
                    startCalendarService();
                } else {
                    Log.d(LOG_TAG, "user blocked permission");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
