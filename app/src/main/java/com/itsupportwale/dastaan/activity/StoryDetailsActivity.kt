package com.itsupportwale.dastaan.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.PARAM_WRITER_ID
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.*
import com.loopj.android.http.RequestParams
import kotlinx.android.synthetic.main.activity_story_details.*


class StoryDetailsActivity : BaseActivity(), View.OnClickListener, StoryPhotoAdapter.onRecyclerViewItemClickListener{
    lateinit var activityStoryDetailsBinding: ActivityStoryDetailsBinding

    private val storyPhotoArray: ArrayList<String> = ArrayList()
    lateinit var storyPhotoAdapter : StoryPhotoAdapter
    private val storyRatingArray: ArrayList<ResponseStoryDetailsData.RatingDatum> = ArrayList()
    lateinit var storyRatingAdapter : StoryRatingAdapter
    private var userPreference: UserPreference? = null

    var storyId: Int=0
    var writerId: Int=0
    var rating = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStoryDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_story_details
        )
        userPreference = UserPreference.getInstance(this)
        initView()
    }

    override fun onResume() {
        super.onResume()
        getStoryDetails()
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
        activityStoryDetailsBinding.topNavBar.backIcon.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )

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

                if(userPreference!!.user_id!! == model.data!!.writerData!!.id)
                {
                    activityStoryDetailsBinding.thisEditBtn.visibility = View.VISIBLE
                }else{
                    activityStoryDetailsBinding.thisEditBtn.visibility = View.GONE
                }


                if(model.data!!.isFollowing!!)
                {
                    activityStoryDetailsBinding.followBtn.visibility = View.GONE
                    activityStoryDetailsBinding.followingBtn.visibility = View.VISIBLE
                }else{
                    activityStoryDetailsBinding.followBtn.visibility = View.VISIBLE
                    activityStoryDetailsBinding.followingBtn.visibility = View.GONE
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
                initializeChildBottomBarBottomBar("Rating & Review")
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
                intent.putExtra(PARAM_WRITER_ID, writerId)
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