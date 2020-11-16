package com.rakuten.weatherstack.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.rakuten.weatherstack.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

const val CITY_NAME_EXTRA = "CityName"
const val TEMP_EXTRA = "currentTemp"
const val ICON_EXTRA = "iconUrl"

class DetailsActivity : AppCompatActivity() {

    private val detailsAdapter: DetailsAdapter by inject(parameters = { parametersOf(layoutInflater) })

    enum class DayOfWeek(val day: String){
        SUNDAY("Sunday"),
        MONDAY("Monday"),
        TUESDAY("Tuesday"),
        WEDNESDAY("Wednesday"),
        THURSDAY("Thursday"),
        FRIDAY("Friday"),
        SATURDAY("Saturday");

         fun getValue(): Int {
            return ordinal + 1
        }

    }

    var calendar: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initRecyclerView()
        init()
    }

    private fun init() {
        val citName = intent.getStringExtra(CITY_NAME_EXTRA)
        val currentTemp = intent.getIntExtra(TEMP_EXTRA, 0)
        val iconUrl = intent.getStringExtra(ICON_EXTRA)
        details_cityName.text = citName
        details_temperature.text = "$currentTemp" + "\u2103"
        Picasso.get().load(iconUrl).into(details_image)

        //Load Dummy data

        val sdf = SimpleDateFormat("EEEE")
        val listOfDummyData = mutableListOf<DetailViewModel>()
        for (i in 0..6) {
            val calendar: Calendar = GregorianCalendar()
            calendar.add(Calendar.DATE, i)
            val day = sdf.format(calendar.time)
            if( i == 0){
                listOfDummyData.add(DetailViewModel(day, currentTemp, iconUrl))
            }
            else{
                listOfDummyData.add(DetailViewModel(day, 42, iconUrl))
            }
        }
        detailsAdapter.submitList(listOfDummyData)
    }

    private fun initRecyclerView(){
        weather_recyclerView.run {
            val linearLayoutManager = get<LinearLayoutManager>(parameters = { parametersOf(context!!) })
            val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
            context?.let { ContextCompat.getDrawable(it, R.drawable.horizontal_divider) }?.let(dividerItemDecoration::setDrawable)
            addItemDecoration(dividerItemDecoration)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = linearLayoutManager
            adapter = detailsAdapter
        }

    }
}
