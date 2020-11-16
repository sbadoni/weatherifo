package com.rakuten.weatherstack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class Weather(@PrimaryKey
                   val cityName: String,
                   val temperature: Int,
                   val icon: String,
                   val condition: String){
    companion object {
        val EMPTY = Weather("", -1, "", "")
    }

}
