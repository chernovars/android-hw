package com.example.arseniy.hw1_corecomponents;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends IntentService {
    String LOG_TAG = "my service";

    public static final String[] EVENT_PROJECTION = new String[] {
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;

    public MyService() {
        super("my_service");
        Log.d(LOG_TAG, "constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "on handle intent");

        final String result = getCalendars();
        sendLocalBroadCast(result);
    }

    private void sendLocalBroadCast(String result) {
        Intent retIntent = new Intent("my-service-handle");
        Log.d(LOG_TAG, "sending broadcast:" + result);
        retIntent.putExtra("result", result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(retIntent);
    }

    private String getCalendars() {
        // получить названия календарей, используя provider

        Log.d(LOG_TAG, "get calendars");
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri calendar_uri = Calendars.CONTENT_URI;
        cur = cr.query(calendar_uri, EVENT_PROJECTION, null, null, null);

        String result = "No calendars found";

        if (cur != null) {
            while (cur.moveToNext()) {
                String displayName = null;
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                Log.d(LOG_TAG, "display name: " + displayName);
                result = displayName;
            }
            cur.close();
        }

        Log.d(LOG_TAG, "result: " + result);
        return result;
    }
}
