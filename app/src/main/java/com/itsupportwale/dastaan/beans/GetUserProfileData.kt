package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetUserProfileData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

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
        @SerializedName("my_stories")
        @Expose
        var myStories: List<MyStories>? = null

        @SerializedName("user")
        @Expose
        var user: User? = null

    }
    class MyStories {
        @SerializedName("id")
        @Expose
        var id: String? = null

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

        @SerializedName("photo")
        @Expose
        var photo: List<String>? = null

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

        @SerializedName("follower_count")
        @Expose
        var followerCount: Int? = null

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