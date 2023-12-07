package com.muslimlife.app.view.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    String TAG = getClass().getSimpleName();

    public abstract int getLayoutResource();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

    }
}
