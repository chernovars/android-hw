package com.example.arseniy.hw2_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DocumentFragment extends Fragment {
    static final String BASE_TEXT = "Документ №";
    static final String EXTRA_KEY = "ordinal";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = (TextView) inflater.inflate(R.layout.fragment_1, container, false);
        textView.setText(BASE_TEXT + Integer.toString(getArguments().getInt(EXTRA_KEY)));
        return textView;
    }
}
