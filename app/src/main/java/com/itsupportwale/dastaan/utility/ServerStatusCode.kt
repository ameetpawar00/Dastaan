package com.itsupportwale.dastaan.utility

import org.json.JSONException
import org.json.JSONObject


class ServerStatusCode {



    @Throws(JSONException::class)
    fun getStatusCodeMessage(statusName: Int): String? {
        var msg = ""
      //  val jsonObject = JSONObject()
        when (statusName) {
            100 -> msg = "Unexpected Error!"
            101 -> msg = "Unexpected Error!"
            200 -> { }
            201 -> msg = "Unexpected Error!"
            202 -> msg = "Unexpected Error!"
            203 -> msg = "Unexpected Error!"
            204 -> msg = "Unexpected Error!"
            205 -> msg = "Unexpected Error!"
            206 -> msg = "Unexpected Error!"
            300 -> msg = "Unexpected Error!"
            301 -> msg = "Unexpected Error!"
            302 -> msg = "Unexpected Error!"
            303 -> msg = "Unexpected Error!"
            304 -> msg = "Unexpected Error!"
            305 -> msg = "Unexpected Error!"
            307 -> msg = "Unexpected Error!"
            400 -> msg = "Bad Request, Please try again"
            401 -> msg = "Unauthorized"
            402 -> msg = "Payment Required"
            403 -> msg = "Access Denied You don't have permission to access"
            404 -> msg = "Not Found"
            405 -> msg = "Method Not Allowed"
            406 -> msg = "Unexpected Error!"
            407 -> msg = "Unexpected Error!"
            408 -> msg = "Unexpected Error!"
            409 -> msg = "Unexpected Error!"
            410 -> msg = "Unexpected Error!"
            411 -> msg = "Unexpected Error!"
            412 -> msg = "Unexpected Error!"
            413 -> msg = "Unexpected Error!"
            414 -> msg = "Unexpected Error!"
            415 -> msg = "Unexpected Error!"
            416 -> msg = "Unexpected Error!"
            417 -> msg = "Unexpected Error!"
            500 -> msg = "Internal Server Error, Please try again"
            501 -> msg = "Not Implemented"
            503 -> msg = "Bad Gateway"
            504 -> msg = "Service Unavailable"
            505 -> msg = "HTTP Version Not Supported"
            else -> msg = "Unexpected Error!"
        }
      //  jsonObject.put("statuscode", statusName)
        //jsonObject.put("message", msg)
        return msg
    }


}