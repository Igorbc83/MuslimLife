package com.muslimlife.app.di;

import com.muslimlife.app.app.App;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

import com.muslimlife.app.di.viewModel.ViewModelModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityInjector.class,
        ViewModelModule.class,
        RepositoryModule.class,
        NetworkModule.class,
        RuStoreModule.class})
interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {

    }
}
