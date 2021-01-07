package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseMyProfile {
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
        @SerializedName("user")
        @Expose
        var user: User? = null

    }

    class User {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("photo")
        @Expose
        var photo: String? = null

        @SerializedName("followers")
        @Expose
        var followers: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = null

    }
}