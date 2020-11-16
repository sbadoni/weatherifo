package com.rakuten.weatherstack.ui.dashboard

data class CityWeatherModel(val cityName: String,
                            val temperature: Int,
                            val icon: String,
                            val condition: String){
    companion object {
        val EMPTY = CityWeatherModel("", -1, "", "")
    }
}
