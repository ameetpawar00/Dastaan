package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ResponseUpdateStoryData {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

}