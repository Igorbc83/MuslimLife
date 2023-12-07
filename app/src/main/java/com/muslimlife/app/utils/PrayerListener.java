package com.muslimlife.app.utils;

import com.muslimlife.app.data.model.PushTime;

public interface PrayerListener {

    void Timer(String toPrayer,String toPrayerNamaz);

    void Name(int name);

    void Time(String time);

    void pushTime(PushTime pushTime);
}
