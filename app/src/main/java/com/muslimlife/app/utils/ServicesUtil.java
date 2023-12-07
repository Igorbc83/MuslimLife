package com.muslimlife.app.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.OtherEntity;

public class ServicesUtil {

    private static final String[] servicesKeys = {
            Constants.SERVICE_NAMAZ, Constants.SERVICE_KORAN, Constants.SERVICE_COMPASS,
            Constants.SERVICE_TASBIH, Constants.SERVICE_QUESTION,
            Constants.SERVICE_SERMONS, Constants.SERVICE_ZAKYAT,
            Constants.SERVICE_WALLPAPER, Constants.SERVICE_TV, Constants.SERVICE_MIRGANTS, Constants.SERVICE_MUFTIY, Constants.SERVICE_RADIO
    };

    public static void updateScore(SharedPreferences sharedPref, String key){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key + Constants.SERVICE_MOST_USED, sharedPref.getInt(key + Constants.SERVICE_MOST_USED, 0) + 1);
        editor.apply();
    }

    public static List<OtherEntity> getMostUsedServices(SharedPreferences sharedPref) {

        List<OtherEntity> otherEntities = new ArrayList<>();

        for (String key : servicesKeys) {
            if (sharedPref.getInt(key + Constants.SERVICE_MOST_USED, 0) > 0) {
                OtherEntity service = getService(key);
                service.setUsedCount(sharedPref.getInt(key + Constants.SERVICE_MOST_USED, 0));
                otherEntities.add(service);
            }
        }

        Collections.sort(otherEntities);

        return otherEntities;
    }

    public static boolean isServiceActive(SharedPreferences sharedPref, String key){
        return sharedPref.getBoolean(key + Constants.SERVICE_ACTIVE, true);
    }

    public static void setServiceActive(SharedPreferences sharedPref, String key, boolean state){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, state);
        editor.apply();
    }

    public static List<OtherEntity> getServices() {
        List<OtherEntity> services = new ArrayList<>();

        for (String key : servicesKeys)
                services.add(getService(key));

        return services;
    }

    public static List<OtherEntity> getServicesNew() {
        List<OtherEntity> services = new ArrayList<>();

        for (String key : servicesKeys)
            services.add(getServiceNew(key));

        return services;
    }

    public static Map<String, Boolean> getServicesActiveWithKey(StringTokenizer st){
        Map<String, Boolean> servicesActive = new HashMap<>();
        if(st.countTokens() > 0) {
            for (String key : servicesKeys) {
                servicesActive.put(key, Boolean.parseBoolean(st.nextToken()));
            }
        }

        return servicesActive;
    }

    public static OtherEntity getService(String key){
        switch (key){
            case Constants.SERVICE_TASBIH:
                return new OtherEntity(R.id.tasbihFragment, R.string.tasbih, R.drawable.ic_tasbih_new, R.color.icon_tasbih);
            case Constants.SERVICE_NAMAZ:
                return new OtherEntity(R.id.PrayerFragment, R.string.namaz, R.drawable.ic_namaz, R.color.icon_namaz);
            case Constants.SERVICE_KORAN:
                return new OtherEntity(R.id.KoranFragment, R.string.koran, R.drawable.ic_koran_new_2, R.color.icon_koran);
            case Constants.SERVICE_COMPASS:
                return new OtherEntity(R.id.QibleFragment, R.string.qibla, R.drawable.ic_compass_new, R.color.icon_kompas);
            case Constants.SERVICE_ZAKYAT:
                return new OtherEntity(R.id.ZakyatFragment, R.string.zakyat, R.drawable.ic_zakyat_new, R.color.icon_zakyat);
            case Constants.SERVICE_DIASPOARS:
                return new OtherEntity(R.id.DiaspoarsFragment, R.string.diaspoars, R.drawable.ic_world, R.color.icon_world);
            case Constants.SERVICE_QUESTION:
                return new OtherEntity(R.id.QuastionFragment, R.string.send_quastion, R.drawable.ic_imam_quastion, R.color.icon_imam_quastion);
            case Constants.SERVICE_SERMONS:
                return new OtherEntity(R.id.SermonsFragment,R.string.sermons, R.drawable.ic_sermon,R.color.icon_book);
            case Constants.SERVICE_WALLPAPER:
                return new OtherEntity(R.id.WallpaperFragment, R.string.wallpaper, R.drawable.ic_wallpapers, R.color.icon_image);
            case Constants.SERVICE_TV:
                return new OtherEntity(R.id.TvFragment, R.string.tv, R.drawable.ic_tv_new, R.color.icon_tv);
            default:
                return new OtherEntity(R.id.RadioFragment, R.string.radio, R.drawable.ic_radio_new, R.color.icon_radio);
        }
    }

    public static OtherEntity getServiceNew(String key){
        switch (key){
            case Constants.SERVICE_TASBIH:
                return new OtherEntity(R.id.tasbihFragment, R.string.tasbih, R.drawable.ic_qibla_service, R.color.icon_tasbih);
            case Constants.SERVICE_NAMAZ:
                return new OtherEntity(R.id.PrayerFragment, R.string.namaz, R.drawable.ic_namaz_service, R.color.icon_namaz);
            case Constants.SERVICE_KORAN:
                return new OtherEntity(R.id.KoranFragment, R.string.koran, R.drawable.ic_koran_service, R.color.icon_koran);
            case Constants.SERVICE_COMPASS:
                return new OtherEntity(R.id.QibleFragment, R.string.qibla, R.drawable.ic_compass_service, R.color.icon_kompas);
            case Constants.SERVICE_ZAKYAT:
                return new OtherEntity(R.id.ZakyatFragment, R.string.zakyat, R.drawable.ic_zakayat_service, R.color.icon_zakyat);
            case Constants.SERVICE_DIASPOARS:
                return new OtherEntity(R.id.DiaspoarsFragment, R.string.diaspoars, R.drawable.ic_world, R.color.icon_world);
            case Constants.SERVICE_QUESTION:
                return new OtherEntity(R.id.QuastionFragment, R.string.send_quastion, R.drawable.ic_imam_question_service, R.color.icon_imam_quastion);
            case Constants.SERVICE_SERMONS:
                return new OtherEntity(R.id.SermonsFragment,R.string.sermons, R.drawable.ic_book_service,R.color.icon_book);
            case Constants.SERVICE_WALLPAPER:
                return new OtherEntity(R.id.WallpaperFragment, R.string.wallpaper, R.drawable.ic_wallpaper_service, R.color.icon_image);
            case Constants.SERVICE_TV:
                return new OtherEntity(R.id.TvFragment, R.string.tv, R.drawable.ic_tv_service, R.color.icon_tv);
            case Constants.SERVICE_MIRGANTS:
                return new OtherEntity(R.id.migrantListFragment, R.string.migrants_title, R.drawable.ic_migrants, R.color.white);
            case Constants.SERVICE_MUFTIY:
                return new OtherEntity(R.id.ImamFragment, R.string.muftiy_desc, R.drawable.ic_diaspora, R.color.white);
            default:
                return new OtherEntity(R.id.RadioFragment, R.string.radio, R.drawable.ic_radio_service, R.color.icon_radio);
        }
    }

}
