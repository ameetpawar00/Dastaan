package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ResponseGenreData {
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
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("story_count")
        @Expose
        var storyCount: String? = null

    }
}