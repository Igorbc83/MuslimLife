package com.muslimlife.app.utils;

import android.content.Context;

import com.muslimlife.app.data.model.PushTime;

import net.alhazmy13.PrayerTimes.PrayerTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.muslimlife.app.R;

public class PrayerTimeUtil {

    public int[] names={R.string.Fajr,R.string.Shuruk,R.string.Zuhr,R.string.Asr,R.string.Magrib,R.string.Isha};
    public ArrayList<String> prayerTimes;
    int Min,Hour;
    int deltaHour;
    int deltaMinute;
    String timerText;
    String timerTextNamaz;
    Context context;

    boolean isStarted=false;
    Timer timer;
    PrayerListener prayerListener;

    double lat;
    double lon;

    PrayerTime prayers;
    Date Prayer,Now;
    boolean flag = true;

    public PrayerTimeUtil(double lat, double lon, PrayerListener prayerListener, Context context){
        this.lat=lat;
        this.lon=lon;
        this.context=context;
        this.prayerListener=prayerListener;

        init();
    }

    private void init(){
        //TODO: Получить координаты города с бэка
        prayers = new PrayerTime();
        prayers.setTimeFormat(PrayerTime.TimeFormat.Time24);

        prayers.setCalcMethod(PrayerTime.Calculation.Karachi);

        prayers.setAsrJuristic(PrayerTime.Juristic.Shafii);
        prayers.setAdjustHighLats(PrayerTime.Adjusting.AngleBased);
        prayers.setOffsets(new int[]{0, 0, 0, 0, 0, 0, 0});

        double zo = Double.parseDouble(timeZone());

        prayerTimes = prayers.getPrayerTimes(Calendar.getInstance(),
                lat, 	lon,  zo);

        prayerTimes.remove(5);

        prayerListener.pushTime(new PushTime(prayerTimes, timeZone()));
        Timer();
    }

    public static String timeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return timeZone.substring(0, 3) + "."+ timeZone.substring(3, 5);
    }

    public void namazTime(){
        getCurrentPrayer();
    }

    private void Timer(){
        if(!isStarted){
            timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    getCurrentPrayer();
                    Delta(Now, Prayer);

                    Min = deltaMinute;
                    Hour = deltaHour;

                  //  timerText = Hour + " " + Min;
                    timerTextNamaz = Hour + " " + Min;

                    if(flag) {
                        if (Hour < 10 && Min < 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = "0" + Hour + ":0" + Min;
                        } else if (Hour > 10 && Min < 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = Hour + ":0" + Min;
                        } else if (Hour < 10 && Min > 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = "0" + Hour + ":" + Min;
                        } else if (Hour > 10 && Min > 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = Hour + ":" + Min;
                        }
                        flag = false;
                    }else {
                        if (Hour < 10 && Min <= 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = "0" + Hour + " 0" + Min;
                        } else if (Hour > 10 && Min < 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = Hour + " 0" + Min;
                        } else if (Hour < 10 && Min > 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = "0" + Hour + " " + Min;
                        } else if (Hour > 10 && Min > 10) {
                            timerText = Hour + getTime(Hour) + " " + Min;
                            timerTextNamaz = Hour + " " + Min;
                        }
                        flag=true;
                    }

                    if(prayerListener!=null)
                        prayerListener.Timer(timerText, timerTextNamaz);
                }
            };
            timer.schedule(timerTask,0,1000);
        }

    }

    private String getTime(int hour){
        if (hour == 2 || hour == 3 || hour == 4)
            return " " + context.getString(R.string.hour1);

        else if (hour ==1)
            return " " + context.getString(R.string.hour);

        else return " " + context.getString(R.string.hour2);
    }

    public int getCurrentPrayer(){
        int next=0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
        Prayer=new Date(); Now=new Date();
        for(int position=0;position<names.length;position++){
            try {
                Date iterationNow = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
                Date parsePrayer = dateFormat.parse(prayerTimes.get(position));
                if (parsePrayer != null && parsePrayer.compareTo(iterationNow) < 0) {
                    next = position + 1;

                    if (next >= prayerTimes.size())
                        next = prayerTimes.size();

                    if(next==6)
                        next=0;

                    Prayer = dateFormat.parse(prayerTimes.get(next));
                    Now = iterationNow;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        prayerListener.Name(names[next]);
        Delta(Now,Prayer);
        prayerListener.Time(prayerTimes.get(next));
        return next;
    }

    private void Delta(Date now, Date prayerNext){
        prayerNext.setYear(now.getYear());
        prayerNext.setMonth(now.getMonth());
        prayerNext.setDate(now.getDate());
        if(now.getTime()>prayerNext.getTime()){
            now = new Date(System.currentTimeMillis() - 1000L * 60L * 60L * 24L);
            prayerNext.setYear(now.getYear());
            prayerNext.setMonth(now.getMonth());
            prayerNext.setDate(now.getDate()+1);

            long diffInMillies = Math.abs(prayerNext.getTime() - now.getTime());
            long hours = (long) Math.ceil(diffInMillies/3600000);
            long minutes = (long) Math.floor(diffInMillies/60000);

            deltaHour = (int) hours;
            deltaMinute = (int) (minutes-hours*60);
        }else if(now.getTime()<=prayerNext.getTime()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
            String nowStr=dateFormat.format(now);
            String prayerStr=dateFormat.format(prayerNext);

            deltaHour= (Integer.parseInt(prayerStr.substring(0,2))-Integer.parseInt(nowStr.substring(0,2)))*60;
            deltaMinute = Integer.parseInt(prayerStr.substring(3,5))-Integer.parseInt(nowStr.substring(3,5))+deltaHour;

            deltaHour = deltaMinute/60;
            deltaMinute %=60;
        }
    }

    public ArrayList<String> getTimesOnDate(Calendar calendar){
        ArrayList<String> times = prayers.getPrayerTimes(calendar,
                lat, 	lon,  Double.parseDouble(timeZone()));

        times.remove(5);

        return times;
    }

    public void Destroy(){
        isStarted=false;
        timer.cancel();
    }

}
