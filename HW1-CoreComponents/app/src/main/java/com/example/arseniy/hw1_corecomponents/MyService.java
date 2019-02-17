package com.example.arseniy.hw1_corecomponents;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

public class MyService extends IntentService {
    static final String LOG_TAG = "my service";
    //Исправление 3.2: Название ключа в статической константе
    static final String BROADCAST_RETURN_KEY = "result";
    static final int MAX_BROADCAST_LIST_SIZE = 5;

    // Исправление 5: Убраны лишние строки проекции
    // Исправление 7: Поменял таблицу с Calendars на Events, чтобы работать с событиями
    public static final String[] EVENT_PROJECTION = new String[] {
            Events.TITLE
    };

    public MyService() {
        super("my_service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "on handle intent");

        final ArrayList<String> result = getCalendars();
        sendLocalBroadCast(result);
    }

    private void sendLocalBroadCast(ArrayList<String> result) {
        Intent retIntent = new Intent("my-service-handle");
        retIntent.putExtra(BROADCAST_RETURN_KEY, result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(retIntent);
    }

    private ArrayList<String> getCalendars() {
        // получить названия календарей, используя provider

        Log.d(LOG_TAG, "get calendars");
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri events_uri = Events.CONTENT_URI;
        cur = cr.query(events_uri, EVENT_PROJECTION, null, null, null);

        // Исправление 6: Результат выкачивания данных курсора сохраняется в список, который возвращается в итоге в MainActivity
        ArrayList<String> result = new ArrayList();

        if (cur != null) {
            while (cur.moveToNext() && result.size() < MAX_BROADCAST_LIST_SIZE) { // нет смысла выкачивать все события в данном примере
                // Исправление 4: Индекс колонки выводится через метод cur.getColumnIndex
                // Исправление 5: Объединил строки с объявлением и определением title, а также поменял название переменной с displayName на title
                String title = cur.getString(cur.getColumnIndex(Events.TITLE));
                result.add(title);
            }
            cur.close();
        }
        if (result.size() == 0) {
            result.add("No events found");
        }
        else if (result.size() == MAX_BROADCAST_LIST_SIZE) {
            result.add("...");
        }

        return result;
    }
}
