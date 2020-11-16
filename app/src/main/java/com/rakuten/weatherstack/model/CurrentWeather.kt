package com.rakuten.weatherstack.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(@SerializedName("temperature")
                          val temperature: Int,
                          @SerializedName("weather_icons")
                          val listOfIcons: List<String>,
                          @SerializedName("weather_descriptions")
                          val weatherDescriptions: List<String>){
    companion object{
        val EMPTY = CurrentWeather(-1, emptyList(), emptyList())
    }
}

data class LocationInfo(@SerializedName("name")
                        val name: String){
    companion object{
        val EMPTY = LocationInfo("")
    }
}
