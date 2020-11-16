package com.rakuten.weatherstack.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rakuten.weatherstack.R
import com.rakuten.weatherstack.model.Weather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_weather.view.*

class MainAdapter(private val layoutInflater: LayoutInflater,
                  private val onDeleteClickListener: onDeleteClickListener,
                  private val onItemClickListener: onItemClickListener) : ListAdapter<Weather, MainViewHolder>(WeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(layoutInflater.inflate(MainViewHolder.LAYOUT, parent, false), onDeleteClickListener, onItemClickListener)

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) = holder.render(getItem(position))

    override fun getItemId(position: Int): Long =  getItem(position).cityName.hashCode().toLong()
}
class MainViewHolder(private val view: View,
                     private val onDeleteClickListener: onDeleteClickListener,
                     private val onItemClickListener: onItemClickListener): RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_weather
    }

    private val currentCityName by lazy { view.weather_cityName }
    private val currentCityTemperature by lazy { view.weather_temperature }
    private val weatherImage by lazy { view.weather_image }
    private val weatherCondition by lazy { view.weather_condition }
    private val weatherDeleteImage by lazy { view.weather_delete }


    fun render(weather: Weather) {

        with(weather) {
            currentCityName.text = cityName
            currentCityTemperature.text = "$temperature"+"\u2103"
            weatherCondition.text = condition
            Picasso.get().load(icon).into(weatherImage)
            weatherDeleteImage.setOnClickListener { onDeleteClickListener.invoke (cityName) }
            view.setOnClickListener { onItemClickListener.invoke(cityName, temperature, icon) }
        }
    }
}

class WeatherDiffCallback : DiffUtil.ItemCallback<Weather>() {
    override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean = oldItem.cityName == newItem.cityName

    override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean = oldItem == newItem

}

private typealias onDeleteClickListener = (String) -> Unit
private typealias onItemClickListener = (String, Int, String) -> Unit
