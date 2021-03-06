package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class GetNotificationData {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    class Datum {
        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("body")
        @Expose
        var body: String? = null

        @SerializedName("click_action")
        @Expose
        var clickAction: String? = null
    }
}