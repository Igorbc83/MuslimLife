package com.muslimlife.app.di;

import android.content.Context;

import com.muslimlife.app.app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    @Singleton
    @Provides
    public Context provideContext(App app) {
        return app.getApplicationContext();
    }
}
