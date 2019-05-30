package com.alamkanak.weekview

import android.content.Context
import com.alamkanak.weekview.date.firstDayOfYear
import com.alamkanak.weekview.date.getDefaultDateFormat
import com.alamkanak.weekview.date.getDefaultTimeFormat
import com.alamkanak.weekview.date.withTime
import java.util.*

/**
 * Created by Raquib on 1/6/2015.
 */
interface DateTimeInterpreter {
    fun interpretDate(date: Calendar): String
    fun interpretTime(hour: Int): String
}

internal class DefaultDateTimeInterpreter(
        context: Context,
        numberOfDays: Int
) : DateTimeInterpreter {

    private var sdfDate = getDefaultDateFormat(numberOfDays)
    private val sdfTime = getDefaultTimeFormat(context)

    // This calendar is only used for interpreting the time. To avoid issues with time changes,
    // we always use the first day of the year
    private val calendar = firstDayOfYear()

    fun setNumberOfDays(numberOfDays: Int) {
        sdfDate = getDefaultDateFormat(numberOfDays)
    }

    override fun interpretDate(date: Calendar): String {
        return sdfDate.format(date.time).toUpperCase(Locale.getDefault())
    }

    override fun interpretTime(hour: Int): String {
        val time = calendar.withTime(hour, minutes = 0)
        return sdfTime.format(time.time)
    }

}
