package com.muslimlife.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static SimpleDateFormat currentDateShort;
    private static SimpleDateFormat currentDate;

    public static void initFormat() {
        currentDateShort = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        currentDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
    }

    public static String shortResFormatTimeOnly(Date date) {
        return currentDateShort.format(date);
    }

    public static String getDateForNotification(Date date) {
        return currentDate.format(date);
    }

}
