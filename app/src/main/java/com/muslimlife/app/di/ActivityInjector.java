package com.muslimlife.app.di;

import com.muslimlife.app.view.MainActivity;

import all.muslimlife.view.webpay.WebPayActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
interface ActivityInjector {

    @ContributesAndroidInjector(modules = {MainFragmentInjector.class, NetworkModule.class})
    MainActivity provideMainActivity();

    @ContributesAndroidInjector(modules = {MainFragmentInjector.class, NetworkModule.class})
    WebPayActivity provideWebPayActivity();
}
