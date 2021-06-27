package com.itsupportwale.dastaan.activity

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.databinding.ActivityMainBinding
import com.itsupportwale.dastaan.fragment.FavouritesFragment
import com.itsupportwale.dastaan.fragment.HomeFragment
import com.itsupportwale.dastaan.fragment.ProfileFragment
import com.itsupportwale.dastaan.fragment.SettingsFragment
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_USER_ID
import com.itsupportwale.dastaan.utility.*


class MainActivity : BaseActivity(), View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private var userPreference: UserPreference? = null
    lateinit var activityMainBinding: ActivityMainBinding
    var userId = 0
    val gson: Gson = Gson()

    lateinit var drawer: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        userPreference = UserPreference.getInstance(this);
        init()
        initDrawerView()
    }

    private fun initDrawerView() {
        activityMainBinding.topNavBar.editBtn.setOnClickListener(this)
        activityMainBinding.topNavBar.menuIcon.setOnClickListener { v ->
            // open right drawer
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else drawer.openDrawer(GravityCompat.START)
        }
        activityMainBinding.topNavBar.notificationIcon.setOnClickListener { v ->
            if(userPreference!!.email!=null && userPreference!!.email!="")
            {
                var intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                drawer.closeDrawer(GravityCompat.START);
            }else{
                gotoNextActivity()
            }


        }
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            R.string.empty_str,
            R.string.empty_str
        )
        toggle.isDrawerIndicatorEnabled = false
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        activityMainBinding.navigationView.setNavigationItemSelectedListener(this)
    }


    private fun init() {

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mCounterReceiver,
            IntentFilter("counter-to-be-updated")
        )
        drawer = findViewById(R.id.drawer_layout)

        activityMainBinding.tabBarAddCard.setOnClickListener(this)
        activityMainBinding.tabBarHomeCard.setOnClickListener(this)
        activityMainBinding.tabBarFavCard.setOnClickListener(this)
        activityMainBinding.tabBarProfileCard.setOnClickListener(this)
        activityMainBinding.tabBarSettingCard.setOnClickListener(this)

        activityMainBinding.homeLinLay.setOnClickListener(this)
        activityMainBinding.favListLinLay.setOnClickListener(this)
        activityMainBinding.userProfileLinLay.setOnClickListener(this)
        activityMainBinding.notificationLinLay.setOnClickListener(this)
        activityMainBinding.settingsLinLay.setOnClickListener(this)
        activityMainBinding.userProfileLinLay.setOnClickListener(this)

        if (intent.hasExtra(PARAM_COMING_FROM)) {

            if (intent.hasExtra(PARAM_USER_ID)) {
                userId = intent.getIntExtra(PARAM_USER_ID, 0)
            }

            var commingFrom = intent.getIntExtra(PARAM_COMING_FROM, 1)
            changeTab(commingFrom)



        } else {
            changeTab(TAB_BAR_HOME)
        }
    }

    private val mCounterReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val type = intent.getStringExtra("type")
            val counter = intent.getIntExtra("count", 0)
            Log.d("broadcastReceiver", "$type--*--$counter--*--")
            if (counter != null && !counter.equals("") && counter != 0) {
                if (type == "1") {
                    activityMainBinding.topNavBar.notificationCount.setVisibility(View.VISIBLE)
                    activityMainBinding.topNavBar.notificationCount.setText(counter.toString())
                } else {
                    activityMainBinding.topNavBar.notificationCount.setVisibility(View.VISIBLE)
                    activityMainBinding.topNavBar.notificationCount.setText(counter.toString())
                }
            }
        }
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.homeLinLay -> {
                changeTab(TAB_BAR_HOME)
                drawer.closeDrawer(GravityCompat.START);
            }
            R.id.favListLinLay -> {
                if(userPreference!!.email!=null && userPreference!!.email!="")
                {
                    changeTab(TAB_BAR_FAV)
                    drawer.closeDrawer(GravityCompat.START);
                }else{
                    gotoNextActivity()
                }
            }
            R.id.userProfileLinLay -> {
                if(userPreference!!.email!=null && userPreference!!.email!="")
                {
                    changeTab(TAB_BAR_PROFILE)
                    drawer.closeDrawer(GravityCompat.START);
                }else{
                    gotoNextActivity()
                }
            }
            R.id.notificationLinLay -> {
                if(userPreference!!.email!=null && userPreference!!.email!="")
                {
                    var intent = Intent(this, NotificationActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                    drawer.closeDrawer(GravityCompat.START);
                }else{
                    gotoNextActivity()
                }

            }
            R.id.editBtn -> {
                if(userPreference!!.email!=null && userPreference!!.email!="")
                {
                    var intent = Intent(this, EditUserProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                    drawer.closeDrawer(GravityCompat.START);
                }else{
                    gotoNextActivity()
                }

            }
            R.id.settingsLinLay -> {
                changeTab(TAB_BAR_SETTING)
                drawer.closeDrawer(GravityCompat.START);
            }
            R.id.tabBarHomeCard -> {
                changeTab(TAB_BAR_HOME)
            }
            R.id.tabBarFavCard -> {
                if(userPreference!!.email!=null && userPreference!!.email!="")
                {
                    changeTab(TAB_BAR_FAV)
                }else{
                    gotoNextActivity()
                }

            }
            R.id.tabBarProfileCard -> {
                if(userPreference!!.email!=null && userPreference!!.email!="")
                {
                    changeTab(TAB_BAR_PROFILE)
                }else{
                    gotoNextActivity()
                }

            }
            R.id.tabBarSettingCard -> {
                changeTab(TAB_BAR_SETTING)
            }
            R.id.tabBarAddCard -> {
                if(userPreference!!.email!=null && userPreference!!.email!="")
                {
                    changeTab(TAB_BAR_ADD)
                }else{
                    gotoNextActivity()
                }

            }
        }
    }

    private fun gotoNextActivity() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Login For this Feature")
        builder.setMessage("Please Login to Continue. . . .")

        builder.setPositiveButton(
            "Login",
            DialogInterface.OnClickListener { dialog, which -> // Do nothing but close the dialog
                val intent = Intent(applicationContext , LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            })

        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> // Do nothing
                dialog.dismiss()
            })

        val alert: AlertDialog = builder.create()
        alert.show()

    }

    private fun changeTab(i: Int) {
        when (i) {
            TAB_BAR_HOME -> { // HOME Tab
                activityMainBinding.topNavBar.editBtn.visibility = View.GONE
                activityMainBinding.topNavBar.navTitle.text = getString(R.string.home)
                activityMainBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
                activityMainBinding.topNavBar.notificationIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                activityMainBinding.topNavBar.menuIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )


                setSelected(
                    activityMainBinding.tabBarImageHome,
                    activityMainBinding.tabBarActHome
                )
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment: Fragment
                fragment = HomeFragment()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_from_left,
                    R.anim.slide_to_right
                );
                fragmentTransaction.replace(R.id.container, fragment, "Home")
                fragmentTransaction.commitAllowingStateLoss()
            }
            TAB_BAR_FAV -> { // FAV Tab
                activityMainBinding.topNavBar.editBtn.visibility = View.GONE
                activityMainBinding.topNavBar.navTitle.text = getString(R.string.favorite)
                activityMainBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
                activityMainBinding.topNavBar.notificationIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                activityMainBinding.topNavBar.menuIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )

                setSelected(
                    activityMainBinding.tabBarImageFav,
                    activityMainBinding.tabBarActFav
                )
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment: Fragment
                fragment = FavouritesFragment()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_from_left,
                    R.anim.slide_to_right
                );
                fragmentTransaction.replace(R.id.container, fragment, "Favorite")
                fragmentTransaction.commitAllowingStateLoss()
            }
            TAB_BAR_ADD -> { // FAV Tab

                var intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                drawer.closeDrawer(GravityCompat.START);
            }
            TAB_BAR_PROFILE -> { // FAV Tab
                activityMainBinding.topNavBar.notificationCount.visibility = View.GONE
                activityMainBinding.topNavBar.notificationIcon.visibility = View.GONE
                activityMainBinding.topNavBar.navTitle.text = getString(R.string.profile)
                activityMainBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.white))
                activityMainBinding.topNavBar.notificationIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                activityMainBinding.topNavBar.menuIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )

                activityMainBinding.topNavBar.notificationIcon.visibility = View.GONE
                activityMainBinding.topNavBar.notificationCount.visibility = View.GONE



                if(userId == 0)
                {
                    activityMainBinding.topNavBar.editBtn.visibility = View.VISIBLE
                }else{
                    activityMainBinding.topNavBar.editBtn.visibility = View.GONE
                }
                //activityMainBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
                //activityMainBinding.topNavBar.notificationIcon.setColorFilter(ContextCompat.getColor(this, R.color.black))
                //activityMainBinding.topNavBar.menuIcon.setColorFilter(ContextCompat.getColor(this, R.color.black))

                setSelected(
                    activityMainBinding.tabBarImageProfile,
                    activityMainBinding.tabBarActProfile
                )
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment: Fragment
                fragment = ProfileFragment()
                val args = Bundle()
                args.putInt(PARAM_USER_ID, userId)
                fragment.setArguments(args)
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_from_left,
                    R.anim.slide_to_right
                );
                fragmentTransaction.replace(R.id.container, fragment, "Profile")
                fragmentTransaction.commitAllowingStateLoss()
            }
            TAB_BAR_SETTING -> { // FAV Tab
                activityMainBinding.topNavBar.editBtn.visibility = View.GONE
                activityMainBinding.topNavBar.navTitle.text = getString(R.string.settings)
                activityMainBinding.topNavBar.navTitle.setTextColor(resources.getColor(R.color.black))
                activityMainBinding.topNavBar.notificationIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                activityMainBinding.topNavBar.menuIcon.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )

                setSelected(
                    activityMainBinding.tabBarImageSetting,
                    activityMainBinding.tabBarActSetting
                )
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment: Fragment
                fragment = SettingsFragment()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_from_left,
                    R.anim.slide_to_right
                );
                fragmentTransaction.replace(R.id.container, fragment, "Settings")
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    private fun setSelected(tabImg: ImageView, tabImgAct: ImageView) {

        activityMainBinding.tabBarImageHome.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        activityMainBinding.tabBarActHome.visibility = View.INVISIBLE

        activityMainBinding.tabBarImageFav.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        activityMainBinding.tabBarActFav.visibility = View.INVISIBLE

        activityMainBinding.tabBarImageAdd.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        activityMainBinding.tabBarActAdd.visibility = View.INVISIBLE

        activityMainBinding.tabBarImageProfile.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        activityMainBinding.tabBarActProfile.visibility = View.INVISIBLE

        activityMainBinding.tabBarImageSetting.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        activityMainBinding.tabBarActSetting.visibility = View.INVISIBLE

        // set selected
        tabImg.setColorFilter(ContextCompat.getColor(this, R.color.white))
        tabImgAct.visibility = View.VISIBLE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        activityMainBinding.userName.text = userPreference!!.name
        activityMainBinding.userEmail.text = userPreference!!.email

        Glide.with(this).asBitmap()
            .load(userPreference!!.photo)
            .error(R.drawable.default_image_icon)
            .into(activityMainBinding.userImage)


    }
    override fun onDestroy() {
        super.onDestroy()
    }
}