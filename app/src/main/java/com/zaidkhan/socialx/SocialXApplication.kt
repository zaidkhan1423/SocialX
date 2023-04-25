package com.zaidkhan.socialx

import android.app.Application
import com.zaidkhan.socialx.di.AppComponent
import com.zaidkhan.socialx.di.DaggerAppComponent

class SocialXApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}