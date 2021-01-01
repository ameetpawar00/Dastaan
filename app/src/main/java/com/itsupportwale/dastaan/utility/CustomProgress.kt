package com.itsupportwale.dastaan.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.widget.TextView
import com.itsupportwale.dastaan.R


class CustomProgress : Dialog {
    private var dialog: CustomProgress? = null

    constructor(context: Context?) : super(context!!) {
        dialog = CustomProgress(context, R.style.CustomProgress)
    }

    constructor(context: Context?, theme: Int) : super(context!!, theme) {}

    override fun onWindowFocusChanged(hasFocus: Boolean) {}
    fun show(context: Context, message: CharSequence?, indeterminate: Boolean, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): CustomProgress {
        if (dialog == null) dialog = CustomProgress(context, R.style.CustomProgress)
        dialog!!.setTitle("")
        dialog!!.setContentView(R.layout.custom_progress)
        val tvMessage = dialog!!.findViewById<TextView>(R.id.message)
        tvMessage.text = message
        dialog!!.setCancelable(cancelable)
        dialog!!.setOnCancelListener(cancelListener)
        if (dialog!!.window != null) {
            dialog!!.window!!.attributes.gravity = Gravity.CENTER
            val lp = dialog!!.window!!.attributes
            lp.dimAmount = 0.2f
            dialog!!.window!!.attributes = lp
        }
        if (!(context as Activity).isFinishing) {
            //show dialog
            dialog!!.show()
        }
        return dialog as CustomProgress
    }

    fun ProgressDismiss() {
        if (dialog != null && dialog!!.isShowing) dialog!!.dismiss()
    }
}
