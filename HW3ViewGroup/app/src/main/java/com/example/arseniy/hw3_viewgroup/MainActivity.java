package com.example.arseniy.hw3_viewgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {
    public MyViewGroup mMyViewGroup1;
    MyViewGroup mMyViewGroup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyViewGroup1 = findViewById(R.id.myViewGroup1);
        setListeners(mMyViewGroup1);
        mMyViewGroup2 = findViewById(R.id.myViewGroup2);
        setListeners(mMyViewGroup2);
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


