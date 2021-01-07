package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseMyStory {
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
        @SerializedName("story")
        @Expose
        var story: List<Story>? = null

    }

    class Story {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("content")
        @Expose
        var content: String? = null

        @SerializedName("story_tags")
        @Expose
        var storyTags: String? = null

        @SerializedName("genre")
        @Expose
        var genre: String? = null

        @SerializedName("likes")
        @Expose
        var likes: String? = null

        @SerializedName("ratings")
        @Expose
        var ratings: String? = null

        @SerializedName("view")
        @Expose
        var view: String? = null

        @SerializedName("photo")
        @Expose
        var photo: List<String>? = null

        @SerializedName("timestamp")
        @Expose
        var timestamp: String? = null

    }
}
