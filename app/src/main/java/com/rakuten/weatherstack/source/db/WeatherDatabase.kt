package com.rakuten.weatherstack.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rakuten.weatherstack.model.Weather
import com.rakuten.weatherstack.model.WeatherDao

@Database(entities = [Weather::class], version = WeatherDatabase.VERSION)

abstract class WeatherDatabase : RoomDatabase(){

    companion object{
        const val DB_NAME = "weather_database"
        const val VERSION = 1
    }

    abstract fun weatherDao(): WeatherDao
}
