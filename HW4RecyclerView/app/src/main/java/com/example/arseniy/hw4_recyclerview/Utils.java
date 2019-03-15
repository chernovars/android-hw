package com.example.arseniy.hw4_recyclerview;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class Utils {
    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){
        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }
    };

    static SimpleDateFormat dateFormat= new SimpleDateFormat("dd MMMM, yyyy", myDateFormatSymbols);
    static Calendar calendar = new GregorianCalendar();

    static CharSequence today = dateFormat.format(new Date());
    static CharSequence yesterday = dateFormat.format(getPreviousDays(2, 1).get(1));

    static CharSequence todayText = "Сегодня";
    static CharSequence yesterdayText = "Вчера";


    static CharSequence customFormatDate(Date date)   {
       CharSequence res = dateFormat.format(date);
       if (res.equals(today))
           return todayText;
       else if (res.equals(yesterday))
           return yesterdayText;
       return res;
    }

    static ArrayList<Date> getPreviousDays(int days, int repeats) {
        Date startDate = new Date();

        ArrayList<Date> datesInRange = new ArrayList<>();
        calendar.setTime(startDate);

        for (int i=0; i < days; i++) {
            Date result = calendar.getTime();
            for (int j=0; j < repeats; j++)
                datesInRange.add(result);
            calendar.add(Calendar.DATE, -1);
        }
        return datesInRange;
    }

    static ArrayList<CharSequence> generateMockData(int mockNewsCount) {
        ArrayList<CharSequence> mockData = new ArrayList<>();
        for (int i = 0; i < mockNewsCount; i++) {
            mockData.add("Mock " + Integer.toString(i));
        }
        return mockData;
    }

    static ArrayList<Date> generateMockDates(int mockNewsCount) {
        return getPreviousDays(mockNewsCount/2, 2);
    }

    static Date parseDateCharSequence(CharSequence dateCharSequence) {
        Date date;
        try {
            date = dateFormat.parse(dateCharSequence.toString());
        }
        catch (java.text.ParseException e) {
            Log.d("HW4", "PARSE ERROR " + dateCharSequence.toString());
            date = new Date();
        }
        return date;
    }
}


