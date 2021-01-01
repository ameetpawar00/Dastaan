package com.itsupportwale.dastaan.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.loopj.android.http.RequestParams
import com.itsupportwale.dastaan.R
import com.itsupportwale.dastaan.adapters.StoryPhotoAdapter
import com.itsupportwale.dastaan.adapters.StoryRatingAdapter
import com.itsupportwale.dastaan.beans.GetStoryDetailsModel
import com.itsupportwale.dastaan.databinding.ActivityStoryDetailsBinding
import com.itsupportwale.dastaan.servermanager.UrlManager
import com.itsupportwale.dastaan.servermanager.UrlManager.Companion.METHOD_NAME
import com.itsupportwale.dastaan.servermanager.request.CommonValueModel
import com.itsupportwale.dastaan.utility.API
import com.itsupportwale.dastaan.utility.CustomRatingBar
import com.itsupportwale.dastaan.utility.UserPreference
import dev.amin.tagadapter.Tag
import dev.amin.tagadapter.TagAdapter

class StoryDetailsActivity : BaseActivity(), View.OnClickListener, StoryPhotoAdapter.onRecyclerViewItemClickListener{
    lateinit var activityStoryDetailsBinding: ActivityStoryDetailsBinding

    private val storyPhotoArray: ArrayList<String> = ArrayList()
    lateinit var storyPhotoAdapter : StoryPhotoAdapter
    private val storyRatingArray: ArrayList<GetStoryDetailsModel.RatingDatum> = ArrayList()
    lateinit var storyRatingAdapter : StoryRatingAdapter
    private var userPreference: UserPreference? = null

    var storyId: String=""
    var rating = 0.0

    lateinit var featureListAdapter: TagAdapter
    private val featureList: ArrayList<Tag> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStoryDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_story_details
        )
        userPreference = UserPreference.getInstance(this)

    }

    override fun onResume() {
        super.onResume()
        initView()
    }
    fun initView()
    {
        activityStoryDetailsBinding.shareBtn.setOnClickListener(this)
        activityStoryDetailsBinding.ratingLinLay.setOnClickListener(this)
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
            storyId = extras.getString(UrlManager.PARAM_STORY_ID, "")
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
     /*
        if(apiName.equals(UrlManager.GET_STORY_DETAILS_METHOD_NAME))
        {
            closeLoader()
            val model: GetStoryDetailsModel = getGsonAsConvert().fromJson(result, GetStoryDetailsModel::class.java)
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

                if(model.data!!.ownerData!=null){
                    if(model.data!!.ownerData!!.photo!=null&& model.data!!.ownerData!!.photo!!.isNotEmpty()){
                        Glide.with(this)
                            .load(model.data!!.ownerData!!.photo)
                            .error(R.drawable.ic_default_image)
                            .into(activityStoryDetailsBinding.storyWriterImage)
                        ownerImage = model.data!!.ownerData!!.photo!!
                    }
                    ownerId = model.data!!.ownerData!!.id!!
                    ownerName = model.data!!.ownerData!!.name!!

                    activityStoryDetailsBinding.storyWriterName.text = model.data!!.ownerData!!.name
                }

                // scheduleAVisitBtn
                if(model.data!!.photo!=null && model.data!!.photo!!.isNotEmpty()){
                    Glide.with(this)
                        .load(model.data!!.photo!![0])
                        .error(R.drawable.ic_default_image)
                        .into(activityStoryDetailsBinding.storyImg)
                }

                activityStoryDetailsBinding.ratingTxt.text = model.data!!.ratingData!!.size.toString()+" "+"Reviews"

                if(userPreference!!.user_id!! == model.data!!.ownerData!!.id)
                {
                    activityStoryDetailsBinding.thisChatBtn.visibility = View.GONE
                    activityStoryDetailsBinding.thisEditBtn.visibility = View.VISIBLE
                }else{
                    activityStoryDetailsBinding.thisChatBtn.visibility = View.VISIBLE
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



                var theseFeaturesList = model.data!!.features!!.split(",")
                featureList.clear()
                for (feature in theseFeaturesList) {
                    if(!feature.equals(""))
                    {
                        var tag = Tag()
                        tag.title= feature
                        featureList.add(tag)
                    }
                }
                if(featureList.size>0)
                {

                    activityStoryDetailsBinding.noDataAvailable.visibility = View.GONE
                    activityStoryDetailsBinding.thisListRv.visibility = View.VISIBLE

                }else{
                    activityStoryDetailsBinding.thisListRv.visibility = View.GONE
                    activityStoryDetailsBinding.noDataAvailable.visibility = View.VISIBLE
                }

                setFeatureAdapter()






                activityStoryDetailsBinding.storyTitle.text = model.data!!.title

            } else {
                showSnackBar(activityStoryDetailsBinding.photoListRv, model.message)
            }
        }else if(apiName.equals(UrlManager.SET_STORY_RATING)){
            closeLoader()
        }else if(apiName.equals(UrlManager.SET_FAV)){

            closeLoader()

            val model: SetFavModel = getGsonAsConvert().fromJson(result, SetFavModel::class.java)
            if (model.status!!) {
                if(model.message.equals("Added to favourite successfully.."))
                {
                    activityStoryDetailsBinding.favImage.setImageBitmap(null)
                    activityStoryDetailsBinding.favImage.setImageDrawable(resources.getDrawable(R.drawable.favorite_gold_icon))
                }else{
                    activityStoryDetailsBinding.favImage.setImageBitmap(null)
                    activityStoryDetailsBinding.favImage.setImageDrawable(resources.getDrawable(R.drawable.favorite_gray_icon))
                }
            }
        }*/
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

    private fun setFeatureAdapter() {
        featureListAdapter = TagAdapter(this, featureList, 0)
        activityStoryDetailsBinding.thisListRv.layoutManager = LinearLayoutManager(this)
        activityStoryDetailsBinding.thisListRv.adapter = featureListAdapter
        featureListAdapter.notifyDataSetChanged()
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
                jsObj.addProperty(METHOD_NAME, UrlManager.SET_FAV)
                jsObj.addProperty(UrlManager.PARAM_STORY_ID, storyId)
                jsObj.addProperty(UrlManager.PARAM_USER_ID, userPreference!!.user_id)
                showLog("HOME_METHOD_NAME-param", jsObj.toString())
                params.put("data", toBase64(jsObj.toString()))
                apiCall(
                    applicationContext,
                    UrlManager.MAIN_URL,
                    UrlManager.SET_FAV,
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

    override fun onItemListItemClickListener(position: Int, tabType: Int) {
        TODO("Not yet implemented")
    }
}