package com.itsupportwale.dastaan.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.activity.SplashScreenActivity
import com.itsupportwale.dastaan.databinding.FragmentSettingsBinding
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.ShareData
import com.itsupportwale.dastaan.utility.UserPreference


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : BaseFragment(), FragmentBaseListener, View.OnClickListener {

    lateinit var fragmentSettingsBinding : FragmentSettingsBinding
    val gson: Gson = Gson()
    var rootView: View? = null
    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingsBinding = DataBindingUtil.inflate(
            inflater!!,
            R.layout.fragment_settings,
            container,
            false
        )
        val view: View = fragmentSettingsBinding.getRoot()
        setOnFragmentListener(this)
        initView(view)
        return view
    }
    private fun initView(view: View) {

        fragmentSettingsBinding.logoutBtn.setOnClickListener(this)
        fragmentSettingsBinding.termsBtn.setOnClickListener(this)
        fragmentSettingsBinding.privacyBtn.setOnClickListener(this)
        fragmentSettingsBinding.sendFeedbackBtn.setOnClickListener(this)
        fragmentSettingsBinding.shareUsBtn.setOnClickListener(this)
        fragmentSettingsBinding.rateUsBtn.setOnClickListener(this)

        rootView = view

      //  val postRequestModel = PostRequestModel()
     //   showLoader(requireActivity().resources.getString(R.string.please_wait))
    //    var commonModel = CommonValueModel()
    //    postApiCall(requireActivity(), UrlManager.GET_CAR_LIST_ALL, postRequestModel, commonModel)
    }

    override fun onClick(view: View) {
        when (view?.id) {
            R.id.logoutBtn -> {
                LogoutBtn()
            }
            R.id.termsBtn -> {
                val uri =
                    Uri.parse("https://dastaan.life/terms.php") // missing 'http://' will cause crashed

                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            R.id.privacyBtn -> {
                val uri =
                    Uri.parse("https://dastaan.life/policy.php") // missing 'http://' will cause crashed

                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            R.id.sendFeedbackBtn -> {
                sendFeedback(requireActivity())
            }
            R.id.shareUsBtn -> {
                shareUs()
            }
            R.id.rateUsBtn -> {
                rateUs()
            }
        }
    }


    fun rateUs() {
        val uri =
            Uri.parse("market://details?id=" + requireActivity().getPackageName())
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + requireActivity().getPackageName())
                )
            )
        }
    }

    private fun shareUs() {
        var screenshot= drawableToBitmap(requireActivity().getDrawable(R.drawable.splash)!!)
        val shareData = ShareData(
            screenshot, requireActivity(),requireActivity()
        )
        shareData.shareScreenshots("")
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }
    fun sendFeedback(
        activity: Activity
    ) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        var deviceInfo = "Device Info:"
        try {
            val android_id = Settings.Secure.getString(
                activity.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val versionName =
                activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
            deviceInfo += "<br> Android Id: $android_id"
            deviceInfo += "<br> Android Version: $versionName"
            deviceInfo += "<br> OS Version: " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")"
            deviceInfo += "<br> OS API Level: " + Build.VERSION.SDK_INT
            deviceInfo += "<br> Device: " + Build.DEVICE
            deviceInfo += "<br> Model (and Product): " + Build.MODEL + " (" + Build.PRODUCT + ")"
            deviceInfo += "<br>------ Please don't edit anything above this line, to help us serve you better ------<br><br><br>"
        } catch (e: PackageManager.NameNotFoundException) {
        }
        val aEmailList =
            arrayOf("contact@itsupportwale.in")
        emailIntent.data = Uri.parse("mailto:") // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, aEmailList)
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            Html.fromHtml("<i><font color='your color'>$deviceInfo</font></i>")
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback : Dastaan")
        val packageManager = activity.packageManager
        val isIntentSafe = emailIntent.resolveActivity(packageManager) != null
        if (isIntentSafe) {
            startActivity(emailIntent)
        } else {
            Toast.makeText(activity, "Email App Not Installed", Toast.LENGTH_SHORT).show()
        }
    }


    open fun LogoutBtn() {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.menu_logout))
            .setMessage(getString(R.string.logout_msg))
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                var userPreference: UserPreference? = UserPreference.getInstance(requireActivity());
                userPreference!!.loginStatus = 0
                val intent: Intent = Intent(
                    requireActivity(),
                    SplashScreenActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            .setNegativeButton(
                android.R.string.no
            ) { dialog, which ->
                // do nothing
            } //  .setIcon(R.drawable.ic_logout)
            .show()
    }

    override fun onFragmentApiSuccess(
        result: String?,
        apiName: String?,
        commonModel: CommonValueModel?
    ) {

    }

    override fun onFragmentApiFailure(message: String?, apiName: String?) {

    }

    override fun onReadWriteStoragePermissionAllow(medialTypes: String?) {

    }

    override fun onReadWriteStoragePermissionDeny(medialTypes: String?) {

    }
}