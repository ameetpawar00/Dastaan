package com.itsupportwale.dastaan.utility

import android.content.Context
import com.google.gson.Gson
import com.itsupportwale.dastaan.beans.ResponseGetLocationData


class UserPreference private constructor() {

    var deviceToken: String? = ""
    var language = 0
    var locationData: ResponseGetLocationData? = null
    var latitude : Double = 0.0
    var longitude: Double = 0.0
    var currentCity: String? = ""
    var loginStatus = 0
    var initSelStatus = 0
    var homeAddress: String? = ""
    var device_token: String? = ""
    var carSeenArrayListRight : ArrayList<String> = ArrayList()
    var carSeenArrayListLeft : ArrayList<String> = ArrayList()
    var favouriteSportMap : HashMap<String, String> = HashMap<String, String> ()
    var favouriteSport = ""

    fun save(context: Context) {
        context.getSharedPreferences(UserPreference::class.java.name, Context.MODE_PRIVATE).edit().putString("user", Gson().toJson(jUser)).apply()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(UserPreference::class.java.name, Context.MODE_PRIVATE).edit().clear().apply()
        jUser = null
    }


    var name: String? = ""
    var user_id: String? = ""
    var phone: String? = ""
    var countryCode: String? = ""
    var dealerLicenseNumber: String? = ""
    var email: String? = ""
    var photo: String? = ""
    var rating: Double? = 0.0
    var followers: Int? = 0
    var firstDBUpdate: Boolean? = false
    var image: String? = ""
    var accessToken: String? = ""
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
