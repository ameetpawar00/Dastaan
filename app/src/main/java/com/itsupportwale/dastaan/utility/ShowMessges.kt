package com.itsupportwale.dastaan.utility

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar

class ShowMessges
{
    lateinit var activity: Activity

    constructor(activity: Activity)
    {
        this.activity = activity
    }

    fun showErrorMessage(layout: View, msg:String)
    {
        Snackbar.make(layout, msg, Snackbar.LENGTH_LONG).show()
    }
}