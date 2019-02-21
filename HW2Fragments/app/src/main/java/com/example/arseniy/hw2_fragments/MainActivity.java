package com.example.arseniy.hw2_fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    static final String EXTRA_FRAGMENTS_CREATED = "fragments_created";

    int fragmentsCreated = 0;

    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private MenuItem mDeleteMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fragmentsCreated = savedInstanceState.getInt(EXTRA_FRAGMENTS_CREATED);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mDeleteMenuItem = menu.findItem(R.id.action_delete);
        mDeleteMenuItem.setEnabled(fragmentsCreated != 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                onMenuAddClicked();
                return true;
            case R.id.action_delete:
                onMenuDeleteClicked();
                return true;
        }

        return false;
    }

    private void onMenuAddClicked() {
        Bundle args = new Bundle();
        fragmentsCreated += 1;

        if (fragmentsCreated == 1) {
            mDeleteMenuItem.setEnabled(true);
        }

        args.putInt(DocumentFragment.EXTRA_KEY, fragmentsCreated);
        Fragment fragment =  new DocumentFragment();
        fragment.setArguments(args);

        mFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void onMenuDeleteClicked() {
        mFragmentManager.popBackStack();
        fragmentsCreated -=1;
        if (fragmentsCreated == 0) mDeleteMenuItem.setEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_FRAGMENTS_CREATED, fragmentsCreated);
        super.onSaveInstanceState(outState);
    }
}
