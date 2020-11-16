package com.rakuten.weatherstack.model

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface WeatherDao {

    @get:Query("SELECT * FROM weather_table")
    val getAll: Observable<List<Weather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: Weather)

    @Query("DELETE FROM weather_table WHERE cityName = :cityName")
    fun delete(cityName: String)

    @Query("DELETE FROM weather_table")
    fun deleteAll()

    @Update
    fun update(weather: Weather)
}

