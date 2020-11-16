package com.rakuten.weatherstack.util

import android.content.res.Resources
import com.rakuten.weatherstack.R

class WeatherResourcesImpl(private val resources: Resources) : WeatherResources {

    override fun getWeatherUrl(): String = resources.getString(R.string.weather_base_url)

    override fun getAccessKey(): String = resources.getString(R.string.weather_api_key)
}
