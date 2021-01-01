package com.itsupportwale.dastaan.utility

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.LinearLayout
import com.itsupportwale.dastaan.beans.CalenderModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utilities {

    fun calculateHrs(fromTime:String,toTime:String): String {
        val simpleDateFormat = SimpleDateFormat("hh:mma")
        val startDate: Date = simpleDateFormat.parse(fromTime)
        val endDate: Date = simpleDateFormat.parse(toTime)

        var difference: Long = endDate.getTime() - startDate.getTime()
        if (difference < 0) {
            val dateMax: Date = simpleDateFormat.parse("12:00")
            val dateMin: Date = simpleDateFormat.parse("00:00")
            difference = dateMax.getTime() - startDate.getTime() + (endDate.getTime() - dateMin.getTime())
        }
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        val hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
        val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)

        var timeDate = ""+hours+"hrs "
        if(min>0)
        {
            timeDate += ""+min+"min"
        }
        return timeDate
    }

    fun getChangeDateFormat(date: String?, currentFormat: String?, requiredFormat: String?): String? {
        var newDateString = ""
        if (currentFormat != null && currentFormat.length > 0 && requiredFormat != null && requiredFormat.length > 0) {
            val dateObj: Date
            val input = SimpleDateFormat(currentFormat, Locale.US)
            val output = SimpleDateFormat(requiredFormat, Locale.US)
            try {
                dateObj = input.parse(date)
                newDateString = output.format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return newDateString
    }


    var grey:Int =1
    var pink = 2
    var white = 3
    var weekdays =
            arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    var months = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    )
    var daysOfMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private var daysInMonth = 0
    var currentDayOfMonth = 0
    var currentWeekDay = 0

    fun calculateCalenderDate(mm: Int, yy: Int): ArrayList<CalenderModel>? {
        val tag = "tag"
        val list: ArrayList<String> = ArrayList()
        val calenderModelArrayList: ArrayList<CalenderModel> = ArrayList()
        val DAY_OFFSET = 1
        Log.d(tag, "==> printMonth: mm: $mm yy: $yy")
        var trailingSpaces = 0
        var daysInPrevMonth = 0
        var prevMonth = 0
        var prevYear = 0
        var nextMonth = 0
        var nextYear = 0
        val currentMonth = mm - 1
        val currentMonthName: String =months[currentMonth]
        daysInMonth = daysOfMonth[currentMonth]
        Log.d(
                tag,
                "Current Month:  $currentMonthName having $daysInMonth days."
        )
        val cal = GregorianCalendar(yy, currentMonth, 1)
        Log.d(tag, "Gregorian Calendar:= " + cal.time.toString())
        if (currentMonth == 11) {
            prevMonth = currentMonth - 1
            daysInPrevMonth = prevMonth
            nextMonth = 0
            prevYear = yy
            nextYear = yy + 1
            Log.d(
                    tag,
                    "*->PrevYear: $prevYear PrevMonth:$prevMonth NextMonth: $nextMonth NextYear: $nextYear"
            )
        } else if (currentMonth == 0) {
            prevMonth = 11
            prevYear = yy - 1
            nextYear = yy
            daysInPrevMonth = daysOfMonth[prevMonth]
            nextMonth = 1
            Log.d(
                    tag,
                    "**--> PrevYear: $prevYear PrevMonth:$prevMonth NextMonth: $nextMonth NextYear: $nextYear"
            )
        } else {
            prevMonth = currentMonth - 1
            nextMonth = currentMonth + 1
            nextYear = yy
            prevYear = yy
            daysInPrevMonth =daysOfMonth[prevMonth]
            Log.d(
                    tag,
                    "***---> PrevYear: $prevYear PrevMonth:$prevMonth NextMonth: $nextMonth NextYear: $nextYear"
            )
        }
        val currentWeekDay = cal[Calendar.DAY_OF_WEEK] - 1
        trailingSpaces = currentWeekDay
        Log.d(
                tag, "Week Day:" + currentWeekDay + " is "
                + weekdays[currentWeekDay]
        )
        Log.d(tag, "No. Trailing space to Add: $trailingSpaces")
        Log.d(tag, "No. of Days in Previous Month: $daysInPrevMonth")
        if (cal.isLeapYear(cal[Calendar.YEAR])) if (mm == 2) ++daysInMonth else if (mm == 3) ++daysInPrevMonth

        // Trailing Month days
        for (i in 0 until trailingSpaces) {
            Log.d(
                    tag,
                    "PREV MONTH:= " + prevMonth + " => " + months[prevMonth] + " " + (daysInPrevMonth - trailingSpaces + DAY_OFFSET + i).toString()
            )
            list.add(
                    (daysInPrevMonth - trailingSpaces + DAY_OFFSET + i).toString() + "-GREY" + "-" + months[prevMonth]
                            + "-" + prevYear
            )
            val calenderModel = CalenderModel()
            calenderModel.days = (daysInPrevMonth - trailingSpaces + DAY_OFFSET + i).toString()
            calenderModel.type = grey
            calenderModel.month = months[prevMonth]
            calenderModel.year = prevYear.toString() + ""
            calenderModelArrayList.add(calenderModel)
        }

        // Current Month Days
        for (i in 1..daysInMonth) {
            Log.d(
                    currentMonthName,
                    i.toString() + " " + months[currentMonth]  + " " + yy
            )
            if (i == currentDayOfMonth) {
                list.add(i.toString() + "-BLUE" + "-" + months[currentMonth]  + "-" + yy)
                val calenderModel = CalenderModel()
                calenderModel.type = pink
                calenderModel.days = i.toString()
                calenderModel.month = months[currentMonth]
                calenderModel.year = yy.toString() + ""
                calenderModelArrayList.add(calenderModel)
            } else {
                //month data
                list.add(i.toString() + "-WHITE" + "-" + months[currentMonth] + "-" + yy)
                val calenderModel = CalenderModel()
                calenderModel.type = white
                calenderModel.days = i.toString()
                calenderModel.month = months[currentMonth]
                calenderModel.year = yy.toString() + ""
                calenderModelArrayList.add(calenderModel)
            }
        }

        // Leading Month days

        // Leading Month days

        // Leading Month days
        val leading = list.size % 7
        for (i in 0 until leading) {
            Log.d(tag, "NEXT MONTH:= " +  months[nextMonth] )
            list.add((i + 1).toString() + "-GREY" + "-" +  months[nextMonth]  + "-" + nextYear)
            val calenderModel = CalenderModel()
            calenderModel.type = grey
            calenderModel.days = (i + 1).toString()
            calenderModel.month = months[nextMonth]
            calenderModel.year = nextYear.toString() + ""
            calenderModelArrayList.add(calenderModel)
        }

        /*  val leading = list.size % 7
        for (var i = 0 i < leading; i++) {
            Log.d(tag, "NEXT MONTH:= " +  months[nextMonth] )
            list.add((i + 1).toString() + "-GREY" + "-" +  months[nextMonth]  + "-" + nextYear)
            val calenderModel = CalenderModel()
            calenderModel.type = grey
            calenderModel.days = (i + 1).toString()
            calenderModel.month = months[nextMonth]
            calenderModel.year = nextYear.toString() + ""
            calenderModelArrayList.add(calenderModel)
        }*/
        return calenderModelArrayList
    }

    /* public static void changeBgStrokeColor(LinearLayout school, int colors) {
        GradientDrawable bgShape = (GradientDrawable) school.getBackground();
        //bgShape.setColor(colors);
        bgShape.setStroke(3, colors);
    }*/
    fun setBgWithOutStrokeColor(school: LinearLayout, colors: Int) {
        val bgShape = school.background as GradientDrawable
        bgShape.setColor(colors)
        //  bgShape.setStroke(1, colors);
    }
}