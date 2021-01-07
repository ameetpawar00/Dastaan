package com.itsupportwale.dastaan.utility

import android.content.Context
import com.google.gson.Gson


class UserPreference private constructor() {


    var loginStatus = 0
    var device_token: String? = ""

    fun save(context: Context) {
        context.getSharedPreferences(UserPreference::class.java.name, Context.MODE_PRIVATE).edit().putString("user", Gson().toJson(jUser)).apply()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(UserPreference::class.java.name, Context.MODE_PRIVATE).edit().clear().apply()
        jUser = null
    }


    var name: String? = ""
    var user_id: Int? = 0
    var phone: String? = ""
    var countryCode: String? = ""
    var dealerLicenseNumber: String? = ""
    var email: String? = ""
    var photo: String? = ""
    var rating: Double? = 0.0
    var followers: Int? = 0
    var type: String? = "user"

    companion object {
        private var jUser: UserPreference? = null
        fun getInstance(context: Context): UserPreference? {
            if (jUser == null) {
                jUser = Gson().fromJson(context.getSharedPreferences(UserPreference::class.java.name, Context.MODE_PRIVATE).getString("user", null), UserPreference::class.java)
                if (jUser == null) jUser = UserPreference()
            }
            return jUser
        }

        fun getjUser(): UserPreference? {
            return jUser
        }

        fun setjUser(jUser: UserPreference?) {
            Companion.jUser = jUser
        }
    }
}
