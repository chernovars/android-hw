package com.example.arseniy.hw8_network;

import android.content.Context;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

class Utils {
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


    static CharSequence customFormatDate(Date date)   {
       CharSequence res = dateFormat.format(date);
       if (res.equals(today))
           return todayText;
       else if (res.equals(yesterday))
           return yesterdayText;
       return res;
    }

    static Date fromMillisToDate(long millis) {
        calendar.setTimeInMillis(millis);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

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

    private static ArrayList<CharSequence> generateMockTitles(int mockNewsCount) {
        ArrayList<CharSequence> mockData = new ArrayList<>();
        for (int i = 0; i < mockNewsCount; i++) {
            mockData.add("Mock " + Integer.toString(i));
        }
        return mockData;
    }

    private static ArrayList<Date> generateMockDates(int mockNewsCount) {
        return getPreviousDays(mockNewsCount, 1);
    }

    static ArrayList<News> generateNews(int mockNewsCount, Context context) {
        ArrayList<News> news = new ArrayList<>();
        ArrayList<Date> dates = generateMockDates(mockNewsCount);
        ArrayList<CharSequence> titles = generateMockTitles(mockNewsCount);

        News n;
        for (int i = 0; i < titles.size(); i++) {
            n = new News();
            n.title = titles.get(i).toString();
            n.date = dates.get(i);
            n.shortDesc = context.getString(R.string.mock_news_short_desc);
            n.fullDesc = context.getString(R.string.mock_news_full_desc);
            n.id = i;
            news.add(n);
        }
        return news;
    }
}


