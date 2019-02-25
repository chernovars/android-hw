package com.example.arseniy.hw2_fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final String EXTRA_FRAGMENTS_CREATED = "fragments_created";
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private MenuItem mDeleteMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Исправление 3.1 Убрал восстановление значения счетчика фрагментов (в связи с исправлением 2)

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mDeleteMenuItem = menu.findItem(R.id.action_delete);
        mDeleteMenuItem.setEnabled(mFragmentManager.getBackStackEntryCount() > 0);
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
        // Исправление 5.1: Вызов статического метода для передачи аргументов во фрагмент
        Fragment fragment =  DocumentFragment.newInstance(mFragmentManager.getBackStackEntryCount());

        mFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        mFragmentManager.executePendingTransactions();

        // Исправление 1.1: Упрощение if блока
        // Исправление 2.1: Метод вместо атрибута объекта для счета фрагментов в бэкстеке
        mDeleteMenuItem.setEnabled(mFragmentManager.getBackStackEntryCount() > 0);
    }

    private void onMenuDeleteClicked() {
        mFragmentManager.popBackStack();
        mFragmentManager.executePendingTransactions();

        // Исправление 1.2: Упрощение if блока
        // Исправление 2.2: Метод вместо атрибута объекта
        mDeleteMenuItem.setEnabled(mFragmentManager.getBackStackEntryCount() > 0);
    }

    // Исправление 3.2 Убрал onSaveInstanceState за ненадобностью (в связи с исправлением 2)

}
