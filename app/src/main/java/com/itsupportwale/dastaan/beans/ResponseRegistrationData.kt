package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseRegistrationData {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    class Data {
        @SerializedName("photo")
        @Expose
        var photo: Any? = null

        @SerializedName("thisOtp")
        @Expose
        var thisOtp: String? = null

        @SerializedName("country_code")
        @Expose
        var countryCode: String? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("followers")
        @Expose
        var followers: Int? = null

        @SerializedName("rating")
        @Expose
        var rating: Double? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("otp")
        @Expose
        var otp: String? = null

    }

}