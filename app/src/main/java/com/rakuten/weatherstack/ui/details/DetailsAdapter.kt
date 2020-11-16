package com.rakuten.weatherstack.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rakuten.weatherstack.R
import com.rakuten.weatherstack.model.Weather
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_weather.view.*

class DetailsAdapter(private val layoutInflater: LayoutInflater) : ListAdapter<DetailViewModel, MainViewHolder>(WeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(layoutInflater.inflate(MainViewHolder.LAYOUT, parent, false))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) = holder.render(getItem(position))

    override fun getItemId(position: Int): Long =  getItem(position).currentDay.hashCode().toLong()
}
class MainViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    companion object {
        const val LAYOUT = R.layout.item_detail
    }

    private val presentDay by lazy { view.item_detail_day }
    private val currentTemperature by lazy { view.item_detail_temperature }
    private val weatherImage by lazy { view.item_detail_image }


    fun render(detailViewModel: DetailViewModel) {

        with(detailViewModel) {
            presentDay.text = currentDay
            currentTemperature.text = "$currentTemp"+"\u2103"
            Picasso.get().load(iconUrl).into(weatherImage)
        }
    }
}

class WeatherDiffCallback : DiffUtil.ItemCallback<DetailViewModel>() {
    override fun areItemsTheSame(oldItem: DetailViewModel, newItem: DetailViewModel): Boolean = oldItem.currentDay == newItem.currentDay

    override fun areContentsTheSame(oldItem: DetailViewModel, newItem: DetailViewModel): Boolean = oldItem == newItem

}

private typealias onDeleteClickListener = (String) -> Unit
private typealias onItemClickListener = (String, Int, String) -> Unit
