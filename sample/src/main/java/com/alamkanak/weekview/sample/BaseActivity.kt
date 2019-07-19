package com.alamkanak.weekview.sample

import android.graphics.RectF
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.sample.data.FakeEventsDatabase
import com.alamkanak.weekview.sample.data.ReactiveEventsStore
import com.alamkanak.weekview.sample.data.model.Event
import java.util.Calendar
import java.util.Locale

/**
 * This is a base activity which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
class BaseActivity : AppCompatActivity() {

    private var weekViewType = TYPE_THREE_DAY_VIEW
    private val weekView: WeekView<Event> by lazy {
        findViewById<WeekView<Event>>(R.id.weekView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val database = FakeEventsDatabase(this)
        val store = ReactiveEventsStore(database)

        weekView.setOnEventClickListener(this::onEventClick)
        weekView.setOnEventLongPressListener(this::onEventLongPress)
        weekView.setOnEmptyViewClickListener(this::onEmptyViewLongPress)
        weekView.setOnLoadMoreListener(store::fetchEvents)

        store.events.observe(this, Observer { weekView.submit(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_today -> {
                weekView.goToToday()
                return true
            }
            R.id.action_day_view -> {
                openDayView(item)
                return true
            }
            R.id.action_three_day_view -> {
                openThreeDayView(item)
                return true
            }
            R.id.action_week_view -> {
                openWeekView(item)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openDayView(item: MenuItem) {
        if (weekViewType == TYPE_DAY_VIEW) {
            return
        }

        item.isChecked = !item.isChecked
        weekViewType = TYPE_DAY_VIEW
        weekView.numberOfVisibleDays = 1
    }

    private fun openThreeDayView(item: MenuItem) {
        if (weekViewType == TYPE_THREE_DAY_VIEW) {
            return
        }

        item.isChecked = !item.isChecked
        weekViewType = TYPE_THREE_DAY_VIEW
        weekView.numberOfVisibleDays = 3
    }

    private fun openWeekView(item: MenuItem) {
        if (weekViewType == TYPE_WEEK_VIEW) {
            return
        }

        item.isChecked = !item.isChecked
        weekViewType = TYPE_WEEK_VIEW
        weekView.numberOfVisibleDays = 7
    }

    private fun getEventTitle(time: Calendar): String {
        val hour = time.get(Calendar.HOUR_OF_DAY)
        val minute = time.get(Calendar.MINUTE)
        val month = time.get(Calendar.MONTH) + 1
        val dayOfMonth = time.get(Calendar.DAY_OF_MONTH)
        return String.format(Locale.getDefault(), "Event of %02d:%02d %s/%d", hour, minute, month, dayOfMonth)
    }

    private fun onEventClick(data: Event, eventRect: RectF) {
        Toast.makeText(this, "Clicked " + data.title, Toast.LENGTH_SHORT).show()
    }

    private fun onEventLongPress(data: Event, eventRect: RectF) {
        Toast.makeText(this, "Long pressed event: " + data.title, Toast.LENGTH_SHORT).show()
    }

    private fun onEmptyViewLongPress(time: Calendar) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TYPE_DAY_VIEW = 1
        private const val TYPE_THREE_DAY_VIEW = 2
        private const val TYPE_WEEK_VIEW = 3
    }

}
