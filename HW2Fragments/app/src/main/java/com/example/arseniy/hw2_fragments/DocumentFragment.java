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
    static final private String EXTRA_KEY = "ordinal";

    // Исправление 5.2 Статический метод для передачи аргументов фрагменту
    public static DocumentFragment newInstance(int ordinal) {
        Bundle args = new Bundle();
        args.putInt(DocumentFragment.EXTRA_KEY, ordinal + 1);

        DocumentFragment fragment = new DocumentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = (TextView) inflater.inflate(R.layout.fragment_1, container, false);

        // Исправление 4: Перенес строку "Документ №" из переменной в ресурсы
        int ord  = getArguments().getInt(EXTRA_KEY);
        textView.setText(getString(R.string.fragment_base_text, ord));
        return textView;
    }
}
