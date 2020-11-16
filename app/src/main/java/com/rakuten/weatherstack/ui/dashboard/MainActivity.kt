package com.rakuten.weatherstack.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.rakuten.weatherstack.R
import com.rakuten.weatherstack.model.Weather
import com.rakuten.weatherstack.model.WeatherResponse
import com.rakuten.weatherstack.source.network.RemoteDataSource
import com.rakuten.weatherstack.ui.details.CITY_NAME_EXTRA
import com.rakuten.weatherstack.ui.details.DetailsActivity
import com.rakuten.weatherstack.ui.details.ICON_EXTRA
import com.rakuten.weatherstack.ui.details.TEMP_EXTRA
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivity"
private const val SEARCH_QUERY_TIMEOUT = 500L
private const val SEARCH_QUERY_MINI_CHAR = 3
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val mainAdapter: MainAdapter by inject(parameters = { parametersOf(layoutInflater, mainViewModel::deleteWeatherData, this::launchDetailsPage) })
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("sameer onCreate $mainViewModel")
        setContentView(R.layout.activity_main)
        weather_searchView.setQuery("", false)
        initRecyclerView()
        initWeatherData()
        initSearchView()
    }


    private fun initSearchView(){
        disposable.add(Observable.create(ObservableOnSubscribe<String> { subscriber ->
            weather_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    subscriber.onNext(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    subscriber.onNext(query)
                    return false
                }
            })
        })
            .distinctUntilChanged()
            .filter { it.isNotEmpty() && it.length >= SEARCH_QUERY_MINI_CHAR}
            .map { text -> text.toLowerCase().trim() }
            .debounce(SEARCH_QUERY_TIMEOUT, TimeUnit.MILLISECONDS)
            .subscribe { text -> mainViewModel.onSearchTextChanged(text) }
        )
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun initWeatherData() {
        mainViewModel.getAllCitiesData().observe(this, Observer { weatherList ->
            weatherList?.let {
                mainAdapter.submitList(weatherList)
            }
        })

        mainViewModel.errorLiveData.observe(this, Observer<String> { error ->
            if (error!= null) {
                Toast.makeText(this,error, Toast.LENGTH_LONG ).show()
            }
        })
    }

    private fun initRecyclerView(){
        weather_recyclerView.run {
            val linearLayoutManager = get<LinearLayoutManager>(parameters = { parametersOf(context!!) })
            val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
            context?.let { ContextCompat.getDrawable(it, R.drawable.horizontal_divider) }?.let(dividerItemDecoration::setDrawable)
            addItemDecoration(dividerItemDecoration)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = linearLayoutManager
            adapter = mainAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun launchDetailsPage(citName: String, temp: Int, iconUrl: String){
        val intent = Intent(applicationContext, DetailsActivity::class.java).apply {
            putExtra(CITY_NAME_EXTRA, citName)
            putExtra(TEMP_EXTRA, temp)
            putExtra(ICON_EXTRA, iconUrl)
        }
        startActivity(intent)
    }
}
