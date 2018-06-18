package com.wallet.crypto

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDexApplication

import com.wallet.crypto.di.AppInjector

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import kotlin.properties.Delegates

class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        context = this
        Realm.init(this)
        AppInjector.init(this)

    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    companion object {

        var context: App by Delegates.notNull()
            private set
    }

}
