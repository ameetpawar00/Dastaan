package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetFavData {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<Datum>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("approve_status")
        @Expose
        var approveStatus: String? = null

        @SerializedName("address")
        @Expose
        var address: String? = null

        @SerializedName("country")
        @Expose
        var country: String? = null

        @SerializedName("state")
        @Expose
        var state: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("cityName")
        @Expose
        var cityName: String? = null

        @SerializedName("landmark")
        @Expose
        var landmark: String? = null

        @SerializedName("zip")
        @Expose
        var zip: String? = null

        @SerializedName("latitude")
        @Expose
        var latitude: String? = null

        @SerializedName("longitude")
        @Expose
        var longitude: String? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("features")
        @Expose
        var features: String? = null

        @SerializedName("property_size")
        @Expose
        var propertySize: String? = null

        @SerializedName("kitchen")
        @Expose
        var kitchen: String? = null

        @SerializedName("bathroom")
        @Expose
        var bathroom: String? = null

        @SerializedName("bedroom")
        @Expose
        var bedroom: String? = null

        @SerializedName("parking")
        @Expose
        var parking: String? = null

        @SerializedName("year_build")
        @Expose
        var yearBuild: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("photo")
        @Expose
        var photo: List<String>? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("owner")
        @Expose
        var owner: String? = null

        @SerializedName("rating")
        @Expose
        var rating: String? = null

        @SerializedName("view")
        @Expose
        var view: String? = null

        @SerializedName("timestamp")
        @Expose
        var timestamp: String? = null
    }

}