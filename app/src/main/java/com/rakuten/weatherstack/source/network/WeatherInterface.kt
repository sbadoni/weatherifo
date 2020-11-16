package com.rakuten.weatherstack.source.network

import com.rakuten.weatherstack.model.WeatherResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("current")
    fun searchCurrentCityWeather(@Query("access_key") access_key: String,
                                 @Query("query") cityName: String): Observable<Response<WeatherResponse>>
}
