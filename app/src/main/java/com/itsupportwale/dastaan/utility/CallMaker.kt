package com.itsupportwale.dastaan.utility

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.itsupportwale.dastaan.interfaces.AppPermissionListener

class CallMaker {

    lateinit var activity: Activity

    constructor(activity: Activity)
    {
        this.activity = activity
    }

    fun call(mobileNo:String) {
        var permissionList = ArrayList<String>()
        permissionList.add(Manifest.permission.CALL_PHONE)

        UserPermision.requestForCheckedAndPermission(activity, permissionList, object :
            AppPermissionListener {
            override fun OnAllPermissionsGranted(status: Boolean) {

                if (UserPermision.checkPermission(
                        activity,
                        permissionList
                    )
                ) {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" +mobileNo)
                    activity.startActivity(callIntent)
                }
            }
        })
    }
}