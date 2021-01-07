package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger


class ResponseHomeData {
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
        @SerializedName("genre")
        @Expose
        var genre: List<Genre>? = null

        @SerializedName("story")
        @Expose
        var story: List<Story>? = null

        @SerializedName("my_subscription")
        @Expose
        var mySubscription: List<MySubscription>? = null

    }

    class Genre {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("story_count")
        @Expose
        var storyCount: Int? = null

        var isSelected: Boolean? = false

    }

    class Story {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("approve_status")
        @Expose
        var approveStatus: String? = null

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

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("photo")
        @Expose
        var photo: List<String>? = null

        @SerializedName("timestamp")
        @Expose
        var timestamp: String? = null

        @SerializedName("writer")
        @Expose
        var writer: String? = null

        @SerializedName("writer_data")
        @Expose
        var writerData: WriterData? = null

        @SerializedName("is_favourite")
        @Expose
        var isFavourite: Boolean? = null

    }

    class MySubscription {
        @SerializedName("id")
        @Expose
        var id: BigInteger? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("approve_status")
        @Expose
        var approveStatus: String? = null

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

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("photo")
        @Expose
        var photo: List<String>? = null

        @SerializedName("timestamp")
        @Expose
        var timestamp: String? = null

        @SerializedName("writer")
        @Expose
        var writer: String? = null

        @SerializedName("writer_data")
        @Expose
        var writerData: WriterData? = null

        @SerializedName("is_favourite")
        @Expose
        var isFavourite: Boolean? = null

    }

    class WriterData {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

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

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("timestamp")
        @Expose
        var timestamp: String? = null

    }

}
