package com.rakuten.weatherstack.usecase

import com.rakuten.weatherstack.model.WeatherDao
import io.reactivex.Completable

class DeleteWeatherData(private val weatherDao: WeatherDao) {
    operator fun invoke(cityName: String): Completable =
        Completable.fromAction { weatherDao.delete(cityName) }
}
