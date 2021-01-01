package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ResponseForgotPasswordData {
    @SerializedName("status")
    @Expose
    var status: Boolean? = false

    @SerializedName("message")
    @Expose
    var message: String? = ""
}