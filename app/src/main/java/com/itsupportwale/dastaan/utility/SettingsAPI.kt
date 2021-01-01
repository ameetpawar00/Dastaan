package com.itsupportwale.dastaan.utility

import android.content.Context
import android.content.SharedPreferences
import com.itsupportwale.dastaan.R
import java.util.*

class SettingsAPI(var mContext: Context) {
    private val sharedSettings: SharedPreferences
    fun readSetting(key: String?): String? {
        return sharedSettings.getString(key, "na")
    }

    fun addUpdateSettings(key: String?, value: String?) {
        val editor = sharedSettings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun deleteAllSettings() {
        sharedSettings.edit().clear().apply()
    }

    fun readAll(): List<String> {
        val allUser: MutableList<String> = ArrayList()
        val allEntries = sharedSettings.all
        for ((key, value) in allEntries) {
            if (key.contains("@")) allUser.add("$key ($value)")
        }
        return allUser
    }

    init {
        sharedSettings = mContext.getSharedPreferences(
            mContext.getString(R.string.settings_file_name),
            Context.MODE_PRIVATE
        )
    }
}