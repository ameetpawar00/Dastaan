package com.itsupportwale.dastaan.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResponseGetLocationData {
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
        @SerializedName("country")
        @Expose
        var country: List<Country>? = null
    }

    class Country {
        @SerializedName("id")
        @Expose
        var id: String? = ""

        @SerializedName("name")
        @Expose
        var name: String? = ""

        @SerializedName("country_code")
        @Expose
        var countryCode: String? = ""

        @SerializedName("regions")
        @Expose
        var regions: List<Region>? = null
    }

    class Region {
        @SerializedName("id")
        @Expose
        var id: String? = ""

        @SerializedName("name")
        @Expose
        var name: String? = ""

        @SerializedName("cities")
        @Expose
        var cities: List<City>? = null
    }

    class City {
        @SerializedName("id")
        @Expose
        var id: String? = ""

        @SerializedName("name")
        @Expose
        var name: String? = ""
    }
}