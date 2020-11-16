package com.rakuten.weatherstack.usecase

import com.rakuten.weatherstack.model.Weather
import com.rakuten.weatherstack.model.WeatherDao
import io.reactivex.Observable

class QuerySavedWeatherData(private val weatherDao: WeatherDao) {

    operator fun invoke() : Observable<List<Weather>> = weatherDao.getAll
}
