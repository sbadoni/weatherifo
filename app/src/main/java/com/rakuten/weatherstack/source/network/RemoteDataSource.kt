package com.rakuten.weatherstack.source.network

import com.rakuten.weatherstack.model.Weather
import com.rakuten.weatherstack.model.WeatherResponse
import com.rakuten.weatherstack.util.WeatherResources
import io.reactivex.Observable
import retrofit2.Response

class RemoteDataSource(private val weatherInterface: WeatherInterface,
                       private val weatherResources: WeatherResources) {

    fun searchResultsObservable(query: String): Observable<NetworkResult> {
        println("sameer $query")
        return weatherInterface.searchCurrentCityWeather(weatherResources.getAccessKey(), query)
            .map(::mapWeatherResponse)
    }

    private fun mapWeatherResponse(response: Response<WeatherResponse>): NetworkResult {
        return when (response.code()) {
            in 200..300 -> {
                val body = response.body()
                if (body != null) {
                    body.success?.let {
                        println("sameer ${ body.success}")
                        NetworkResult.Failure(NetworkError.OtherFailure(body.error?.info ?: "No info available"))
                    } ?: kotlin.run {
                        NetworkResult.Success(body.toWeatherModel())
                    }
                }
                else {
                    NetworkResult.Failure(NetworkError.ServerFailure)
                }
            }
            in 400..500 -> {
                NetworkResult.Failure(NetworkError.CityNotFound)}
            else -> {
                NetworkResult.Failure(NetworkError.ServerFailure) }
        }
    }
}

sealed class NetworkResult {
    class Success(val weather: Weather) : NetworkResult()
    class Failure(val error: NetworkError) : NetworkResult()
}

sealed class NetworkError {
    object ServerFailure : NetworkError()
    object CityNotFound : NetworkError()
    class OtherFailure(val errorMessage: String) : NetworkError()
}
