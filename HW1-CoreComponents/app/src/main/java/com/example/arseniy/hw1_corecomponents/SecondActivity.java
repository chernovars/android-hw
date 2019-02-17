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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    static final String LOG_TAG = "second activity";
    //Исправление 3.1: Название ключа в статической константе
    static final String RETURN_KEY = "return";
    static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 98;
    TextView mTextView;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> result = intent.getStringArrayListExtra(MyService.BROADCAST_RETURN_KEY);
            Log.d("receiver", "got message: " + result);
            mTextView.setText("Calendar data: " + result);
            final Intent retIntent = new Intent();
            retIntent.putExtra(RETURN_KEY, result);

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
        // Исправление 2: Убрал лишние логи (здесь удалена строка, в других местах не помечено)
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-service-handle"));

        // Исправление 1.2: Запуск сервиса привязал на кнопку вместо выжидания двух секунд
        final Button button = findViewById(R.id.button_to_second_activity);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getPermissions()) {
                    // eсли разрешение на чтения календаря уже есть, запустить сервис
                    // в противном случае сделается запрос пользователю, и сервис запустится в onRequestPermissionsResult
                    startCalendarService();
                }
            }
        });

    }

    private void startCalendarService(){
        Intent startIntent = new Intent(this, MyService.class);
        this.startService(startIntent);
    }

    private boolean getPermissions() {
        Log.d(LOG_TAG, "get permissions");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CALENDAR);
            }

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCalendarService();
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
