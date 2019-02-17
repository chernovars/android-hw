package com.example.arseniy.hw1_corecomponents;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final String LOG_TAG = "first activity";
    static final int RESULT_REQUEST_CODE = 908;

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            goToNextActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "on create");

        // Исправление 1.1: Переход к следующему окну привязал на кнопку вместо выжидания двух секунд
        final Button button = findViewById(R.id.button_to_second_activity);
        button.setOnClickListener(buttonListener);
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
            Toast toast = Toast.makeText(this,
                    data.getStringArrayListExtra(SecondActivity.RETURN_KEY).toString(),
                    Toast.LENGTH_LONG
            );
            toast.show();
        }
    }
}
