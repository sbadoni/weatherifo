package com.rakuten.weatherstack

import android.app.Application
import com.rakuten.weatherstack.di.WeatherAppModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            modules(WeatherAppModule)
        }
    }
}
