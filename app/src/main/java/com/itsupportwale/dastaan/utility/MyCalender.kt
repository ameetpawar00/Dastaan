package com.itsupportwale.dastaan.utility

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class MyCalender
{
    companion object {

        fun isEqualDateAndTime(firstDate: Calendar, SecondDate: Calendar): Boolean {
            if (firstDate.get(Calendar.YEAR) == SecondDate.get(Calendar.YEAR)) {
                if (firstDate.get(Calendar.MONTH) == SecondDate.get(Calendar.MONTH)) {
                    if (firstDate.get(Calendar.DATE) == SecondDate.get(Calendar.DATE)) {
                        if (firstDate.get(Calendar.HOUR_OF_DAY) == SecondDate.get(Calendar.HOUR_OF_DAY)) {
                            if (firstDate.get(Calendar.MINUTE) == SecondDate.get(Calendar.MINUTE)) {
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }

        fun isEqualDate(firstDate: Calendar, SecondDate: Calendar): Boolean {
            if (firstDate[Calendar.YEAR] == SecondDate[Calendar.YEAR]) {
                if (firstDate[Calendar.MONTH] == SecondDate[Calendar.MONTH]) {
                    if (firstDate[Calendar.DATE] == SecondDate[Calendar.DATE]) {
                        return true
                    }
                }
            }
            return false
        }

        fun compareTwoDateAndTime(firstDate: Calendar, SecondDate: Calendar): Boolean {
            if (firstDate[Calendar.YEAR] > SecondDate[Calendar.YEAR]) {
                return true
            } else {
                if (firstDate[Calendar.YEAR] == SecondDate[Calendar.YEAR]) {
                    if (firstDate[Calendar.MONTH] > SecondDate[Calendar.MONTH]) {
                        return true
                    } else {
                        if (firstDate[Calendar.MONTH] == SecondDate[Calendar.MONTH]) {
                            if (firstDate[Calendar.DATE] > SecondDate[Calendar.DATE]) {
                                return true
                            } else {
                                if (firstDate[Calendar.DATE] == SecondDate[Calendar.DATE]) {
                                    if (firstDate[Calendar.HOUR_OF_DAY] > SecondDate[Calendar.HOUR_OF_DAY]
                                    ) {
                                        return true
                                    } else {
                                        if (firstDate[Calendar.HOUR_OF_DAY] == SecondDate[Calendar.HOUR_OF_DAY]
                                        ) {
                                            if (firstDate[Calendar.MINUTE] > SecondDate[Calendar.MINUTE]
                                            ) {
                                                return true
                                            }
                                           /* else {
                                                if (firstDate[Calendar.MINUTE] == SecondDate[Calendar.MINUTE]) {
                                                    if (firstDate[Calendar.SECOND] > SecondDate[Calendar.SECOND]) {
                                                        return true
                                                    }
                                                }
                                            }*/
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false
        }


       /* fun compareTwoHourAndMonute(firstDate: Calendar, SecondDate: Calendar): Boolean {
            Log.e("first date", firstDate[Calendar.HOUR_OF_DAY].toString() + " " + SecondDate[Calendar.HOUR_OF_DAY])
            if (firstDate[Calendar.HOUR_OF_DAY] > SecondDate[Calendar.HOUR_OF_DAY]) {
                return true
            } else {
                if (firstDate[Calendar.HOUR_OF_DAY] == SecondDate[Calendar.HOUR_OF_DAY]) {
                    if (firstDate[Calendar.MINUTE] > SecondDate[Calendar.MINUTE]) {
                        return true
                    }
                }
            }
            return false
        }*/

        fun compareTwoTime(firstDate: Calendar, SecondDate: Calendar): Boolean {
            Log.d("first date", firstDate[Calendar.HOUR_OF_DAY].toString() + " " + SecondDate[Calendar.HOUR_OF_DAY])
            if (firstDate[Calendar.HOUR_OF_DAY] > SecondDate[Calendar.HOUR_OF_DAY]) {
                return true
            } else {
                if (firstDate[Calendar.HOUR_OF_DAY] == SecondDate[Calendar.HOUR_OF_DAY]) {
                    if (firstDate[Calendar.MINUTE] > SecondDate[Calendar.MINUTE]) {
                        return true
                    } /*else {
                        if (firstDate[Calendar.MINUTE] == SecondDate[Calendar.MINUTE]) {
                            if (firstDate[Calendar.SECOND] > SecondDate[Calendar.SECOND]) {
                                return true
                            }
                        }
                    }*/
                }
            }
            return false
        }

        fun isTimeValid(times:String) :Boolean
        {
            var sDF = SimpleDateFormat("hh:mm a")

            var thisTime = Calendar.getInstance()
            var cTm = Calendar.getInstance()
            thisTime.time = sDF.parse(times)

            var ctmS  = sDF.format(cTm.time)
            cTm.time = sDF.parse(ctmS)

           // Log.d("firstdate", ""+thisTime.time + " " + cTm.time)
            Log.d("firstdate", ""+thisTime[Calendar.HOUR_OF_DAY] + " " +thisTime[Calendar.MINUTE] + " " + cTm[Calendar.HOUR_OF_DAY]+ " " +cTm[Calendar.MINUTE])
            if (thisTime[Calendar.HOUR_OF_DAY] >= cTm[Calendar.HOUR_OF_DAY] ){
                if (thisTime[Calendar.MINUTE] >= cTm[Calendar.MINUTE]) {}
                    return true

            }
            return false
        }

        fun isTimeInMorning(times:String) :Boolean
        {
            var simpleDateFormat = SimpleDateFormat("hh:mm a")
            var firstDate = Calendar.getInstance()
            firstDate.time = simpleDateFormat.parse(times)
            if (firstDate[Calendar.HOUR_OF_DAY] < 12 ){
                return true
            }
            return false
        }
        fun isTimeInAfterNoon(times:String) :Boolean
        {
            var simpleDateFormat = SimpleDateFormat("hh:mm a")
            var firstDate = Calendar.getInstance()
            firstDate.time = simpleDateFormat.parse(times)
            if (firstDate[Calendar.HOUR_OF_DAY] >= 12 && firstDate[Calendar.HOUR_OF_DAY] <= 17 ){
                return true
            }
            return false
        }

        fun isTimeInEvening(times:String) :Boolean
        {
            var simpleDateFormat = SimpleDateFormat("hh:mm a")
            var firstDate = Calendar.getInstance()
            firstDate.time = simpleDateFormat.parse(times)

            if (firstDate[Calendar.HOUR_OF_DAY] < 17 ){
                return true
            }
            return false
        }

        fun compareTwoDate(firstDate: Calendar, SecondDate: Calendar): Boolean {
            if (firstDate[Calendar.YEAR] > SecondDate[Calendar.YEAR]) {
                return true
            } else {
                if (firstDate[Calendar.YEAR] === SecondDate[Calendar.YEAR]) {
                    if (firstDate[Calendar.MONTH] > SecondDate[Calendar.MONTH]) {
                        return true
                    } else {
                        run {
                            if (firstDate[Calendar.DATE] > SecondDate[Calendar.DATE]) {
                                return true
                            }
                        }
                    }
                }
            }
            return false
        }

        fun get24TimeFormate(time: String?): String? {
            var time = time
            val displayFormat = SimpleDateFormat("HH:mm")
            val parseFormat = SimpleDateFormat("hh:mm a")
            var date: Date? = null
            try {
                date = parseFormat.parse(time)
            } catch (e: Exception) {

                e.printStackTrace()
            }
            println(parseFormat.format(date).toString() + " = " + displayFormat.format(date))
            time = displayFormat.format(date)
            return time
        }

        fun get12TimeFormate(time: String?): String? {
            var time = time
            val displayFormat = SimpleDateFormat("hh:mm a")
            val parseFormat = SimpleDateFormat("HH:mm")
            var date: Date? = null
            try {
                date = parseFormat.parse(time)
            } catch (e:Exception) {

                e.printStackTrace()
            }
            println(parseFormat.format(date).toString() + " = " + displayFormat.format(date))
            time = displayFormat.format(date)
            return time
        }

        fun getCalenderFromString(date: String?, dateFormate: String?): Calendar? {
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            try {
                val simpleDateFormat = SimpleDateFormat(dateFormate)
                calendar.time = simpleDateFormat.parse(date)
            } catch (e: java.lang.Exception) {
            }
            return calendar
        }

        fun getStringFromCal(calendar: Calendar, dateFormate: String?): String? {
            var dateStr = ""
            try {
                val simpleDateFormat =
                    SimpleDateFormat(dateFormate)
                dateStr = simpleDateFormat.format(calendar.time)
            } catch (e: java.lang.Exception) {
            }
            return dateStr
        }

        fun formattedDateFromString(
            inputFormat: String,
            outputFormat: String,
            inputDate: String?
        ): String? {
            var inputFormat = inputFormat
            var outputFormat = outputFormat
            if (inputFormat == "") { // if inputFormat = "", set a default
                // input format.
                inputFormat = "dd-MM-yyyy"
            }
            if (outputFormat == "") {
                outputFormat = "EEEE d MMMM  yyyy"
                // if inputFormat =
                // "", set a default
                // output format.Thursday 15 August 2019
            }
            var parsed: Date? = null
            var outputDate = ""
            val df_input =
                SimpleDateFormat(inputFormat, Locale.getDefault())
            val df_output =
                SimpleDateFormat(outputFormat, Locale.getDefault())

            // You can set a different Locale, This example set a locale of Country
            // Mexico.
            // SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new
            // Locale("es", "MX"));
            // SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new
            // Locale("es", "MX"));
            try {
                parsed = df_input.parse(inputDate)
                outputDate = df_output.format(parsed)
            } catch (e: java.lang.Exception) {
                Log.e(
                    "formattedDateFromString",
                    "Exception in formateDateFromstring(): " + e.message
                )
            }
            return outputDate
        }

    }




}