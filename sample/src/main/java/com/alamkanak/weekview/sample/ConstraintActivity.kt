package com.alamkanak.weekview.sample

import android.graphics.RectF
import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alamkanak.weekview.DateTimeInterpreter
import com.alamkanak.weekview.OnEmptyViewLongClickListener
import com.alamkanak.weekview.OnEventClickListener
import com.alamkanak.weekview.OnEventLongClickListener
import com.alamkanak.weekview.OnMonthChangeListener
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.sample.apiclient.Event
import com.alamkanak.weekview.sample.data.EventsDatabase
import com.alamkanak.weekview.sample.data.FakeEventsDatabase
import kotlinx.android.synthetic.main.activity_constraint.guideline
import kotlinx.android.synthetic.main.activity_constraint.seekBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConstraintActivity : AppCompatActivity(), OnEventClickListener<Event>,
    OnMonthChangeListener<Event>, OnEventLongClickListener<Event>, OnEmptyViewLongClickListener {

    private val weekView: WeekView<Event> by lazy { findViewById<WeekView<Event>>(R.id.weekView) }
    private val database: EventsDatabase by lazy { FakeEventsDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint)

        weekView.onEventClickListener = this
        weekView.onMonthChangeListener = this
        weekView.onEventLongClickListener = this
        weekView.onEmptyViewLongClickListener = this

        setupSeekBarAction()
        setupDateTimeInterpreter()
    }

    override fun onMonthChange(
        startDate: Calendar,
        endDate: Calendar
    ): List<WeekViewDisplayable<Event>> {
        return database.getEventsInRange(startDate, endDate)
    }

    override fun onEventClick(data: Event, eventRect: RectF) {
        Toast.makeText(this, "Clicked " + data.title, Toast.LENGTH_SHORT).show()
    }

    override fun onEventLongClick(data: Event, eventRect: RectF) {
        Toast.makeText(this, "Long pressed event: " + data.title, Toast.LENGTH_SHORT).show()
    }

    override fun onEmptyViewLongClick(time: Calendar) {
        val sdf = SimpleDateFormat.getDateTimeInstance()
        Toast.makeText(this, "Empty view long pressed: " +
            sdf.format(time.time), Toast.LENGTH_SHORT).show()
    }

    private fun setupSeekBarAction() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val adjustedProgress = ((SEEKBAR_MIN_VALUE + progress) / 5) * 5
                guideline.setGuidelinePercent(adjustedProgress / 100f)
            }
        })
    }

    private fun setupDateTimeInterpreter() {
        weekView.dateTimeInterpreter = object : DateTimeInterpreter {

            private val sdfDate = SimpleDateFormat("E dd", Locale.getDefault())

            override fun interpretDate(date: Calendar): String {
                val result = sdfDate.format(date.time).replace(" ".toRegex(), "\n")
                return result.substring(0, 1).toUpperCase(Locale.getDefault()) + result.substring(1)
            }

            override fun interpretTime(hour: Int): String {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, 0)

                val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
                return sdfTime.format(calendar.time)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private companion object {
        private const val SEEKBAR_MIN_VALUE = 50
    }
}
