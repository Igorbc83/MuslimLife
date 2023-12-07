package com.muslimlife.app.app;

import android.app.Activity;
import android.app.Application;

import com.google.android.libraries.places.api.Places;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import ru.rustore.sdk.billingclient.RuStoreBillingClient;
import ru.rustore.sdk.billingclient.RuStoreBillingClientFactory;

import com.muslimlife.app.R;
import com.muslimlife.app.di.DaggerAppComponent;
import com.muslimlife.app.utils.DateUtil;

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> injector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().create(this).inject(this);
        DateUtil.initFormat();

        String apiKey = "";

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        RuStoreBillingClient billingClient = RuStoreBillingClientFactory.INSTANCE.create(
                this,//идентификатор приложения;
                "", //код приложения из системы RuStore Консоль;
                "rustoremuslimlifescheme://iamback" //URL для использования deeplink.
        );
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return injector;
    }
}
