package com.rakuten.weatherstack.usecase

import com.rakuten.weatherstack.model.Weather
import com.rakuten.weatherstack.model.WeatherDao
import io.reactivex.Completable

class CommandSaveWeatherInfo(private val weatherDao: WeatherDao) {
    operator fun invoke(weather: Weather) : Completable = Completable.fromAction { weatherDao.insert(weather) }
}
