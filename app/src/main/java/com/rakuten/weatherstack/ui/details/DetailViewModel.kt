package com.rakuten.weatherstack.ui.details

data class DetailViewModel(val currentDay: String,
                           val currentTemp: Int,
                           val iconUrl: String)

data class DetailViewState(val weekData: List<DetailViewModel>)
