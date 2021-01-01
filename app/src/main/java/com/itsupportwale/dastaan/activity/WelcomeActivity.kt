package com.itsupportwale.dastaan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    lateinit var activityWelcomeBinding: ActivityWelcomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWelcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        initView()
    }
    fun initView()
    {
        activityWelcomeBinding.currentLocationButton.setOnClickListener{
            val intent = Intent(applicationContext , MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }
    }
}