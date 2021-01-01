package com.itsupportwale.dastaan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.adapters.OnBoardingAdapter
import com.itsupportwale.dastaan.beans.OnBoardingModel
import com.itsupportwale.dastaan.databinding.ActivityOnBoardingBinding


class OnBoardingActivity : AppCompatActivity() {
    lateinit var activityOnBoardingBinding: ActivityOnBoardingBinding
    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>
    private var mAdapter: OnBoardingAdapter? = null
    var previous_pos = 0
    var onBoardItems: ArrayList<OnBoardingModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnBoardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        loadData()
        mAdapter = OnBoardingAdapter(this, onBoardItems)
        activityOnBoardingBinding.pagerIntroduction!!.adapter = mAdapter
        activityOnBoardingBinding.pagerIntroduction!!.currentItem = 0
        activityOnBoardingBinding.pagerIntroduction!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {

                if(position == 2)
                {
                    activityOnBoardingBinding.finish.visibility = View.VISIBLE
                    activityOnBoardingBinding.next.visibility = View.GONE
                }else{
                    activityOnBoardingBinding.finish.visibility = View.GONE
                    activityOnBoardingBinding.next.visibility = View.VISIBLE
                }
                // Change the current position intimation
                for (i in 0 until dotsCount) {
                    dots[i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@OnBoardingActivity,
                            R.drawable.non_selected_item_dot
                        )
                    )
                }
                dots[position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OnBoardingActivity,
                        R.drawable.selected_item_dot
                    )
                )
                val pos = position + 1
                //if (pos == dotsCount && previous_pos == dotsCount - 1) show_animation() else if (pos == dotsCount - 1 && previous_pos == dotsCount) hide_animation()
                previous_pos = pos
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        setUiPageViewController()
    }

    // Load data into the viewpager
    fun loadData() {

        val header = intArrayOf(R.string.ob_header1, R.string.ob_header2, R.string.ob_header3)
        val desc = intArrayOf(R.string.ob_desc1, R.string.ob_desc2, R.string.ob_desc3)
        val imageId = intArrayOf(R.drawable.sp_bg_1, R.drawable.sp_bg_2, R.drawable.sp_bg_3)
        for (i in imageId.indices) {
            val item = OnBoardingModel()
            item.imageID = imageId[i]
            item.title = resources.getString(header[i])
            item.description = resources.getString(desc[i])
            onBoardItems.add(item)
        }

        activityOnBoardingBinding.next.setOnClickListener({
            if( activityOnBoardingBinding.pagerIntroduction.currentItem == 2)
            {
                GoToLogin()
            }else {
                GoToNextPage(activityOnBoardingBinding)
            }
        })

        activityOnBoardingBinding.finish.setOnClickListener({
            GoToLogin()
        })

        activityOnBoardingBinding.skip.setOnClickListener({
            GoToLogin()
        })

    }

    fun GoToLogin()
    {
        val intent = Intent(applicationContext , LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        finish()
    }

    fun GoToNextPage(appInfoViewPagerBinding:ActivityOnBoardingBinding)
    {
        appInfoViewPagerBinding.pagerIntroduction.currentItem = appInfoViewPagerBinding.pagerIntroduction.currentItem+1
    }

    // setup the
    private fun setUiPageViewController() {
        dotsCount = mAdapter!!.getCount()
        dots = arrayOfNulls<ImageView>(dotsCount)
        for (i in 0 until dotsCount) {
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this@OnBoardingActivity,
                    R.drawable.non_selected_item_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(6, 0, 6, 0)
            activityOnBoardingBinding.viewPagerCountDots!!.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                this@OnBoardingActivity,
                R.drawable.selected_item_dot
            )
        )
    }
}