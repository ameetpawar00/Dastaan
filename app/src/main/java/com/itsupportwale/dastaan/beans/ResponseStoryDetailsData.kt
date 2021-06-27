package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseStoryDetailsData {
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

        @SerializedName("genre_id")
        @Expose
        var genreId: Int? = null

        @SerializedName("likes")
        @Expose
        var likes: String? = null

        @SerializedName("identity_status")
        @Expose
        var identityStatus: String? = null



        @SerializedName("photo")
        @Expose
        var photo: ArrayList<String>? = ArrayList()

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("writer")
        @Expose
        var writer: String? = null

        @SerializedName("view")
        @Expose
        var view: String? = null

        @SerializedName("timestamp")
        @Expose
        var timestamp: String? = null

        @SerializedName("writer_data")
        @Expose
        var writerData: WriterData? = null

        @SerializedName("is_favourite")
        @Expose
        var isFavourite: Boolean? = null

        @SerializedName("is_following")
        @Expose
        var isFollowing: Boolean? = null

        @SerializedName("rating")
        @Expose
        var rating: Double? = null

        @SerializedName("rating_data")
        @Expose
        val ratingData: ArrayList<RatingDatum>? = ArrayList()

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

    class RatingDatum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("review")
        @Expose
        var review: String? = null

        @SerializedName("rating")
        @Expose
        var rating: Float? = null

        @SerializedName("story_id")
        @Expose
        var storyId: Int? = null

        @SerializedName("reviewer_id")
        @Expose
        var reviewerId: Int? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("timestamp")
        @Expose
        var timestamp: String? = null
    }

}