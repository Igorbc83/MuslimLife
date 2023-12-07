package com.muslimlife.app.di;

import android.content.Context;

import com.muslimlife.app.data.network.GPlacesApi;
import com.muslimlife.app.data.repository.GPlacesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.rustore.sdk.billingclient.RuStoreBillingClient;
import ru.rustore.sdk.billingclient.RuStoreBillingClientFactory;

@Module
public class RuStoreModule {

    @Singleton
    @Provides
    public RuStoreBillingClient provideRuStoreBillingClient(Context context) {
        return RuStoreBillingClientFactory.INSTANCE.create(
                context,//идентификатор приложения;
                "1442148287", //код приложения из системы RuStore Консоль;
                "rustoremuslimlifescheme://iamback" //URL для использования deeplink.
        );
    }
}
