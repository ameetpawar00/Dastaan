package com.itsupportwale.dastaan.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.adapters.StoryPhotoAdapter
import com.itsupportwale.dastaan.adapters.StoryRatingAdapter
import com.itsupportwale.dastaan.beans.ResponseStoryDetailsData
import com.itsupportwale.dastaan.beans.ResponseUpdateBookmarkData
import com.itsupportwale.dastaan.beans.ResponseUpdateFollowing
import com.itsupportwale.dastaan.databinding.ActivityStoryDetailsBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.METHOD_NAME
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_USER_ID
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import com.loopj.android.http.RequestParams
import java.io.File
import java.io.FileOutputStream
import java.lang.Integer.parseInt


class StoryDetailsActivity : BaseActivity(), View.OnClickListener, StoryPhotoAdapter.onRecyclerViewItemClickListener{
    lateinit var activityStoryDetailsBinding: ActivityStoryDetailsBinding
    lateinit var parentPanel:LinearLayout
    private val storyPhotoArray: ArrayList<String> = ArrayList()
    lateinit var storyPhotoAdapter : StoryPhotoAdapter
    private val storyRatingArray: ArrayList<ResponseStoryDetailsData.RatingDatum> = ArrayList()
    lateinit var storyRatingAdapter : StoryRatingAdapter
    private var userPreference: UserPreference? = null
    private lateinit var adView: AdView
    var storyId: Int=0
    var writerId: Int=0
    var rating = 0.0

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStoryDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_story_details
        )
        userPreference = UserPreference.getInstance(this)

        val pullToRefresh: SwipeRefreshLayout = activityStoryDetailsBinding.pullToRefresh
        pullToRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                initView()
                pullToRefresh.setRefreshing(false)
            }
        })

        MobileAds.initialize(
            this
        ) { }
        adView = findViewById(R.id.ad_view)
        val adRequest =
            AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        //
        //
        adView.loadAd(adRequest)
        adView.setAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                Toast.makeText(this@StoryDetailsActivity, "bottom Ad failed: $errorCode", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4905384164104793/7198617950"

        val adRequestInterstitial =
            AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()

        mInterstitialAd.loadAd(adRequestInterstitial)

        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Toast.makeText(this@StoryDetailsActivity, "Full Ad failed: "+adError, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                finish()
            }

            override fun onAdLeftApplication() {
                finish()
            }

            override fun onAdClosed() {
                finish()
            }
        }

        initView()
    }

    override fun onResume() {
        super.onResume()
        getStoryDetails()
    }

    override fun onBackPressed() {
        super.onBackPressed()
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }


    }


    fun initView()
    {
        activityStoryDetailsBinding.shareBtn.setOnClickListener(this)
        activityStoryDetailsBinding.ratingLinLay.setOnClickListener(this)
        activityStoryDetailsBinding.userLinLay.setOnClickListener(this)
        activityStoryDetailsBinding.followBtn.setOnClickListener(this)
        activityStoryDetailsBinding.followingBtn.setOnClickListener(this)
        activityStoryDetailsBinding.favoritesLinLay.setOnClickListener(this)
        activityStoryDetailsBinding.thisEditBtn.setOnClickListener(this)
        activityStoryDetailsBinding.topNavBar.notificationIcon.visibility = View.GONE
        activityStoryDetailsBinding.topNavBar.notificationCount.visibility = View.GONE
        activityStoryDetailsBinding.topNavBar.menuIcon.visibility = View.GONE
        activityStoryDetailsBinding.topNavBar.backIcon.visibility = View.VISIBLE
        activityStoryDetailsBinding.topNavBar.backIcon.setOnClickListener(this)
        activityStoryDetailsBinding.topNavBar.navTitle.text = getString(R.string.story_details)
        activityStoryDetailsBinding.topNavBar.navTitle.setTextColor(ContextCompat.getColor(
            this,
            R.color.white
        ))
        activityStoryDetailsBinding.topNavBar.backIcon.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )

        parentPanel = activityStoryDetailsBinding.parentPanel

        storyPhotoAdapter = StoryPhotoAdapter(this, storyPhotoArray)
        activityStoryDetailsBinding.photoListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        activityStoryDetailsBinding.photoListRv.adapter = storyPhotoAdapter
        storyPhotoAdapter.setOnItemClickListener(this)
        storyPhotoAdapter.notifyDataSetChanged()
        getBundleData()

    }
    private fun getBundleData() {
        val extras = intent.extras
        if (null != extras) {
            storyId = extras.getInt(UrlManager.PARAM_STORY_ID)
            getStoryDetails()
        }

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data

        if (appLinkAction != null) {
            if (appLinkAction == "android.intent.action.VIEW") {

                if (appLinkData!!.lastPathSegment != null)
                {
                    storyId = parseInt(appLinkData!!.lastPathSegment)
                }
            }
        }
    }

    private fun getStoryDetails() {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()

        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(METHOD_NAME, UrlManager.GET_STORY_DETAILS_METHOD_NAME)
        jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
        jsObj.addProperty(UrlManager.PARAM_STORY_ID, storyId)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        apiCall(
            applicationContext,
            UrlManager.MAIN_URL,
            UrlManager.GET_STORY_DETAILS_METHOD_NAME,
            params,
            commonModel
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccess(result: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onSuccess(result, apiName, commonModel)

        showLog("HOME_METHOD_NAME-result", result.toString())

        if(apiName.equals(UrlManager.GET_STORY_DETAILS_METHOD_NAME))
        {
            closeLoader()
            val model: ResponseStoryDetailsData = getGsonAsConvert().fromJson(result, ResponseStoryDetailsData::class.java)
            if (model.status!!&& model.data!=null) {
                storyPhotoArray.clear()
                storyPhotoArray.addAll(model.data!!.photo!!)
                storyPhotoAdapter.notifyDataSetChanged()

                storyRatingArray.clear()
                storyRatingArray.addAll(model.data!!.ratingData!!)

                setRatingAdapter()

                if(storyPhotoArray.size>0)
                {
                    activityStoryDetailsBinding.noPicAvailable.visibility = View.GONE
                    activityStoryDetailsBinding.photoListRv.visibility = View.VISIBLE

                }else{
                    activityStoryDetailsBinding.photoListRv.visibility = View.GONE
                    activityStoryDetailsBinding.noPicAvailable.visibility = View.VISIBLE
                }

                if(model.data!!.writerData!=null){
                    if(model.data!!.writerData!!.photo!=null&& model.data!!.writerData!!.photo!!.isNotEmpty()){
                        Glide.with(this)
                            .load(model.data!!.writerData!!.photo)
                            .error(R.drawable.ic_default_image)
                            .into(activityStoryDetailsBinding.storyWriterImage)
                        //writerImage = model.data!!.writerData!!.photo!!
                        activityStoryDetailsBinding.storyWriterName.text = model.data!!.writerData!!.name!!
                        activityStoryDetailsBinding.storyWriterEmail.text = model.data!!.writerData!!.email!!
                        writerId = model.data!!.writerData!!.id!!
                    }
                    /*  writerId = model.data!!.writerData!!.id!!
                      writerName = model.data!!.writerData!!.name!!*/

                    activityStoryDetailsBinding.storyWriterName.text = model.data!!.writerData!!.name
                }

                // scheduleAVisitBtn
                if(model.data!!.photo!=null && model.data!!.photo!!.isNotEmpty()){
                    Glide.with(this)
                        .load(model.data!!.photo!![0])
                        .error(R.drawable.ic_default_image)
                        .into(activityStoryDetailsBinding.storyImg)
                }

                activityStoryDetailsBinding.ratingTxt.text = model.data!!.ratingData!!.size.toString()+" "+"Reviews"

                if(model.data!!.isFollowing!!)
                {
                    activityStoryDetailsBinding.followBtn.visibility = View.GONE
                    activityStoryDetailsBinding.followingBtn.visibility = View.VISIBLE
                }else{
                    activityStoryDetailsBinding.followBtn.visibility = View.VISIBLE
                    activityStoryDetailsBinding.followingBtn.visibility = View.GONE
                }

                if(userPreference!!.user_id!! == model.data!!.writerData!!.id)
                {
                    activityStoryDetailsBinding.thisEditBtn.visibility = View.VISIBLE
                    activityStoryDetailsBinding.followBtn.visibility = View.GONE
                    activityStoryDetailsBinding.followingBtn.visibility = View.GONE
                    activityStoryDetailsBinding.favoritesLinLay.visibility = View.GONE
                }else{
                    activityStoryDetailsBinding.thisEditBtn.visibility = View.GONE
                }



                rating = model.data!!.rating!!.toFloat().toDouble()


                if(model.data!!.isFavourite!!)
                {
                    activityStoryDetailsBinding.favImage.setImageBitmap(null)
                    activityStoryDetailsBinding.favImage.setImageDrawable(resources.getDrawable(R.drawable.favorite_gold_icon))

                }else{
                    activityStoryDetailsBinding.favImage.setImageBitmap(null)
                    activityStoryDetailsBinding.favImage.setImageDrawable(resources.getDrawable(R.drawable.favorite_gray_icon))
                }


                activityStoryDetailsBinding.storyGenre.text = model.data!!.genre
                activityStoryDetailsBinding.storyTitle.text = model.data!!.title
                activityStoryDetailsBinding.storyContent.text = model.data!!.content

            } else {
                showSnackBar(activityStoryDetailsBinding.photoListRv, model.message)
            }
        }else if(apiName.equals(UrlManager.SET_STORY_RATING)){
            getStoryDetails()
            closeLoader()
        }else if(apiName.equals(UrlManager.UPDATE_BOOKMARK_STATUS)){

            closeLoader()

            val model: ResponseUpdateBookmarkData = getGsonAsConvert().fromJson(
                result,
                ResponseUpdateBookmarkData::class.java
            )
            if (model.status!!) {
                getStoryDetails()
                showSnackBar(activityStoryDetailsBinding.ratingsListRv, "Bookmark Updated Successfully")
            }else{
                showSnackBar(activityStoryDetailsBinding.ratingsListRv, model.message)
            }
        }else if(apiName.equals(UrlManager.UPDATE_FOLLOWING_STATUS)){

            closeLoader()

            val model: ResponseUpdateFollowing = getGsonAsConvert().fromJson(
                result,
                ResponseUpdateFollowing::class.java
            )
            if (model.status!!) {
                getStoryDetails()
                showSnackBar(activityStoryDetailsBinding.ratingsListRv, model.message)
            }else{
                showSnackBar(activityStoryDetailsBinding.ratingsListRv, model.message)
            }
        }
    }
    override fun onFailure(message: String?, apiName: String?, commonModel: CommonValueModel?) {
        super.onFailure(message, apiName, commonModel)
        closeLoader()
        showLog("HOME_METHOD_NAME-result", message.toString())
    }
    private fun setRatingAdapter() {
        if(storyRatingArray.size>0)
        {
            activityStoryDetailsBinding.ratingsListRv.visibility = View.VISIBLE
            activityStoryDetailsBinding.noRatingsAvailable.visibility = View.GONE
        }else{
            activityStoryDetailsBinding.ratingsListRv.visibility = View.GONE
            activityStoryDetailsBinding.noRatingsAvailable.visibility = View.VISIBLE
        }
        storyRatingAdapter = StoryRatingAdapter(this, storyRatingArray)
        activityStoryDetailsBinding.ratingsListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        activityStoryDetailsBinding.ratingsListRv.adapter = storyRatingAdapter
    }






    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backIcon -> {
                finish()
            }
            R.id.shareBtn -> {

                val rootView = window.decorView.findViewById<View>(android.R.id.content)
                val screenshot: Bitmap = getScreenShot(rootView)!!
                share(screenshot)

            }
            R.id.favoritesLinLay -> {
                val params = RequestParams()
                var commonModel = CommonValueModel()
                val jsObj = Gson().toJsonTree(API()) as JsonObject
                jsObj.addProperty(METHOD_NAME, UrlManager.UPDATE_BOOKMARK_STATUS)
                jsObj.addProperty(UrlManager.PARAM_STORY_ID, storyId)
                jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
                showLog("HOME_METHOD_NAME-param", jsObj.toString())
                params.put("data", toBase64(jsObj.toString()))
                apiCall(
                    applicationContext,
                    UrlManager.MAIN_URL,
                    UrlManager.UPDATE_BOOKMARK_STATUS,
                    params,
                    commonModel
                )
            }

            R.id.thisEditBtn -> {
                val Messages = Intent(this, EditStoryActivity::class.java)
                Messages.putExtra(UrlManager.PARAM_STORY_ID, storyId)
                startActivity(Messages)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
            R.id.ratingLinLay -> {
                if(userPreference!!.user_id!! != writerId) {
                    initializeChildBottomBarBottomBar("Rating & Review")
                }
            }
            R.id.followBtn -> {
                setFollowingStatus()
            }
            R.id.followingBtn -> {
                setFollowingStatus()
            }
            R.id.userLinLay -> {
                val intent = Intent(applicationContext , MainActivity::class.java)
                intent.putExtra(PARAM_COMING_FROM, TAB_BAR_PROFILE)
                intent.putExtra(PARAM_USER_ID, writerId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
        }
    }






    var bottomSheetDialog: BottomSheetDialog? = null
    fun initializeChildBottomBarBottomBar(header: String?) {
        bottomSheetDialog = BottomSheetDialog(this)
        val parentView = layoutInflater.inflate(R.layout.bottom_sheet_rating, null)
        bottomSheetDialog!!.setContentView(parentView)
        parentView.minimumHeight = 200
        (parentView.parent as View).setBackgroundColor(Color.TRANSPARENT)
        (parentView.findViewById<View>(R.id.userNameTxt) as TextView).text = header
        parentView.findViewById<View>(R.id.cancelImg)
            .setOnClickListener { bottomSheetDialog!!.cancel() }

        (parentView.findViewById<View>(R.id.thisRatingBar) as CustomRatingBar).setScore(rating.toFloat())

        parentView.findViewById<View>(R.id.submitTxt).setOnClickListener {
            if ((parentView.findViewById<View>(R.id.thisRatingBar) as CustomRatingBar).getScore() === 0.0f) {
                Toast.makeText(this@StoryDetailsActivity, resources.getString(R.string.rating_msg)
                    , Toast.LENGTH_SHORT).show()
            } else {
                bottomSheetDialog!!.cancel()

                setStoryRating((parentView.findViewById<View>(R.id.thisRatingBar) as CustomRatingBar).getScore(),
                    (parentView.findViewById<View>(R.id.commentEdt) as EditText).text.toString())
            }
        }
        bottomSheetDialog!!.show()
    }


    fun getScreenShot(view: View): Bitmap? {
        val screenView = view.rootView
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    fun share(bitmap: Bitmap) {
        val fileName = "share.png"
        val dir = File(cacheDir, "images")
        dir.mkdirs()
        val file = File(dir, fileName)
        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val uri = FileProvider.getUriForFile(
            this,
            "com.itsupportwale.dastaan.fileprovider", file
        )
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        intent.setDataAndType(uri, contentResolver.getType(uri))
        intent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT")
        intent.putExtra(Intent.EXTRA_TEXT, "EXTRA_TEXT")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(intent, "Share Image"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setStoryRating( rating:Float, review:String ) {
        val params = RequestParams()
        showLoader(resources.getString(R.string.please_wait))
        var commonModel = CommonValueModel()
        val jsObj = Gson().toJsonTree(API()) as JsonObject
        jsObj.addProperty(METHOD_NAME, UrlManager.SET_STORY_RATING)
        jsObj.addProperty(UrlManager.PARAM_REVIEWER_ID, userPreference!!.user_id)
        jsObj.addProperty(UrlManager.PARAM_STORY_ID, storyId)
        jsObj.addProperty(UrlManager.PARAM_RATING, rating)
        jsObj.addProperty(UrlManager.PARAM_REVIEW, review)
        showLog("HOME_METHOD_NAME-param", jsObj.toString())
        params.put("data", toBase64(jsObj.toString()))
        apiCall(
            applicationContext,
            UrlManager.MAIN_URL,
            UrlManager.SET_STORY_RATING,
            params,
            commonModel
        )
    }

    private fun setFollowingStatus()
    {
        if(writerId!=0)
        {
            val params = RequestParams()
            showLoader(resources.getString(R.string.please_wait))
            var commonModel = CommonValueModel()
            val jsObj = Gson().toJsonTree(API()) as JsonObject
            jsObj.addProperty(METHOD_NAME, UrlManager.UPDATE_FOLLOWING_STATUS)
            jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
            jsObj.addProperty(UrlManager.PARAM_WRITER_ID, writerId)
            showLog("HOME_METHOD_NAME-param", jsObj.toString())
            params.put("data", toBase64(jsObj.toString()))
            apiCall(
                applicationContext,
                UrlManager.MAIN_URL,
                UrlManager.UPDATE_FOLLOWING_STATUS,
                params,
                commonModel
            )
        }else{
            showSnackBar(activityStoryDetailsBinding.favImage,"Writer Missing")
        }
    }

    override fun onItemListItemClickListener(position: Int) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(storyPhotoArray[position])
        startActivity(i)
    }
}