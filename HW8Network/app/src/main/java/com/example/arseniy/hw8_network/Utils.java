package com.example.arseniy.hw8_network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AlertDialog;

import com.example.arseniy.hw8_network.retrofit.MsDate;

import org.reactivestreams.Publisher;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Utils {
    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){
        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }
    };

    private static SimpleDateFormat dateFormat= new SimpleDateFormat("dd MMMM, yyyy", myDateFormatSymbols);
    private static Calendar calendar = new GregorianCalendar();

    private static CharSequence today = dateFormat.format(new Date());
    private static CharSequence yesterday = dateFormat.format(getPreviousDays(2, 1).get(1));

    private static CharSequence todayText = "Сегодня";
    private static CharSequence yesterdayText = "Вчера";

    private static ConnectivityManager connectivityManager;


    static CharSequence customFormatDate(Date date)   {
       CharSequence res = dateFormat.format(date);
       if (res.equals(today))
           return todayText;
       else if (res.equals(yesterday))
           return yesterdayText;
       return res;
    }

    public static Date fromMillisToDate(long millis) {
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    private static ArrayList<Date> getPreviousDays(int days, int repeats) {
        Date startDate = new Date();

        ArrayList<Date> datesInRange = new ArrayList<>();
        calendar.setTime(startDate);
        Random random = new Random();

        for (int i=0; i < days; i++) {
            Date result = calendar.getTime();
            for (int j=0; j < repeats; j++)
                datesInRange.add(result);
            calendar.add(Calendar.HOUR, -(random.nextInt(8) + 8));
        }
        return datesInRange;
    }

    public static int compareMsDates(MsDate o1, MsDate o2) {
        long diff = o2.getMilliseconds() - o1.getMilliseconds();
        diff = diff / 60000;
        return Math.toIntExact(diff);
    }

    static boolean isConnected(Context context) {
        if (connectivityManager == null)
            connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    static void showWarningDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.no_internet))
                .setMessage(activity.getString(R.string.no_internet_warning_message))
                .show();
    }

    static Observable runInBackgroundObserveOnUi(Observable o) {
        return o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}


