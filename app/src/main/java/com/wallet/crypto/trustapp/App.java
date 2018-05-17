package com.wallet.crypto.trustapp;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.wallet.crypto.trustapp.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.realm.Realm;

public class App extends MultiDexApplication implements HasActivityInjector {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        Realm.init(this);
        AppInjector.init(this);

    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
