package com.muslimlife.app.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.muslimlife.app.data.model.KoranTestRes;
import com.muslimlife.app.data.model.azkars.AzkarsRes;
import com.muslimlife.app.data.model.surah.SurahRes;

public class CacheHelper {

    public static final String KEY_CACHE_QURAN_PAGES = "muslimLifeQuranPagesCache";
    public static final String KEY_CACHE_SURAHS = "muslimLifeSurahsCache";

    public static final String KEY_CACHE_AZKARS = "muslimLifeAzkarsCache";
    public static final String CACHE_VERSION = "7";

    static int cacheLifeHour = 7 * 24;

    public static String getCacheDirectory(Context context){

        return context.getCacheDir().getPath();
    }

    public static void saveQuranPages(Context context, ArrayList<KoranTestRes> value){
        try {
            Log.d("LOAD_QURAN", "saveQuranPages local start");

            String key = URLEncoder.encode(KEY_CACHE_QURAN_PAGES + "_" + CACHE_VERSION, "UTF-8");

            File cache = new File(getCacheDirectory(context) + "/" + key + ".srl");

            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(cache));
            out.writeObject(value);
            out.close();

            Log.d("LOAD_QURAN", "saveQuranPages local end");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LOAD_QURAN", "saveQuranPages error: " + e.getMessage());
        }
    }

    public static void saveSurahs(Context context, ArrayList<SurahRes> value) {
        try {
            Log.d("LOAD_QURAN", "saveSurahs local start");

            String key = URLEncoder.encode(KEY_CACHE_SURAHS + "_" + CACHE_VERSION, "UTF-8");

            File cache = new File(getCacheDirectory(context) + "/" + key + ".srl");

            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(cache));
            out.writeObject(value);
            out.close();

            Log.d("LOAD_QURAN", "saveSurahs local end");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LOAD_QURAN", "saveSurahs error: " + e.getMessage());
        }
    }

    public static void saveAzkars(Context context, AzkarsRes value) {
        try {
            Log.d("LOAD_AZKARS", "saveAzkars local start");

            String key = URLEncoder.encode(KEY_CACHE_AZKARS, "UTF-8");

            File cache = new File(getCacheDirectory(context) + "/" + key + ".srl");

            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(cache));
            out.writeObject(value);
            out.close();

            Log.d("LOAD_AZKARS", "saveAzkars local end");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LOAD_AZKARS", "saveSsaveAzkarsurahs error: " + e.getMessage());
        }
    }

    public static ArrayList<SurahRes> loadSurah(Context context){
        try{
            return (ArrayList) load(context, KEY_CACHE_SURAHS + "_" + CACHE_VERSION);
        }catch (Exception ignored){
            return null;
        }
    }

    public static ArrayList<KoranTestRes> loadQuranPages(Context context){
        try{
            return (ArrayList) load(context, KEY_CACHE_QURAN_PAGES + "_" + CACHE_VERSION);
        }catch (Exception ignored){
            return null;
        }
    }

    public static AzkarsRes loadAzkars(Context context){
        try{
            return (AzkarsRes) load(context, KEY_CACHE_AZKARS);
        }catch (Exception ignored){
            return null;
        }
    }

    public static Object load(Context context, String key) {

        try {

            key = URLEncoder.encode(key, "UTF-8");

            File cache = new File(getCacheDirectory(context) + "/" + key + ".srl");

            if (cache.exists()) {

                /*Date lastModDate = new Date(cache.lastModified());
                Date now = new Date();

                long diffInMillisec = now.getTime() - lastModDate.getTime();
                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);

                diffInSec /= 60;
                diffInSec /= 60;
                long hours = diffInSec % 24;

                if (hours > cacheLifeHour) {
                    cache.delete();
                    return new ArrayList<>();
                }*/

                ObjectInputStream in = new ObjectInputStream(new FileInputStream(cache));
                return in.readObject();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}
