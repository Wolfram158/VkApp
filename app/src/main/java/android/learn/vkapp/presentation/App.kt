package android.learn.vkapp.presentation

import android.app.Application
import android.learn.vkapp.di.ApplicationComponent
import android.learn.vkapp.di.DaggerApplicationComponent

class App : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder().build()
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}