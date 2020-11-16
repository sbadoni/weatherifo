package com.rakuten.weatherstack.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(@SerializedName("success")
                           val success: Boolean? = false,
                           @SerializedName("error")
                           val error: ErrorCode? = ErrorCode.EMPTY,
                           @SerializedName("location")
                           val locationInfo: LocationInfo,
                           @SerializedName("current")
                           val currentWeather: CurrentWeather){

    fun toWeatherModel(): Weather = Weather( locationInfo.name, currentWeather.temperature, currentWeather.listOfIcons.first(), currentWeather.weatherDescriptions.first())

    companion object {
        val EMPTY =
            WeatherResponse(false, ErrorCode.EMPTY, LocationInfo.EMPTY, CurrentWeather.EMPTY)

    }
}


