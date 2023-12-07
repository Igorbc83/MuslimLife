package com.muslimlife.app.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import java.util.Locale;

public class MyContextWrapper extends ContextWrapper {

    public MyContextWrapper(Context base) {
        super(base);
    }

    @SuppressWarnings("deprecation")
    public static ContextWrapper wrap(Context context, String language) {
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale = null;

        sysLocale = getSystemLocaleLegacy(config);

        if (!language.equals("") && !sysLocale.getLanguage().equals(language)) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            setSystemLocaleLegacy(config, locale);

        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        return new MyContextWrapper(context);
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale){
        config.locale = locale;
    }
}
