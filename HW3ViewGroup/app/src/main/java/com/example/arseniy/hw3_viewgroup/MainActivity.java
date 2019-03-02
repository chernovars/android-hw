package com.example.arseniy.hw3_viewgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {
    MyViewGroup mMyViewGroup1;
    MyViewGroup mMyViewGroup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyViewGroup1 = findViewById(R.id.myViewGroup1);
        setListeners(mMyViewGroup1);
        mMyViewGroup2 = findViewById(R.id.myViewGroup2);
        setListeners(mMyViewGroup2);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMyViewGroup1.opposeGravity();
                mMyViewGroup1.requestLayout();
                mMyViewGroup2.opposeGravity();
                mMyViewGroup2.requestLayout();
            }
        });
    }

    void setListeners(MyViewGroup viewGroup) {
        for(int index=0; index < viewGroup.getChildCount(); ++index) {
            ((Chip) viewGroup.getChildAt(index)).
                    setOnCloseIconClickListener(new MyOnClickListener());
        }
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.removeView(v);
            if (parent.equals(mMyViewGroup1))
                mMyViewGroup2.addView(v);
            else
                mMyViewGroup1.addView(v);
        }
    }
}


