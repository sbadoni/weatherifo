package com.rakuten.weatherstack.ui.dashboard

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rakuten.weatherstack.model.Weather
import com.rakuten.weatherstack.model.WeatherResponse
import com.rakuten.weatherstack.source.network.NetworkError
import com.rakuten.weatherstack.source.network.NetworkResult
import com.rakuten.weatherstack.usecase.CommandSaveWeatherInfo
import com.rakuten.weatherstack.usecase.QuerySavedWeatherData
import com.rakuten.weatherstack.usecase.SearchCityWeather
import com.rakuten.weatherstack.usecase.DeleteWeatherData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val searchCityWeather: SearchCityWeather,
    private val commandSaveWeatherInfo: CommandSaveWeatherInfo,
    private val querySavedWeatherData: QuerySavedWeatherData,
    private val deleteWeatherData: DeleteWeatherData
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val maxAttempts = 2

    init {
        disposable.add(querySavedWeatherData()
            .firstOrError()
            .subscribe({ list -> updateWeatherInfoAtLaunch(list) }, { throwable -> println("Sameer Error... $throwable") }))
        disposable.add(querySavedWeatherData().subscribe({ list -> mapInitialData(list) }, { throwable -> println("Sameer 2Error...$throwable") }))
    }

    private val weatherLiveData = MutableLiveData<List<Weather>>()
    val errorLiveData = MutableLiveData<String>()
    private fun getWeatherForLocationName(name: String) =
        searchCityWeather(name)
            .retryWhen { errors ->
                errors
                    .scan(1) { count, error ->
                        if (count > maxAttempts) {
                            throw error
                        }
                        count + 1
                    }
                    .flatMap { Observable.timer(it.toLong(), TimeUnit.SECONDS) }
            }
            .onErrorReturn {
                NetworkResult.Failure(NetworkError.OtherFailure(it.message?: "Unknown Error") )
            }


    fun onSearchTextChanged(cityName: String): Disposable = getWeatherForLocationName(cityName)
        .flatMapCompletable { networkResult ->
            when (networkResult) {
                is NetworkResult.Success -> {
                    commandSaveWeatherInfo(networkResult.weather)
                }
                is NetworkResult.Failure -> {
                    when (networkResult.error) {
                        is NetworkError.ServerFailure -> errorLiveData.postValue("Server Failure")
                        is NetworkError.CityNotFound -> errorLiveData.postValue("City Not Found")
                        is  NetworkError.OtherFailure -> errorLiveData.postValue(networkResult.error.errorMessage)
                    }
                    Completable.complete()
                }
            }
        }
        .subscribeOn(Schedulers.io())
        .subscribe()

    fun getAllCitiesData() = weatherLiveData

    private fun mapInitialData(listOf: List<Weather>) {
        weatherLiveData.postValue(listOf)
    }

    private fun updateWeatherInfoAtLaunch(listOfCities: List<Weather>){
        for(city in listOfCities){
            println("sameer update for citi $city")
            onSearchTextChanged(city.cityName)
        }
    }

    fun deleteWeatherData(cityName: String): Disposable =
        deleteWeatherData.invoke(cityName).subscribeOn(Schedulers.io()).subscribe()

    override fun onCleared() {
        super.onCleared()
        println(("sameer clearing the viewModel"))
        disposable.clear()
    }

}
