package com.example.arseniy.hw1_corecomponents;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String LOG_TAG = "first activity";
    static final int RESULT_REQUEST_CODE = 908;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "on create");

        // выдержать время, чтобы пользователь успел взглянуть на текущую Activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                goToNextActivity();
            }
        }, 2000);
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        Log.d(LOG_TAG, "starting second activity");
        this.startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "on activity result");
        if (requestCode == RESULT_REQUEST_CODE) {
            Log.d(LOG_TAG, "result from second activity");
            Toast toast = Toast.makeText(this, data.getStringExtra("return"), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
