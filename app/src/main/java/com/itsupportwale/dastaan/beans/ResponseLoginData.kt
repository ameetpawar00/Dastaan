package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseLoginData {
    @SerializedName("status")
    @Expose
    var status: Boolean? = false

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("message")
    @Expose
    var message: String? = ""

    class Data {
        @SerializedName("user")
        @Expose
        var user: User? = null

        @SerializedName("extra")
        @Expose
        var extra: String? = ""
    }

    class User {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = ""

        @SerializedName("phone")
        @Expose
        var phone: String? = ""

        @SerializedName("country_code")
        @Expose
        var countryCode: String? = ""

        @SerializedName("email")
        @Expose
        var email: String? = ""

        @SerializedName("rating")
        @Expose
        var rating: Double? = 0.0

        @SerializedName("followers")
        @Expose
        var followers: Int? = 0

        @SerializedName("photo")
        @Expose
        var photo: String? = ""

        @SerializedName("country")
        @Expose
        var country: String? = ""

        @SerializedName("state")
        @Expose
        var state: String? = ""

        @SerializedName("city")
        @Expose
        var city: String? = ""

        @SerializedName("latitude")
        @Expose
        var latitude: String? = ""

        @SerializedName("longitude")
        @Expose
        var longitude: String? = ""

        @SerializedName("zip")
        @Expose
        var zip: String? = ""

        @SerializedName("address")
        @Expose
        var address: String? = ""

        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = ""
    }
}