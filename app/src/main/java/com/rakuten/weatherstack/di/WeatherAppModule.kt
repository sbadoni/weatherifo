package com.rakuten.weatherstack.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.rakuten.weatherstack.source.db.WeatherDatabase
import com.rakuten.weatherstack.source.network.RemoteDataSource
import com.rakuten.weatherstack.source.network.WeatherInterface
import com.rakuten.weatherstack.ui.dashboard.MainAdapter
import com.rakuten.weatherstack.ui.dashboard.MainViewModel
import com.rakuten.weatherstack.ui.details.DetailsAdapter
import com.rakuten.weatherstack.usecase.CommandSaveWeatherInfo
import com.rakuten.weatherstack.usecase.QuerySavedWeatherData
import com.rakuten.weatherstack.usecase.SearchCityWeather
import com.rakuten.weatherstack.usecase.DeleteWeatherData
import com.rakuten.weatherstack.util.WeatherResources
import com.rakuten.weatherstack.util.WeatherResourcesImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val RETROFIT_CLIENT = "RETROFIT_CLIENT"
val WeatherAppModule = module {

    single<Resources> { get<Application>().resources }
    //Web interface
    single<WeatherResources> { WeatherResourcesImpl(get()) }
    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    single<OkHttpClient> {
        OkHttpClient
            .Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single<Retrofit>(named(RETROFIT_CLIENT)) {
        Retrofit.Builder()
            .baseUrl(get<WeatherResources>().getWeatherUrl())
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
    single<WeatherInterface> { get<Retrofit>(named(RETROFIT_CLIENT)).create(WeatherInterface::class.java) }

    //Room init
    single {
        Room.databaseBuilder(
            get<Application>(),
            WeatherDatabase::class.java,
            WeatherDatabase.DB_NAME
        )
            .build()
    }

    single { get<WeatherDatabase>().weatherDao() }

    //Data Source init
    single { RemoteDataSource(get(), get()) }

    //usecases
    single { CommandSaveWeatherInfo(get()) }
    single { QuerySavedWeatherData(get()) }
    single { SearchCityWeather(get()) }
    single { DeleteWeatherData(get()) }

    //viewModels
    viewModel {
        MainViewModel(get(), get(), get(), get())
    }

    //Adapter

    factory {
        val layoutInflater: LayoutInflater = it[0]
        val onDeleteClickListener: (String) -> Unit = it[1]
        val onItemClickListener: (String, Int, String) -> Unit = it[2]
        MainAdapter(
            layoutInflater,
            onDeleteClickListener,
            onItemClickListener
        )
    }

    factory {
        val layoutInflater: LayoutInflater = it[0]
        DetailsAdapter(
            layoutInflater
        )
    }

    //util
    factory { LinearLayoutManager(androidApplication()) }
}
