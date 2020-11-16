package com.rakuten.weatherstack.usecase

import com.rakuten.weatherstack.source.network.NetworkResult
import com.rakuten.weatherstack.source.network.RemoteDataSource
import io.reactivex.Observable

class SearchCityWeather(private val remoteDataSource: RemoteDataSource) {
    operator fun invoke(cityName: String) : Observable<NetworkResult> = remoteDataSource.searchResultsObservable(cityName)
}
