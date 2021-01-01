package com.itsupportwale.dastaan.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.beans.ResponseGetLocationData
import com.itsupportwale.dastaan.databinding.ActivitySplashScreenBinding
import com.itsupportwale.dastaan.databinding.ActivityWelcomeBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.METHOD_NAME
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*

class SplashScreenActivity : BaseActivity(), View.OnClickListener{

    private val SPLASH_TIME_OUT = 2000
    private var userPreference: UserPreference? = null
    lateinit var activitySplashScreenBinding: ActivitySplashScreenBinding

    val gson: Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        userPreference = UserPreference.getInstance(getApplicationContext());

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

        splashUsingHandler()
    }
    private fun splashUsingHandler() {
        Handler().postDelayed({
            if (userPreference!!.loginStatus != LOGIN_CHECK) {
                val intent = Intent(applicationContext, OnBoardingActivity::class.java)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIME_OUT.toLong())
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}