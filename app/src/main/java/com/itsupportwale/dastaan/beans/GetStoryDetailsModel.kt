package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetStoryDetailsModel {

    @SerializedName("status")
    @Expose
    var status: Boolean? = false

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("message")
    @Expose
    var message: String? = ""


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
         var type: Int? = null

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

        @SerializedName("owner_data")
        @Expose
         var ownerData: OwnerData? = null

        @SerializedName("is_favourite")
        @Expose
         var isFavourite: Boolean? = null

        @SerializedName("rating_data")
        @Expose
         var ratingData: ArrayList<RatingDatum>? = null
        @SerializedName("city_name")
        var cityName: String? = ""
        @SerializedName("region_name")
        var regionName: String? = ""
        @SerializedName("country_name")
        var countryName: String? = ""
    }

     class OwnerData {
         @SerializedName("id")
         @Expose
          var id: Int? = null

         @SerializedName("name")
         @Expose
          var name: String? = null

         @SerializedName("phone")
         @Expose
          var phone: String? = null

         @SerializedName("country_code")
         @Expose
          var countryCode: String? = null

         @SerializedName("email")
         @Expose
          var email: String? = null

         @SerializedName("password")
         @Expose
          var password: String? = null

         @SerializedName("photo")
         @Expose
          var photo: String? = null

         @SerializedName("country")
         @Expose
          var country: String? = null

         @SerializedName("state")
         @Expose
          var state: String? = null

         @SerializedName("city")
         @Expose
          var city: String? = null

         @SerializedName("latitude")
         @Expose
          var latitude: String? = null

         @SerializedName("longitude")
         @Expose
          var longitude: String? = null

         @SerializedName("zip")
         @Expose
          var zip: String? = null

         @SerializedName("address")
         @Expose
          var address: String? = null

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

        @SerializedName("property_id")
        @Expose
         var propertyId: Int? = null

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