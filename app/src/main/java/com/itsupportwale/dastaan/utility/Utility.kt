package com.itsupportwale.dastaan.utility

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itsupportwale.dastaan.beans.CalenderModel
import com.itsupportwale.dastaan.servermanager.BooleanDeserializer
import com.itsupportwale.dastaan.servermanager.DoubleDeserializer
import com.itsupportwale.dastaan.servermanager.IntDeserializer
import org.ocpsoft.prettytime.PrettyTime
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Utility {
    var weekdays = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
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

    private fun getMonthAsString(i: Int): String {
        return months[i]
    }

    private fun getWeekDayAsString(i: Int): String {
        return weekdays[i]
    }

    private fun getNumberOfDaysOfMonth(i: Int): Int {
        return daysOfMonth[i]
    }

    fun calculateCalenderDate(mm: Int, yy: Int): ArrayList<CalenderModel> {
        val tag = "tag"
        val list = ArrayList<String>()
        val calenderModelArrayList: ArrayList<CalenderModel> = ArrayList<CalenderModel>()
        val DAY_OFFSET = 1
        Log.d(tag, "==> printMonth: mm: $mm yy: $yy")
        var trailingSpaces = 0
        var daysInPrevMonth = 0
        var prevMonth = 0
        var prevYear = 0
        var nextMonth = 0
        var nextYear = 0
        val currentMonth = mm - 1
        val currentMonthName = getMonthAsString(currentMonth)
        daysInMonth = getNumberOfDaysOfMonth(currentMonth)
        Log.d(tag, "Current Month:  $currentMonthName having $daysInMonth days.")
        val cal = GregorianCalendar(yy, currentMonth, 1)
        Log.d(tag, "Gregorian Calendar:= " + cal.time.toString())
        if (currentMonth == 11) {
            prevMonth = currentMonth - 1
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth)
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
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth)
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
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth)
            Log.d(
                tag,
                "***---> PrevYear: $prevYear PrevMonth:$prevMonth NextMonth: $nextMonth NextYear: $nextYear"
            )
        }
        val currentWeekDay = cal[Calendar.DAY_OF_WEEK] - 1
        trailingSpaces = currentWeekDay
        Log.d(
            tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay)
        )
        Log.d(tag, "No. Trailing space to Add: $trailingSpaces")
        Log.d(tag, "No. of Days in Previous Month: $daysInPrevMonth")
        if (cal.isLeapYear(cal[Calendar.YEAR])) if (mm == 2) ++daysInMonth else if (mm == 3) ++daysInPrevMonth

        // Trailing Month days
        for (i in 0 until trailingSpaces) {
            Log.d(
                tag,
                "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + (daysInPrevMonth - trailingSpaces + DAY_OFFSET + i).toString()
            )
            list.add(
                (daysInPrevMonth - trailingSpaces + DAY_OFFSET + i).toString() + "-GREY" + "-" + getMonthAsString(
                    prevMonth
                ) + "-" + prevYear
            )
            val calenderModel = CalenderModel()
            calenderModel.days = (daysInPrevMonth - trailingSpaces + DAY_OFFSET + i).toString()
            calenderModel.type = (Constant.grey)
            calenderModel.month = (getMonthAsString(prevMonth))
            calenderModel.year = (prevYear.toString() + "")
            calenderModelArrayList.add(calenderModel)
        }

        // Current Month Days
        for (i in 1..daysInMonth) {
            Log.d(currentMonthName, i.toString() + " " + getMonthAsString(currentMonth) + " " + yy)
            if (i == currentDayOfMonth) {
                list.add(i.toString() + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy)
                val calenderModel = CalenderModel()
                calenderModel.type = (Constant.pink)
                calenderModel.days = (i.toString())
                calenderModel.month = (getMonthAsString(currentMonth))
                calenderModel.year = (yy.toString() + "")
                calenderModelArrayList.add(calenderModel)
            } else {
                //month data
                list.add(i.toString() + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy)
                val calenderModel = CalenderModel()
                calenderModel.type = (Constant.white)
                calenderModel.days = (i.toString())
                calenderModel.month = (getMonthAsString(currentMonth))
                calenderModel.year = (yy.toString() + "")

                calenderModelArrayList.add(calenderModel)
            }
        }

        // Leading Month days
        for (i in 0 until list.size % 7) {
            Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth))
            list.add((i + 1).toString() + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear)
            val calenderModel = CalenderModel()
            calenderModel.type = (Constant.grey)
            calenderModel.days = ((i + 1).toString())
            calenderModel.month = (getMonthAsString(nextMonth))
            calenderModel.year = (nextYear.toString() + "")


            calenderModelArrayList.add(calenderModel)
        }
        return calenderModelArrayList
    }

    companion object {
        fun dateToTimeFormat(oldstringDate: String?): String? {
            val p = PrettyTime(Locale.ENGLISH)
            var isTime: String? = ""
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                val date = sdf.parse(oldstringDate)
                isTime = p.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return isTime
        }

        fun convertLongToDate(time: Long): String {
            val date = Date(time)
            val format: Format = SimpleDateFormat("dd, MMM yyyy", Locale.getDefault())
            return format.format(date)
        }

        fun convertLongToDateTime(time: Long): String {
            val date = Date(time)
            val format: Format = SimpleDateFormat("dd, MMM yyyy hh:mm aa", Locale.getDefault())
            return format.format(date)
        }

        //
        //https://console.firebase.google.com/project/parttime-c858c/storage/parttime-c858c.appspot.com/files
        const val URL_STORAGE_REFERENCE = "gs://parttime-c858c.appspot.com"
        const val FOLDER_STORAGE_IMG = "images"
        fun getChangeDateFormat(
            date: String?,
            currentFormat: String?,
            requiredFormat: String?
        ): String {
            var newDateString = ""
            if (currentFormat != null && currentFormat.length > 0 && requiredFormat != null && requiredFormat.length > 0) {
                val dateObj: Date
                val input = SimpleDateFormat(currentFormat, Locale.getDefault())
                val output = SimpleDateFormat(requiredFormat, Locale.getDefault())
                try {
                    dateObj = input.parse(date)
                    newDateString = output.format(dateObj)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            return newDateString
        }

        val currentDateIn_YYYY_MM_DD: String
            get() {
                val cal = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return sdf.format(cal.time)
            }

        fun setBgWithOutStrokeColorRelLay(school: RelativeLayout, colors: Int) {
            val bgShape = school.background as GradientDrawable
            bgShape.setColor(colors)
            //  bgShape.setStroke(1, colors);
        }

        fun setBgColor(school: LinearLayout, colors: Int) {
            val bgShape = school.background as GradientDrawable
            bgShape.setColor(colors)
            bgShape.setStroke(1, colors)
        }

        fun setBgColor(linLay: LinearLayout, bgColors: Int, stokeColor: Int) {
            val bgShape = linLay.background as GradientDrawable
            bgShape.setColor(bgColors)
            bgShape.setStroke(2, stokeColor)
        }

        fun setBgColor(relLay: RelativeLayout, bgColors: Int, stokeColor: Int) {
            val bgShape = relLay.background as GradientDrawable
            bgShape.setColor(bgColors)
            bgShape.setStroke(2, stokeColor)
        }

        fun setBgColor(btn: TextView, bgColors: Int, stokeColor: Int) {
            val bgShape = btn.background as GradientDrawable
            bgShape.setColor(bgColors)
            bgShape.setStroke(2, stokeColor)
        }

        fun returnYearFromDates(date: String): String? {
            // fromat yyyy-MM-dd
            var result = ""
            val dateArray: Array<String?> = date.split("-".toRegex()).toTypedArray()
            if (dateArray[0] != null) {
                result = dateArray[0].toString()
            }
            return result
        }

        fun returnMonthFromDates(date: String): String? {
            // fromat yyyy-MM-dd
            var result = ""
            val dateArray: Array<String?> = date.split("-".toRegex()).toTypedArray()
            if (dateArray[1] != null) {
                result = dateArray[1].toString()
            }
            return result
        }

        fun returnDayFromDates(date: String): String? {
            // fromat yyyy-MM-dd
            var result = ""
            val dateArr: Array<String?> = date.split("-".toRegex()).toTypedArray()
            if (dateArr[2] != null) {
                result = dateArr[2].toString()
            }
            return result
        }

        fun getCurrentDate(format: String?): String {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(cal.time)
        }
    }



}
