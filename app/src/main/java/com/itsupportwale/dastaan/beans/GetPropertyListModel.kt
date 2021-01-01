package com.itsupportwale.dastaan.beans


import com.google.gson.annotations.SerializedName


class GetPropertyListModel {

    @SerializedName("status")
    var status: Boolean? = false
    @SerializedName("data")
     var data: List<Datum>? = null
    @SerializedName("message")
     var message: String? = ""


     class Datum {

        @SerializedName("id")
         var id: String? = ""
        @SerializedName("title")
         var title: String? = ""
        @SerializedName("approve_status")
         var approveStatus: String? = ""
        @SerializedName("address")
         var address: String? = ""
        @SerializedName("country")
         var country: String? = ""
        @SerializedName("state")
         var state: String? = ""
        @SerializedName("city")
         var city: String? = ""
        @SerializedName("landmark")
         var landmark: String? = ""
        @SerializedName("zip")
         var zip: String? = ""
        @SerializedName("latitude")
         var latitude: Double? = 0.0
        @SerializedName("longitude")
         var longitude: Double? = 0.0
        @SerializedName("type")
         var type: String? = ""
        @SerializedName("price")
         var price: String? = ""
        @SerializedName("features")
         var features: String? = ""
        @SerializedName("property_size")
         var propertySize: String? = ""
        @SerializedName("kitchen")
         var kitchen: String? = ""
        @SerializedName("bathroom")
         var bathroom: String? = ""
        @SerializedName("bedroom")
         var bedroom: String? = ""
        @SerializedName("parking")
         var parking: String? = ""
        @SerializedName("year_build")
         var yearBuild: String? = ""
        @SerializedName("description")
         var description: String? = ""
        @SerializedName("photo")
         var photo: List<String>? = null
        @SerializedName("status")
         var status: String? = ""
        @SerializedName("timestamp")
         var timestamp: String? = ""
        @SerializedName("rating")
         var rating: String? = ""
        @SerializedName("owner")
         var owner: String? = ""
        @SerializedName("owner_data")
         var ownerData: OwnerData? = null
        @SerializedName("is_favourite")
         var isFavourite: Boolean? = false

         @SerializedName("city_name")
         var cityName: String? = ""

    }
    public class OwnerData {
        @SerializedName("id")
         var id: String? = ""
        @SerializedName("name")
         var name: String? = ""
        @SerializedName("phone")
         var phone: String? = ""
        @SerializedName("country_code")
         var countryCode: String? = ""
        @SerializedName("email")
         var email: String? = ""
        @SerializedName("password")
         var password: String? = ""
        @SerializedName("photo")
         var photo: String? = ""
        @SerializedName("country")
         var country: String? = ""
        @SerializedName("state")
         var state: String? = ""
        @SerializedName("city")
         var city: String? = ""
        @SerializedName("latitude")
         var latitude: String? = ""
        @SerializedName("longitude")
         var longitude: String? = ""
        @SerializedName("zip")
         var zip: String? = ""
        @SerializedName("address")
         var address: String? = ""
        @SerializedName("device_token")
         var deviceToken: String? = ""
        @SerializedName("status")
         var status: String? = ""
        @SerializedName("timestamp")
         var timestamp: String? = ""

    }


}